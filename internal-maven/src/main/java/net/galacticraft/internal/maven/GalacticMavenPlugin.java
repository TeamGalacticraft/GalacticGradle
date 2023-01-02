/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.internal.maven;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.plugins.signing.Sign;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;
import org.jetbrains.annotations.NotNull;

import lombok.NonNull;
import net.galacticraft.gradle.core.internal.SpecialMavenPlugin;
import net.galacticraft.gradle.core.internal.ext.SpecialPublishExt;
import net.galacticraft.gradle.core.model.RepositoryCredentials;
import net.galacticraft.gradle.core.plugin.Configurable;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamConstants;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;
import net.galacticraft.gradle.core.util.StringUtil;
import net.galacticraft.internal.maven.pom.PomActionImpl;
import net.galacticraft.internal.maven.pom.PomIO;
import net.galacticraft.internal.maven.pom.PomJsonSpec;
import net.galacticraft.internal.maven.task.TestPublishTask;

public class GalacticMavenPlugin extends GradlePlugin
{
    public static final String PUBLISH_TO_GALACTIC_MAVEN_TASKNAME = "publishToGalacticMaven";

    public static final String PUBLISH_TO_LOCAL_MAVEN_TASKNAME = "publishToLocalMaven";

    private BooleanProperty signPublication;

    private List<Sign> signingTasks = null;

    @Override
    protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
    {
        this.signPublication = new BooleanProperty(target, "internal.maven.sign", true);
        BooleanProperty debug = new BooleanProperty(target, "internal.maven.debug", false);
        conditionalLog.setConditional(debug);

        plugins.apply(SpecialMavenPlugin.class);

        GalacticInternalExtension ext = extensions.findOrCreate("maven", GalacticInternalExtension.class, target);

        TaskProvider<PublishToMavenRepository> publishMavenTask =
            tasks.registerTask(PUBLISH_TO_GALACTIC_MAVEN_TASKNAME, PublishToMavenRepository.class);
        TaskProvider<PublishToMavenRepository> publishLocalTask =
            tasks.registerTask(PUBLISH_TO_LOCAL_MAVEN_TASKNAME, PublishToMavenRepository.class);
        TaskProvider<TestPublishTask> testPublishTask = tasks.registerTask("testPublish", TestPublishTask.class);

        PomIO io = new PomIO(target);

        afterEval(project -> {
            if (signPublication.get()) {
                plugins.apply(SigningPlugin.class);
            }

            List<AbstractArchiveTask> archiveTasks = ext.getTasks().get();

            RepositoryCredentials nexusLogin =
                RepositoryCredentials.create(target).username(ext.getNexusUsername()).password(ext.getNexusPassword());

            extensions.container().configure(SpecialPublishExt.class, special -> {
                MavenArtifactRepository repository =
                    special.getRepositories().maven(configureDefaultRepository(target, nexusLogin));
                MavenPublication publication =
                    special.getPublications().create(ext.getPublicationName().get(), MavenPublication.class);

                Configurable.configureIfNonNull(publication, pub -> {
                    pub.setArtifactId(configureArtifactId(target, io));
                    pub.setGroupId(target.getGroup().toString());
                    pub.setVersion(target.getVersion().toString());

                    pub.pom(PomActionImpl.getPomAction(io, ext.getXml()));

                    if (archiveTasks.isEmpty()) {
                        pub.from(target.getComponents().getByName("java"));
                    } else {
                        archiveTasks.forEach((f) -> pub.artifact(f));
                    }
                });

                String keyId = variables.get("signing.keyId");
                String password = variables.get("signing.password");

                if (signPublication.get() && allNotNull(keyId, password)) {
                    SigningExtension signing = extensions.find(SigningExtension.class);
                    signingTasks = signing.sign(publication);
                }

                testPublishTask.configure(task -> {
                    task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
                    task.getMavenRepository().set(repository);
                    task.getMavenPublication().set(publication);
                    task.setDescription(
                        "[Internal Maven] Prints out the Publications identity and all artifacts that would be published");
                });

                if (nexusLogin.notNull()) {
                    publishMavenTask.configure(task -> {
                        task.setPublication(publication);
                        task.setRepository(repository);
                        task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
                        task.setDescription("[Internal Maven] Publishes " + ext.getPublicationName().get()
                            + " to the Team Galacticraft Repository");
                        if (signingTasks != null) {
                            task.dependsOn(signingTasks);
                        }
                    });
                }

                publishLocalTask.configure(task -> {
                    task.setPublication(publication);
                    task.setRepository(repositories.handler().mavenLocal());
                    task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
                    task.setDescription("[Internal Maven] Publishes " + ext.getPublicationName().get()
                        + " to your local Maven repository");
                    if (signingTasks != null) {
                        task.dependsOn(signingTasks);
                    }
                });
            });
        });
    }

    private String configureArtifactId(@NonNull Project project, @NonNull PomIO io)
    {
        final Optional<String> modName = getModName();
        final Optional<String> modId = getModId();
        final Optional<PomJsonSpec> spec = io.getJsonSpec();

        if (spec.isPresent() && io.hasProperty("artifactId"))
            return spec.get().getArtifactId();

        else if (modName.isPresent())
            return modName.get();

        else if (modId.isPresent())
            return modId.get();

        else
            return project.getName().toLowerCase().replaceAll("[^\\w\\d]", "-");
    }

    @Nullable
    private Optional<String> getModId()
    {
        if (variables.has("mod.id"))
            return Optional.of(variables.get("mod.id"));

        else if (variables.has("mod_id"))
            return Optional.of(variables.get("mod_id"));

        else if (variables.has("modid"))
            return Optional.of(variables.get("modid"));

        else if (variables.has("modId"))
            return Optional.of(variables.get("modId"));

        else
            return Optional.empty();
    }

    @Nullable
    private Optional<String> getModName()
    {
        if (variables.has("mod.name"))
            return Optional.of(variables.get("mod.name"));

        else if (variables.has("mod_name"))
            return Optional.of(variables.get("mod_name"));

        else if (variables.has("modname"))
            return Optional.of(variables.get("modname"));

        else if (variables.has("modName"))
            return Optional.of(variables.get("modName"));

        else
            return Optional.empty();
    }

    private Action<MavenArtifactRepository> configureDefaultRepository(Project project,
        RepositoryCredentials nexusLogin)
    {
        boolean isSnapshotVersion = project.getVersion().toString().endsWith("SNAPSHOT");

        if (StringUtil.containsIgnoreCase(project.getName(), "legacy")) {
            if (isSnapshotVersion) {
                return TeamConstants.Repositories.Hosted.legacySnapshots(nexusLogin);
            }

            return TeamConstants.Repositories.Hosted.legacyReleases(nexusLogin);
        }

        if (isSnapshotVersion) {
            return TeamConstants.Repositories.Hosted.mavenSnapshots(nexusLogin);
        }

        return TeamConstants.Repositories.Hosted.mavenReleases(nexusLogin);
    }

    @Override
    protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
    {

    }

    @SafeVarargs
    private final <T extends Object> boolean allNotNull(@Nullable T... objects)
    {
        for (T obj : objects) {
            if (obj == null) {
                return false;
            }
        }

        return true;
    }
}
