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

package net.galacticraft.plugins.curseforge;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

import net.galacticraft.plugins.curseforge.curse.ConfigurationContainer;
import net.galacticraft.plugins.curseforge.curse.FileArtifact;

public class CurseUploadExtension implements ConfigurationContainer {

	private final Property<String> apiKey, projectId, changelog, changelogType, releaseType, displayName;
	private final Property<Boolean> debug;
	private final ListProperty<Object> gameVersions;
	//private final ListProperty<FileArtifact> extraFiles;
	private final Property<FileArtifact> mainFile;
	private final RelationsConfiguation relations;
	
	@Inject
	public CurseUploadExtension(final ObjectFactory factory) {
		this.apiKey = factory.property(String.class);
		this.projectId = factory.property(String.class);
		this.mainFile = factory.property(FileArtifact.class);
		this.debug = factory.property(Boolean.class).convention(false);
        this.changelogType = factory.property(String.class);
        this.changelog = factory.property(String.class);
        this.releaseType = factory.property(String.class);
        this.displayName = factory.property(String.class);
        this.relations = factory.newInstance(RelationsConfiguation.class, factory);
        this.gameVersions = factory.listProperty(Object.class).empty();
        //this.extraFiles = factory.listProperty(FileArtifact.class).empty();
	}
	
	public Property<Boolean> getDebug() {
		return this.debug;
	}
	
	public void debug() {
		this.debug.set(true);
	}

	@Override
	public Property<String> getProjectId() {
		return this.projectId;
	}

	public Property<String> getApiKey() {
		return this.apiKey;
	}
	
	@Override
	public Property<String> getReleaseType() {
		return this.releaseType;
	}

	@Override
	public Property<String> getChangelog() {
		return this.changelog;
	}

	@Override
	public Property<String> getChangelogType() {
		return this.changelogType;
	}
	
	@Override
	public ListProperty<Object> getGameVersions() {
		return this.gameVersions;
	}
	
	@Override
	public Property<String> getDisplayName() {
		return this.displayName;
	}
	
	@Override
	public Property<FileArtifact> getMainFile() {
		return this.mainFile;
	}

//	@Override
//	public ListProperty<FileArtifact> getExtraFiles() {
//		return this.extraFiles;
//	}
	
	@Nested
    public RelationsConfiguation getRelationsContainer() {
        return this.relations;
    }

    public void dependencies(final Action<? super RelationsConfiguation> action) {
        action.execute(this.getRelationsContainer());
    }
    
    
}
