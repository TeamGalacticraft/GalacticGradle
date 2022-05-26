package net.galacticraft.plugins.publishing.nexus.internal.extension;

import org.gradle.api.Action;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.publish.PublicationContainer;

public class InternalDefaultPublishExtension implements InternalPublishExtension {
    private final RepositoryHandler repositories;
    private final PublicationContainer publications;

    public InternalDefaultPublishExtension(RepositoryHandler repositories, PublicationContainer publications) {
        this.repositories = repositories;
        this.publications = publications;
    }

    @Override
    public RepositoryHandler getRepositories() {
        return repositories;
    }

    @Override
    public void repositories(Action<? super RepositoryHandler> configure) {
        configure.execute(repositories);
    }

    @Override
    public PublicationContainer getPublications() {
        return publications;
    }

    @Override
    public void publications(Action<? super PublicationContainer> configure) {
        configure.execute(publications);
    }
}
