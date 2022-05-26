package net.galacticraft.plugins.publishing.nexus.internal.extension;

import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.PublicationContainer;

public interface InternalPublishExtension {

    String NAME = "galacticpublishing";

    RepositoryHandler getRepositories();

    void repositories(Action<? super RepositoryHandler> configure);

    PublicationContainer getPublications();

    void publications(Action<? super PublicationContainer> configure);
}
