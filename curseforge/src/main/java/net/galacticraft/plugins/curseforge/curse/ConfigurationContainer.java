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
	
	default void file(final Object file) {
		this.getMainFile().set(new FileArtifact(file, null));;
	}
	
	default void file(final Object file, final String displayName) {
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
