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

package net.galacticraft.plugins.modrinth.model;

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

	Property<Object> getMainFile();
	
	default void file(Object file) {
		this.getMainFile().set(file);
	}
	
//	ListProperty<Object> getAdditionalFiles();
//	
//	default void addFile(Object file) {
//		this.getAdditionalFiles().add(file);
//	}
//	
//	default void addFiles(Object... files) {
//		this.getAdditionalFiles().addAll(files);
//	}

	Property<Boolean> getDebug();

	default void debug() {
		this.getDebug().set(true);
	}
	
	Property<String> getVersionNumber();
	
	default void version(final String version) {
		this.getVersionNumber().set(version);
	}
	
	Property<String> getVersionName();
	
	default void versionName(final String versionName) {
		this.getVersionName().set(versionName);
	}
	
	Property<String> getVersionType();
	
	default void versionType(final String version) {
		this.getVersionType().set(version);
	}

	Property<String> getChangelog();
	
	default void changelog(final String changelog) {
		this.getChangelog().set(changelog);
	}
	
	ListProperty<String> getGameVersions();
	
	default void gameVersions(final String... gameVersions) {
		this.getGameVersions().addAll(gameVersions);
	}

	ListProperty<String> getLoaders();
	
	default void loaders(final String... loaders) {
		this.getLoaders().addAll(loaders);
	}
}
