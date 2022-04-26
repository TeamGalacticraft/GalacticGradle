package net.galacticraft.plugins.curseforge.curse;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public interface ConfigurationContainer {
	Property<String> getProjectId();

	default void projectId(final String projectId) {
		this.getProjectId().set(projectId);
	}

	Property<String> getReleaseType();
	
	default void releaseType(final String version) {
		this.getReleaseType().set(version);
	}

	Property<String> getChangelog();
	
	default void changelog(final String changelog) {
		this.getChangelog().set(changelog);
	}

	Property<String> getChangelogType();
	
	default void changelogType(final String type) {
		this.getChangelogType().set(type);
	}
	
	ListProperty<Object> getGameVersions();
	
	default void gameVersions(final Object... gameVersions) {
		this.getGameVersions().addAll(gameVersions);
	}
}
