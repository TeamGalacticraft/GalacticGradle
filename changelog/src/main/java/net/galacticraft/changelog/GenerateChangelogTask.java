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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;

import net.galacticraft.changelog.util.ChangelogUtils;
import net.galacticraft.changelog.util.Utils;

public abstract class GenerateChangelogTask extends DefaultTask
{

    public GenerateChangelogTask()
    {
        super();

        //Setup defaults: Using merge-base based text changelog generation of the local project into build/changelog.txt
        getGitDirectory().convention(getProject().getLayout().getProjectDirectory().dir(".git"));
        getBuildMarkdown().convention(true);
        getOutputFile().convention(getProject().getLayout().getBuildDirectory().file("changelog.md"));
        getProjectUrl().convention(Utils.buildProjectUrl(getProject()));
    }

    @InputDirectory
    @PathSensitive(PathSensitivity.NONE)
    public abstract DirectoryProperty getGitDirectory();

    @Input
    public abstract Property<Boolean> getBuildMarkdown();

    @OutputFile
    public abstract RegularFileProperty getOutputFile();

    @Input
    public abstract Property<String> getProjectUrl();

    @TaskAction
    public void generate() {        
        final String tag = this.getProject().getExtensions().getByType(ChangelogExtension.class).getTag().get();
        
        if(tag.isEmpty()) {
            throw new IllegalStateException("tag is not supplied to the task: " + getName());
        }

        String changelog = "";
        changelog = ChangelogUtils.generateChangelog(getProject(), getProjectUrl().get(), !getBuildMarkdown().get(), tag);

        final File outputFile = getOutputFile().getAsFile().get();
        outputFile.getParentFile().mkdirs();
        if (outputFile.exists()) {
            outputFile.delete();
        }

        try
        {
            Files.write(outputFile.toPath(), changelog.getBytes(StandardCharsets.UTF_8));
        }
        catch (IOException e)
        {
            throw new IllegalStateException("Failed to write changelog to file: " + outputFile.getAbsolutePath());
        }
    }
}
