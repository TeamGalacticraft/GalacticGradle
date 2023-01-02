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

import java.io.File;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.darkhax.curseforgegradle.TaskPublishCurseForge;
import net.darkhax.curseforgegradle.UploadArtifact;
import net.galacticraft.curseforge.internal.NamedRelation;
import net.galacticraft.curseforge.model.ChangelogType;
import net.galacticraft.curseforge.model.ReleaseType;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;

public class CursePublishPlugin extends GradlePlugin
{
    private BooleanProperty debug;

    @Override
    protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
    {
        debug = new BooleanProperty(target, "galactic.curseforge.debug", false);
        conditionalLog.setConditional(debug);
        CursePublishExtension extension = extensions.findOrCreate("curseforge", CursePublishExtension.class);

        TaskProvider<TaskPublishCurseForge> curseTask = tasks.registerTask("publishToCurseforge", TaskPublishCurseForge.class, task -> {
            task.setGroup("mod-publishing");
            task.setDescription("Publishes mod artifact to the CurseForge Platform");
            task.dependsOn(tasks.container().named("assemble"));
            task.mustRunAfter(tasks.container().named("build"));
        });

        target.afterEvaluate(project -> {
            this.configureApiToken(project, extension);
            this.configureUploadFileIfNeeded(extension);
            this.configureVersionIfNeeded(project, extension);
            this.configureChangelogIfNeeded(project, extension);
            
            curseTask.configure(task -> {
                task.apiToken = extension.getApiKey().get();
                
                UploadArtifact main = task.upload(extension.getProjectId().get(), extension.getMainFile().get());
                main.releaseType = extension.getReleaseType().getOrNull();
                main.changelog = extension.getChangelog().getOrNull();
                main.changelogType = extension.getChangelogType().getOrNull();
                for(NamedRelation r : extension.getRelations().getList().get())
                {
                    main.addRelation(r.getName(), r.getRelationType().toString());
                }
                
                for(Object xtra : extension.getExtraFiles().get())
                {
                    main.withAdditionalFile(xtra);
                }
            });
        });
    }

    @Override
    protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
    {

    }

    private void configureApiToken(Project project, CursePublishExtension extension)
    {
        if (!extension.getApiKey().isPresent()) {

            if (variables.has("CURSEFORGE_API_KEY"))
                extension.getApiKey().set(variables.get("CURSEFORGE_API_KEY"));

            else if (variables.has("CURSEFORGE_API_TOKEN"))
                extension.getApiKey().set(variables.get("CURSEFORGE_API_TOKEN"));

            else if (variables.has("CURSEFORGE_KEY"))
                extension.getApiKey().set(variables.get("CURSEFORGE_KEY"));

            else if (variables.has("CURSEFORGE_TOKEN"))
                extension.getApiKey().set(variables.get("CURSEFORGE_TOKEN"));

            else if (variables.has("CURSE_API_KEY"))
                extension.getApiKey().set(variables.get("CURSE_API_KEY"));

            else if (variables.has("CURSE_API_TOKEN"))
                extension.getApiKey().set(variables.get("CURSE_API_TOKEN"));

            else if (variables.has("CURSE_KEY"))
                extension.getApiKey().set(variables.get("CURSE_KEY"));

            else if (variables.has("CURSE_TOKEN"))
                extension.getApiKey().set(variables.get("CURSE_TOKEN"));

            else {
                lifecycle(
                    "[CurseForge] Could not set CURSE_TOKEN from Environment Variable, System Property or Project Property");
                throw new GradleException(
                    "[CurseForge] Could not set CURSE_TOKEN from Environment Variable, System Property or Project Property");
            }
        }
    }

    private void configureUploadFileIfNeeded(CursePublishExtension extension)
    {
        if (!extension.getMainFile().isPresent()) {
            tasks.container().withType(AbstractArchiveTask.class, task -> {
                if (task.getName().equals("jar")) {
                    conditionalLog.lifecycle("setting 'mainFile as archiveFile from Jar task");
                    extension.getMainFile().set(task.getArchiveFile());
                }
            });
        }
    }

    private void configureVersionIfNeeded(Project project, CursePublishExtension extension)
    {
        if (!extension.getReleaseType().isPresent()) {
            Provider<String> version =
                project.provider(() -> project.getVersion() == null ? null : String.valueOf(project.getVersion()));

            String type = ReleaseType.parse(version.get()).toString();
            conditionalLog.lifecycle("setting 'releaseType' as " + type);
            extension.getReleaseType().set(type);
        }
    }

    private void configureChangelogIfNeeded(Project project, CursePublishExtension extension)
    {
        File dir = project.getProjectDir();
        String changelogContent = "No Changelog Provided";
        String changelogExt = ChangelogType.TEXT.toString();
        if (extension.getChangelog().isPresent()) {
            String changelogFile = extension.getChangelog().get();
            File file = project.file(changelogFile);
            if (file.exists()) {
                conditionalLog.lifecycle("Changelog file was found, parsing");
                changelogContent = this.readFromFile(file);
                String fileExt = Files.getFileExtension(file.getName());
                changelogExt = ChangelogType.fromValue(fileExt).toString();
            } else {
                conditionalLog.lifecycle("Changelog was set as String. Using as TEXT");
                changelogContent = extension.getChangelog().get();
                changelogExt = ChangelogType.TEXT.toString();
            }
        } else {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
                    if (filename[0].equalsIgnoreCase("changelog")) {
                        changelogContent = this.readFromFile(file);
                        String fileExt = Files.getFileExtension(file.getName());
                        changelogExt = ChangelogType.fromValue(fileExt).toString();
                    }
                }
            }
        }
        conditionalLog.lifecycle("setting 'changelog' as " + changelogContent);
        extension.getChangelog().set(changelogContent);
        conditionalLog.lifecycle("setting 'changelogType' as " + changelogExt);
        extension.getChangelogType().set(changelogExt);
    }

    private String readFromFile(File file)
    {
        try {
            return Files.asCharSource(file, Charsets.UTF_8).read();
        } catch (Exception e) {
        }
        return null;
    }
}
