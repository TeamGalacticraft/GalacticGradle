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

package net.galacticraft.curseforge;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

import lombok.Getter;
import net.galacticraft.curseforge.internal.RelationsConfiguration;

@Getter
public class CursePublishExtension
{

    private final Property<String> apiKey, projectId, changelog, changelogType, releaseType, displayName;

    private final RelationsConfiguration relations;


    private final ListProperty<Object> gameVersions;

    private final ListProperty<Object> extraFiles;

    private final Property<Object> mainFile;

    @Inject
    public CursePublishExtension(final ObjectFactory factory)
    {
        this.apiKey = factory.property(String.class);
        this.projectId = factory.property(String.class);
        this.mainFile = factory.property(Object.class);
        this.changelogType = factory.property(String.class);
        this.changelog = factory.property(String.class);
        this.releaseType = factory.property(String.class);
        this.displayName = factory.property(String.class);
        this.gameVersions = factory.listProperty(Object.class).empty();
        this.extraFiles = factory.listProperty(Object.class).empty();
        this.relations = factory.newInstance(RelationsConfiguration.class);
    }

    public RelationsConfiguration getRelations()
    {
        return this.relations;
    }
    
    public void relations(final Action<? super RelationsConfiguration> action) {
        action.execute(this.relations);
    }

    public void project(String id)
    {
        this.projectId.set(id);
    }

    public void mainFile(Object file)
    {
        this.mainFile.set(file);
    }

    public void extraFiles(Object... files)
    {
        this.extraFiles.addAll(files);
    }
}
