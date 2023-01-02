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

package net.galacticraft.publish.modrinth;

import javax.inject.Inject;

import org.gradle.api.Project;

import com.modrinth.minotaur.ModrinthExtension;

public class ModrinthPublishExtension extends ModrinthExtension
{

    @Inject
    public ModrinthPublishExtension(final Project project)
    {
        super(project);
    }

    public void apiUrl(final String string)
    {
        getApiUrl().set(string);
    }

    public void token(final String string)
    {
        getToken().set(string);
    }
    
    public void projectId(final String id)
    {
        getProjectId().set(id);
    }

    public void versionNumber(final String number)
    {
        getVersionNumber().set(number);
    }
    
    public void versionName(final String name)
    {
        getVersionName().set(name);
    }

    public void changelog(final String changelog)
    {
        getChangelog().set(changelog);
    }
    
    public void artifact(final Object artifact)
    {
        getUploadFile().set(artifact);
    }

    public void extras(final Object... artifacts)
    {
        for(Object artifact : artifacts)
        {
            getAdditionalFiles().add(artifact);
        }
    }
    
    public void versionType(final String type)
    {
        getVersionType().set(type);
    }

    public void gameVersions(final String... versions)
    {
        for(String version : versions)
        {
            getGameVersions().add(version);
        }
    }
    
    public void loaders(final String... loaders)
    {
        for(String loader : loaders)
        {
            getLoaders().add(loader);
        }
    }

    public void silentFail()
    {
        getFailSilently().set(true);
    }
    
    public void doNotDetectLoaders()
    {
        getDetectLoaders().set(false);
    }
    
    public void debug()
    {
        getDebugMode().set(true);
    }
    
    public void syncFrom(final String from)
    {
        getSyncBodyFrom().set(from);
    }
}
