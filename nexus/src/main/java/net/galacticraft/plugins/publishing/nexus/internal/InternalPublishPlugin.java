package net.galacticraft.plugins.publishing.nexus.internal;

import javax.inject.Inject;
import org.gradle.api.InvalidUserDataException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.internal.CollectionCallbackActionDecorator;
import org.gradle.api.internal.artifacts.ArtifactPublicationServices;
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.ProjectPublicationRegistry;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.publish.Publication;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.internal.DefaultPublicationContainer;
import org.gradle.api.publish.internal.PublicationInternal;
import org.gradle.internal.Cast;
import org.gradle.internal.model.RuleBasedPluginListener;
import org.gradle.internal.reflect.Instantiator;
import net.galacticraft.plugins.publishing.nexus.internal.extension.InternalDefaultPublishExtension;
import net.galacticraft.plugins.publishing.nexus.internal.extension.InternalPublishExtension;

public class InternalPublishPlugin implements Plugin<Project> {

    public static final String PUBLISH_TASK_GROUP = "galactic-publishing";
    public static final String PUBLISH_LIFECYCLE_TASK_NAME = "publishMod";
    private static final String VALID_NAME_REGEX = "[A-Za-z0-9_\\-.]+";

    private final Instantiator instantiator;
    private final ArtifactPublicationServices publicationServices;
    private final ProjectPublicationRegistry projectPublicationRegistry;
    private CollectionCallbackActionDecorator collectionCallbackActionDecorator;

    @Inject
    public InternalPublishPlugin(ArtifactPublicationServices publicationServices, Instantiator instantiator, ProjectPublicationRegistry projectPublicationRegistry, CollectionCallbackActionDecorator collectionCallbackActionDecorator) {
        this.publicationServices = publicationServices;
        this.instantiator = instantiator;
        this.projectPublicationRegistry = projectPublicationRegistry;
        this.collectionCallbackActionDecorator = collectionCallbackActionDecorator;
    }

    @Override
    public void apply(final Project project) {
        RepositoryHandler repositories = publicationServices.createRepositoryHandler();
        PublicationContainer publications = instantiator.newInstance(DefaultPublicationContainer.class, instantiator, collectionCallbackActionDecorator);
        InternalPublishExtension extension = project.getExtensions().create(InternalPublishExtension.class, InternalPublishExtension.NAME, InternalDefaultPublishExtension.class, repositories, publications);

        extension.getPublications().all(publication -> {
            PublicationInternal<?> internalPublication = Cast.uncheckedNonnullCast(publication);
            ProjectInternal projectInternal = (ProjectInternal) project;
            projectPublicationRegistry.registerPublication(projectInternal, internalPublication);
        });
        bridgeToSoftwareModelIfNeeded((ProjectInternal) project);
        validatePublishingModelWhenComplete(project, extension);
    }

    private void validatePublishingModelWhenComplete(Project project, InternalPublishExtension extension) {
        project.afterEvaluate(projectAfterEvaluate -> {
            for (ArtifactRepository repository : extension.getRepositories()) {
                String repositoryName = repository.getName();
                if (!repositoryName.matches(VALID_NAME_REGEX)) {
                    throw new InvalidUserDataException("Repository name '" + repositoryName + "' is not valid for publication. Must match regex " + VALID_NAME_REGEX + ".");
                }
            }
            for (Publication publication : extension.getPublications()) {
                String publicationName = publication.getName();
                if (!publicationName.matches(VALID_NAME_REGEX)) {
                    throw new InvalidUserDataException("Publication name '" + publicationName + "' is not valid for publication. Must match regex " + VALID_NAME_REGEX + ".");
                }
            }
        });
    }

    private void bridgeToSoftwareModelIfNeeded(ProjectInternal project) {
        project.addRuleBasedPluginListener(new RuleBasedPluginListener() {
            @Override
            public void prepareForRuleBasedPlugins(Project project) {
                project.getPluginManager().apply(InternalPublishPluginRules.class);
            }
        });
    }

}
