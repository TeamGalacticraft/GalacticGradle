package net.galacticraft.plugins.publishing.nexus.internal;

import org.gradle.api.Task;
import org.gradle.api.internal.artifacts.ivyservice.projectmodule.ProjectPublicationRegistry;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.internal.service.ServiceRegistry;
import org.gradle.model.Model;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;

public class InternalPublishPluginRules extends RuleSource {
    @Model
    PublishingExtension publishing(ExtensionContainer extensions) {
        return extensions.getByType(PublishingExtension.class);
    }

    @Model
    ProjectPublicationRegistry projectPublicationRegistry(ServiceRegistry serviceRegistry) {
        return serviceRegistry.get(ProjectPublicationRegistry.class);
    }

    @Mutate
    void addConfiguredPublicationsToProjectPublicationRegistry(ProjectPublicationRegistry projectPublicationRegistry, PublishingExtension extension) {
        // this rule is just here to ensure backwards compatibility for builds that
        // create publications with model rules
    }

    @Mutate
    void tasksDependOnProjectPublicationRegistry(ModelMap<Task> tasks, PublishingExtension extension) {
        // this rule is just here to ensure backwards compatibility for builds that
        // create publications with model rules
    }
}
