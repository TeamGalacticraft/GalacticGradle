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

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import lombok.Getter;

// TODO: Auto-generated Javadoc
/**
 * Gets the commit.
 *
 * @return the commit
 */
@Getter
public class ChangelogExtension
{

    private final Project project;
    
    private final Property<Boolean> debugMode;
    
    private final Property<Boolean> registerAllPublications;
    
    private final Property<String> tag;
    
    private final Property<String> commit;

    /**
     * Instantiates a new changelog extension.
     *
     * @param project the project
     */
    @Inject
    public ChangelogExtension(final Project project)
    {
        this.project = project;
        
        this.debugMode = project.getObjects().property(Boolean.class).convention(false);
        this.registerAllPublications = project.getObjects().property(Boolean.class).convention(false);
        
        this.tag = project.getObjects().property(String.class);
        this.commit = project.getObjects().property(String.class);
    }

    /**
     * Debug mode.
     */
    public void debugMode()
    {
        this.debugMode.set(true);;
    }
    
    /**
     * Register all publications.
     */
    public void registerAllPublications()
    {
        this.registerAllPublications.set(true);;
    }

    /**
     * In debug mode.
     *
     * @return true, if successful
     */
    public boolean inDebugMode()
    {
        return this.debugMode.get();
    }

    /**
     * From tag.
     *
     * @param tag the tag
     */
    public void fromTag(final String tag)
    {
        this.tag.set(tag);
    }
}
