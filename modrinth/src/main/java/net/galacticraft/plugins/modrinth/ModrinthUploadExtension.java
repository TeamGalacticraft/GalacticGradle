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

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

import net.galacticraft.plugins.modrinth.model.ConfigurationContainer;
import net.galacticraft.plugins.modrinth.model.DependenciesConfiguation;

public class ModrinthUploadExtension implements ConfigurationContainer {

	private final Property<String> token, projectId, versionNumber, versionType, changelog;
	private final Property<Boolean> debug;
	private final Property<Object> uploadFile;
	private final DependenciesConfiguation dependencies;

	@Inject
	public ModrinthUploadExtension(final ObjectFactory factory) {
		this.token =factory.property(String.class);
		this.projectId = factory.property(String.class);
		this.debug = factory.property(Boolean.class).convention(false);
        this.uploadFile = factory.property(Object.class);
        this.versionNumber = factory.property(String.class);
        this.versionType = factory.property(String.class);
        this.dependencies = factory.newInstance(DependenciesConfiguation.class, factory);
        this.changelog = factory.property(String.class);
	}

	@Override
	public Property<String> getProjectId() {
		return this.projectId;
	}
	
    public Property<String> getToken() {
        return this.token;
    }
	
    @Override
	public Property<Object> getMainFile() {
		return this.uploadFile;
	}

	@Override
	public Property<Boolean> getDebug() {
		return this.debug;
	}

	@Override
	public Property<String> getVersion() {
		return this.versionNumber;
	}

	public Property<String> getVersionType() {
		return this.versionType;
	}

	@Override
	public Property<String> getChangelog() {
		return this.changelog;
	}

	@Nested
    public DependenciesConfiguation getDependenciesContainer() {
        return this.dependencies;
    }

    public void dependencies(final Action<? super DependenciesConfiguation> action) {
        action.execute(this.dependencies);
    }
}
