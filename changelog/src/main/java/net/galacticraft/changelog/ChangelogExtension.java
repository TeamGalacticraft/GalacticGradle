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

package net.galacticraft.changelog;

import java.util.Arrays;

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

import lombok.Getter;

@Getter
public class ChangelogExtension
{

    private final Project project;

    private final Property<Boolean> debugMode;

    private final Property<String> tag;

    private Property<Boolean> ignoreScopes;

    private final ListProperty<String> addedTypes;

    private final ListProperty<String> commitTypes;
    
    private final RegularFileProperty changelogFile;

    @Inject
    public ChangelogExtension(final Project project)
    {
        final ObjectFactory factory = project.getObjects();
        this.project = project;
        this.debugMode = factory.property(Boolean.class).convention(false);
        this.tag = factory.property(String.class);
        this.ignoreScopes = factory.property(Boolean.class).convention(false);
        this.commitTypes = factory.listProperty(String.class)
            .convention(Arrays.asList("feat", "fix", "perf", "refactor", "revert", "docs", "style"));
        this.addedTypes = factory.listProperty(String.class).empty();
        this.changelogFile = factory.fileProperty().convention(project.getLayout().getBuildDirectory().file("changelog.md"));
    }
    
    public void outputFile(String path)
    {
        this.changelogFile.set(this.project.file(path));
    }

    public void debug()
    {
        this.debugMode.set(true);
    }

    public void ignoreScopes()
    {
        this.ignoreScopes.set(true);
    }

    public void types(final String... types)
    {
        this.commitTypes.empty().addAll(types);
    }

    public void addTypes(final String... types)
    {
        this.addedTypes.addAll(types);
    }

    public void fromTag(final String tag)
    {
        this.tag.set(tag);
    }
}
