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

package net.galacticraft.plugins.modrinth;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Nested;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.galacticraft.plugins.modrinth.config.ConfigurationContainer;
import net.galacticraft.plugins.modrinth.model.DependencyContainer;
import net.galacticraft.plugins.modrinth.model.type.VersionType;

public class ModrinthUploadExtension implements ConfigurationContainer {

	private final Property<String> projectId, versionNumber, versionType, changelog;
	private final Property<Boolean> debug;
	private final ListProperty<String> gameVersions, loaders;
	
	private final NamedDomainObjectContainer<DependencyContainer> dependencies;

	@Inject
	public ModrinthUploadExtension(final Project project, final ObjectFactory factory) {
		this.projectId = factory.property(String.class);
		this.debug = factory.property(Boolean.class).convention(false);
        this.gameVersions = factory.listProperty(String.class).empty();
        this.loaders = factory.listProperty(String.class).empty();
        
        Provider<String> version = project.provider(() -> project.getVersion() == null ? null : String.valueOf(project.getVersion()));
        this.versionNumber = factory.property(String.class).convention(version);
        String type = VersionType.RELEASE.value();
        this.versionType = factory.property(String.class).convention(type);
        
        this.dependencies = factory.domainObjectContainer(DependencyContainer.class);
        
        File changelogFile = new File(project.getProjectDir(), "changelog.md");
        String changelogContent = "";
        if(changelogFile.exists()) {
        	try {
        		changelogContent = Files.asCharSource(changelogFile, Charsets.UTF_8).read();
        	} catch (IOException e) {}
        }
        this.changelog = factory.property(String.class).convention(changelogContent);
	}

	@Override
	public Property<String> getProjectId() {
		return this.projectId;
	}

	@Override
	public Property<Boolean> getDebug() {
		return this.debug;
	}

	@Override
	public Property<String> getVersionNumnber() {
		return this.versionNumber;
	}

	@Override
	public Property<String> getVersionType() {
		return this.versionType;
	}

	@Override
	public Property<String> getChangelog() {
		return this.changelog;
	}

	@Override
	public ListProperty<String> getGameVersions() {
		return this.gameVersions;
	}

	@Override
	public ListProperty<String> getLoaders() {
		return this.loaders;
	}
	
	@Nested
    public NamedDomainObjectContainer<DependencyContainer> getDependencies() {
        return this.dependencies;
    }

    public void dependencies(final Action<? super NamedDomainObjectContainer<DependencyContainer>> action) {
        action.execute(this.dependencies);
    }

    public void dependency(final String name, final Action<? super DependencyContainer> action) {
        this.dependencies.register(name, action);
    }
}
