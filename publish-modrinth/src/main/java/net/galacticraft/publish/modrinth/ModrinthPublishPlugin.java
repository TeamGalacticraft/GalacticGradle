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

import java.io.File;
import java.util.Optional;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.modrinth.minotaur.TaskModrinthUpload;
import com.modrinth.minotaur.request.VersionType;

import net.galacticraft.gradle.core.plugin.GradlePlugin;

public class ModrinthPublishPlugin extends GradlePlugin
{

    @Override
    protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
    {
        extensions.findOrCreate("modrinth", ModrinthPublishExtension.class, target);
        
        this.configure();
        this.createTasks();
        this.applyModrinth();
    }

    @Override
    protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
    {
    }

    private void configure()
    {
        final ModrinthPublishExtension extension = extensions.find(ModrinthPublishExtension.class);
        this.configureApiToken(extension);
        this.configureUploadFileIfNeeded(extension);
        this.configureVersionIfNeeded(extension);
        this.configureChangelogIfNeeded(extension);
    }
    
    private void createTasks()
    {
        tasks.registerTask("publishToModrinth", TaskModrinthUpload.class, task ->
        {
            task.setGroup("mod-publishing");
            task.setDescription("Publishes the mod artifact to the Modrinth Mod Platform");
            task.dependsOn(tasks.container().named("assemble"));
            task.mustRunAfter(tasks.container().named("build"));
        });
    }

    private void applyModrinth()
    {
        ModrinthPublishExtension extension = extensions.find(ModrinthPublishExtension.class);
        afterEval(evaluatedProject ->
        {
            Task task = evaluatedProject.getTasks().getByName("publishToModrinth");
            if (extension.getUploadFile().getOrNull() != null)
            {
                Object uploadFile = extension.getUploadFile().get();
                if (uploadFile instanceof AbstractArchiveTask)
                {
                    task.dependsOn(uploadFile);
                } else if (uploadFile instanceof TaskProvider<?> && ((TaskProvider<?>) uploadFile).get() instanceof AbstractArchiveTask)
                {
                    task.dependsOn(((TaskProvider<?>) uploadFile).get());
                }
            }
            for (Object file : extension.getAdditionalFiles().get())
            {
                if (file instanceof AbstractArchiveTask)
                {
                    task.dependsOn(file);
                } else if (file instanceof TaskProvider<?> && ((TaskProvider<?>) file).get() instanceof AbstractArchiveTask)
                {
                    task.dependsOn(((TaskProvider<?>) file).get());
                }
            }
        });
    }

    private void configureApiToken(ModrinthPublishExtension extension)
    {
        afterEval(p -> {
            if (!extension.getToken().isPresent())
            {
                Optional<String> MODRINTH_TOKEN = Optional.ofNullable(variables.get("MODRINTH_TOKEN"));
                if (MODRINTH_TOKEN.isPresent())
                    extension.getToken().set(MODRINTH_TOKEN.get());
                else
                {
                    lifecycle("[Modrinth] Could not set MODRINTH_TOKEN from Environment Variable, System Property or Project Property");
                    throw new GradleException("[Modrinth] Could not set MODRINTH_TOKEN from Environment Variable, System Property or Project Property");
                }
            }
        });
    }

    private void configureUploadFileIfNeeded(ModrinthPublishExtension extension)
    {
        afterEval(p -> {
            if (!extension.getUploadFile().isPresent())
            {
                tasks.container().withType(AbstractArchiveTask.class, task ->
                {
                    if (task.getName().equals("jar"))
                    {
                        extension.getUploadFile().set(task.getArchiveFile());
                    }
                });
            }
        });
    }

    private void configureVersionIfNeeded(ModrinthPublishExtension extension)
    {
        afterEval(p -> {
            Provider<String> version = getTarget().provider(() -> p.getVersion() == null ? null : String.valueOf(p.getVersion()));
            if (!extension.getVersionNumber().isPresent())
            {
                extension.getVersionNumber().set(version);
            }

            if (!extension.getVersionType().isPresent())
            {
                for (VersionType val : VersionType.values())
                {
                    String s = val.name().toLowerCase();
                    if (version.get().contains(s))
                    {
                        extension.getVersionType().set(s);
                    }
                }
            }
        });
    }

    private void configureChangelogIfNeeded(ModrinthPublishExtension extension)
    {
        afterEval(p -> {
            File   dir              = getTarget().getProjectDir();
            String changelogContent = null;
            if (extension.getChangelog().isPresent())
            {
                String changelogFile = extension.getChangelog().get();
                File   file          = getTarget().file(changelogFile);
                if (file.exists())
                {
                    changelogContent = this.readFromFile(file);
                } else
                {
                    if (!determineIfChangelogPropertyIsAFileName(changelogFile))
                    {
                        changelogContent = extension.getChangelog().get();
                    }
                }
            } else
            {
                for (File file : dir.listFiles())
                {
                    if (file.isFile())
                    {
                        String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
                        if (filename[0].equalsIgnoreCase("changelog"))
                        {
                            changelogContent = this.readFromFile(file);
                        }
                    }
                }
            }
            if (changelogContent != null)
            {
                extension.getChangelog().set(changelogContent);
            } else
            {
                extension.getChangelog().set("No changelog provided");
            }
        });   
    }

    private boolean determineIfChangelogPropertyIsAFileName(String changelog)
    {
        String[] changelogDeclaration = changelog.split("\\.");
        if (changelogDeclaration.length >= 2)
        {
            String afterPeriod = changelogDeclaration[1];
            if (afterPeriod.startsWith("  ") || afterPeriod.startsWith(" "))
            {
                return false;
            } else
            {
                return true;
            }
        } else
        {
            return false;
        }
    }

    private String readFromFile(File file)
    {
        try
        {
            return Files.asCharSource(file, Charsets.UTF_8).read();
        } catch (Exception e)
        {
        }
        return null;
    }
}
