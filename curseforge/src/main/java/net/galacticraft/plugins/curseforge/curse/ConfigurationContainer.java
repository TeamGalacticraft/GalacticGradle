package net.galacticraft.plugins.curseforge.curse;

import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public interface ConfigurationContainer {
	Property<String> getProjectId();

	default void projectId(final String projectId) {
		this.getProjectId().set(projectId);
	}
	
	default void projectId(final Object projectId) {
		this.getProjectId().set(String.valueOf(projectId));
	}
	
	default void projectId(final Integer projectId) {
		this.getProjectId().set(String.valueOf(projectId));
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
	
	Property<FileArtifact> getMainFile();
	
	default void mainFile(final Object file) {
		this.getMainFile().set(new FileArtifact(file, null));;
	}
	
	default void mainFile(final Object file, final String displayName) {
		this.getMainFile().set(new FileArtifact(file, displayName));
	}

//	ListProperty<FileArtifact> getExtraFiles();
//	
//	default void addFile(final Object file) {
//		this.getExtraFiles().add(new FileArtifact(file));
//	}
//	
//	default void addFile(final Object file, final String displayName) {
//		this.getExtraFiles().add(new FileArtifact(file, displayName));
//	}
	
	Property<String> getDisplayName();
	
	default void displayName(final String type) {
		this.getDisplayName().set(type);
	}
}
