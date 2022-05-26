package net.galacticraft.plugins.publishing.nexus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.jvm.tasks.Jar;
import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.common.plugins.property.BooleanProperty;
import net.galacticraft.plugins.publishing.nexus.internal.InternalMavenPlugin;
import net.galacticraft.plugins.publishing.nexus.internal.extension.InternalPublishExtension;
import net.galacticraft.plugins.publishing.nexus.task.PublishToNexus;
import net.galacticraft.plugins.publishing.nexus.util.ConditionalTaskCollectionBuilder;

public class NexusPublishingPlugin extends GradlePlugin {

    private Optional<MavenPublication> modPublication = Optional.empty();
    private Optional<MavenArtifactRepository> modMavenRepository = Optional.empty();

    @Override
    public void plugin() {

        applyPlugin(InternalMavenPlugin.class);
        NexusPublishingExtension extension = extensions().findOrCreate("nexus", NexusPublishingExtension.class);

        project().afterEvaluate(p -> {

            BooleanProperty debugProvider = new BooleanProperty(project(), "nexus.debug");

            if (project().hasProperty("nexusUsername")) {
                extension.username((String) project().property("nexusUsername"));
            }
            if (project().hasProperty("nexusPassword")) {
                extension.password((String) project().property("nexusPassword"));
            }

            ConditionalTaskCollectionBuilder taskList = new ConditionalTaskCollectionBuilder(project());

            TaskProvider<Jar> jarTask = tasks().withType(Jar.class).named("jar");

            // if we have no credentials and at the very least the jar task, lets stop while
            // we are ahead and cry
            if (doWeContinue(extension, jarTask)) {
                project().getExtensions().configure(InternalPublishExtension.class, publishing -> {

                    modPublication = Optional.of(publishing.getPublications().register(cleanDisplayName(project().getDisplayName()), MavenPublication.class, maven -> {

                        // If no file objects are provided, lets attempt to get the jars from
                        // tasks that produce them. With a little bit of semi-sane logic
                        if (extension.getArchives().get().isEmpty()) {

                            maven.artifact(jarTask.get().getArchiveFile());

                            // Check if the additional sources & javadoc jar taks are present
                            taskList.getTasksList().forEach(task -> {
                                if (!task.getArchiveClassifier().get().isEmpty()) {
                                    maven.artifact(task.getArchiveFile(), meta -> {
                                        meta.setClassifier(task.getArchiveClassifier().get());
                                    });
                                }
                            });

                            // If they were provided, then yay, lets just work with those while
                            // ensuring additional archives have classifiers so maven doesn't
                            // start throwing a fit and cry because maven
                        } else {
                            List<AbstractArchiveTask> archives = new ArrayList<>(extension.getArchives().get().size());
                            archives.addAll(extension.getArchives().get().stream().map(o -> resolve(o)).collect(Collectors.toList()));

                            // grab first archive
                            AbstractArchiveTask f1 = archives.get(0);
                            maven.artifact(f1.getArchiveFile());
                         // now remove it
                            archives.remove(0);
                            
                            // then we can just run forEach on the rest since they need classifiers
                            archives.forEach(fn -> {
                                if (!fn.getArchiveClassifier().get().isEmpty()) {
                                    maven.artifact(fn.getArchiveFile(), meta -> {
                                        meta.setClassifier(fn.getArchiveClassifier().get());
                                    });
                                }
                            });
                        }
                        
                        // Set the artifactId using the supplied string via our extension if not null,
                        // fallback to project's DisplayName if null
                        Provider<String> artifactId = project().provider(() -> extension.getArtifactId().isPresent() ? extension.getArtifactId().get() : project().getDisplayName() == null ? null : cleanDisplayName(project().getDisplayName()));
                        maven.setArtifactId(artifactId.get());
                        
                        // Set the groupId using the supplied string via our extension if not null,
                        // fallback to project's group declaration if null
                        Provider<String> group = project().provider(() -> extension.getGroup().isPresent() ? extension.getGroup().get() : NexusPublishingExtension.DEFAULT_GROUP_NAME);
                        maven.setGroupId(group.get());

                        // Set the version using the supplied string via our extension if not null,
                        // fallback to project's version declaration if null
                        Provider<String> version = project().provider(() -> extension.getVersion().isPresent() ? extension.getVersion().get() : project().getVersion() == null ? null : String.valueOf(project().getVersion()));
                        maven.setVersion(version.get());

                        if (extension.getPom() != null) {
                            maven.pom(extension.getPom());
                        }

                    }).get());

                    publishing.repositories(handler -> {
                        modMavenRepository = Optional.of(handler.maven(repo -> {
                            repo.setName("CommonMaven");
                            repo.setUrl("https://repo.galacticraft.net/repository/maven-common/");
                            repo.metadataSources(meta -> {
                                meta.mavenPom();
                                meta.artifact();
                                meta.gradleMetadata();
                            });
                            repo.credentials(PasswordCredentials.class, credential -> {
                                credential.setUsername(extension.nexusUsername());
                                credential.setPassword(extension.nexusPassword());
                            });
                        }));
                    });
                });
            } else {
                lifecycle("[GalacticGradle] NexusPublishing Plugin Disabled");
                lifecycle("[GalacticGradle] Reason: No credentials were provided to use for setup");
            }

            if (modPublication.isPresent() && modMavenRepository.isPresent()) {
                tasks().register("publishMod", PublishToNexus.class, task -> {
                    task.isDebug = project().getObjects().property(Boolean.class).convention(debugProvider.value());
                    task.setPublication(modPublication.get());
                    task.setRepository(modMavenRepository.get());
                    task.setGroup("GalacticGradle");
                    task.setDescription("Publishes the mod artifacts to MavenCommon Repository");
                });
            }
        });
    }

    private AbstractArchiveTask resolve(Object in) {
        // Grabs the file from an archive task. Allows build scripts to do things like
        // the jar task directly.
        if (in instanceof AbstractArchiveTask) {
            return ((AbstractArchiveTask) in);
        }
        // Grabs the file from an archive task wrapped in a provider. Allows Kotlin DSL
        // buildscripts to also specify
        // the jar task directly, rather than having to call #get() before running.
        else if (in instanceof TaskProvider<?>) {
            Object provided = ((TaskProvider<?>) in).get();

            // Check to see if the task provided is actually an AbstractArchiveTask.
            if (provided instanceof AbstractArchiveTask) {
                return ((AbstractArchiveTask) provided);
            }
        }
        return null;
    }

    private String cleanDisplayName(String displayName) {
        return displayName.replace("root project ", "").replaceAll("[-_\\+\\.\\\\\\/~\\!@\\#\\$\\%\\^&\\*\\(\\)\\{\\}\\[\\]']", "").trim();
    }

    private boolean doWeContinue(NexusPublishingExtension extension, TaskProvider<?> task) {
        return extension.isUsernameSet() && extension.isPasswordSet() && task.isPresent();
    }
}
