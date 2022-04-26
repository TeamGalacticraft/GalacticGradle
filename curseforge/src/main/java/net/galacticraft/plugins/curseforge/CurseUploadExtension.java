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

import java.io.File;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Nested;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.galacticraft.plugins.curseforge.curse.ConfigurationContainer;
import net.galacticraft.plugins.curseforge.curse.RelationContainer;
import net.galacticraft.plugins.curseforge.curse.RelationType;
import net.galacticraft.plugins.curseforge.curse.ReleaseType;

public class CurseUploadExtension implements ConfigurationContainer {

	private final Property<String> apiKey, projectId, changelog, changelogType, releaseType;
	private final Property<Boolean> debug;
	private final ListProperty<Object> gameVersions;
	private final RegularFileProperty uploadFile;
	private final NamedDomainObjectContainer<RelationContainer> relations;
	
	@Inject
	public CurseUploadExtension(final Project project, final ObjectFactory factory) {
		this.apiKey = factory.property(String.class).convention((String) project.findProperty("CURSE_TOKEN"));
		this.projectId = factory.property(String.class);
		this.uploadFile = factory.fileProperty();
		this.debug = factory.property(Boolean.class).convention(false);
        File dir = project.getProjectDir();
        String changelogContent = "";
        String changelogExt = "";
        try {
            for(File file : dir.listFiles()) {
            	if (file.isFile()) {
            		String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
            		if(filename[0].equalsIgnoreCase("changelog")) {
            			if(filename[1].equalsIgnoreCase("md"))
            				changelogExt ="markdown";
            			else if(filename[1].equalsIgnoreCase("html"))
            				changelogExt = "html";
            			else
            				changelogExt = "text";
            			changelogContent = Files.asCharSource(file, Charsets.UTF_8).read();
            		}
            	}
            }
        } catch (Exception e) {}
        this.changelogType = factory.property(String.class).convention(changelogExt);
        this.changelog = factory.property(String.class).convention(changelogContent);
        this.releaseType = factory.property(String.class).convention(ReleaseType.RELEASE.value());
        this.relations = factory.domainObjectContainer(RelationContainer.class);
        this.gameVersions = factory.listProperty(Object.class).empty();
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
	
	public RegularFileProperty getUploadFile() {
		return this.uploadFile;
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
	
	@Nested
    public NamedDomainObjectContainer<RelationContainer> getRelations() {
        return this.relations;
    }

    public void dependencies(final Action<? super NamedDomainObjectContainer<RelationContainer>> action) {
        action.execute(this.relations);
    }

    public void dependency(final String name, final Action<? super RelationContainer> action) {
        this.relations.register(name, action);
    }
	
    public void requiredDependency(String slugIn) {
        this.dependency(slugIn, set -> {
        	set.setType(RelationType.REQUIRED);
        });
    }
    
    public void embeddedLibrary(String slugIn) {
        this.dependency(slugIn, set -> {
        	set.setType(RelationType.EMBEDEDLIB);
        });
    }

    public void optionalDependency(String slugIn) {
        this.dependency(slugIn, set -> {
        	set.setType(RelationType.OPTIONAL);
        });
    }
    
    public void tool(String slugIn) {
        this.dependency(slugIn, set -> {
        	set.setType(RelationType.TOOL);
        });
    }
    
    public void incompatible(String slugIn) {
        this.dependency(slugIn, set -> {
        	set.setType(RelationType.INCOMPATIBLE);
        });
    }
}
