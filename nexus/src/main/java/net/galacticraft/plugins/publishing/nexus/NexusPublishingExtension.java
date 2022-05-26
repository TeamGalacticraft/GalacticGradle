package net.galacticraft.plugins.publishing.nexus;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPom;

import lombok.Getter;

@Getter
public class NexusPublishingExtension {
    
    public static String DEFAULT_GROUP_NAME = "mod.dependency";

    private Property<String> nexusUsername;
    private Property<String> nexusPassword;

    private Property<String> group;
    private Property<String> artifactId;
    private Property<String> version;

    private ListProperty<Object> archives;

    private Action<? super MavenPom> pom;

    @Inject
    public NexusPublishingExtension(final ObjectFactory factory) {
        this.nexusUsername = factory.property(String.class).convention("unset");
        this.nexusPassword = factory.property(String.class).convention("unset");
        this.group = factory.property(String.class);
        this.artifactId = factory.property(String.class);
        this.version = factory.property(String.class);
        this.archives = factory.listProperty(Object.class).empty();
    }

    public void pom(Action<? super MavenPom> configure) {
        this.pom = configure;
    }

    public void username(final String username) {
        this.getNexusUsername().set(username);
    }

    public void archives(final Object... files) {
        this.getArchives().addAll(files);
    }

    public void password(final String password) {
        this.getNexusPassword().set(password);
    }

    public void group(final String group) {
        this.getGroup().set(group);
    }

    public void artifactId(final String artifactId) {
        this.getArtifactId().set(artifactId);
    }

    public void version(final String version) {
        this.getVersion().set(version);
    }

    public boolean isUsernameSet() {
        return !this.getNexusUsername().get().equals("unset");
    }

    public String nexusUsername() {
        return this.getNexusUsername().get();
    }

    public boolean isPasswordSet() {
        return !this.getNexusPassword().get().equals("unset");
    }

    public String nexusPassword() {
        return this.getNexusPassword().get();
    }
}
