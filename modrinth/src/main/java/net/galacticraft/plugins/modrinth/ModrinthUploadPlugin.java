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

import java.io.File;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.galacticraft.plugins.modrinth.base.Extensions;
import net.galacticraft.plugins.modrinth.base.GradlePlugin;
import net.galacticraft.plugins.modrinth.model.type.VersionType;

public class ModrinthUploadPlugin implements GradlePlugin {
	private @MonotonicNonNull Project project;
		
	@Override
	public void apply(@NotNull Project project, @NotNull PluginContainer plugins, @NotNull ExtensionContainer extensions, @NotNull TaskContainer tasks) {
		this.project = project;
		
		if(!project.getPluginManager().hasPlugin("java")) {
			plugins.apply(JavaPlugin.class);
		}

		ModrinthUploadExtension extension = Extensions.findOrCreate(extensions, "modrinth", ModrinthUploadExtension.class, project.getObjects());

		tasks.register("publishToModrinth", ModrinthUploadTask.class, task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Publishes the mod artifact to the Modrinth Mod Platform");
			task.dependsOn(tasks.named("build"));
		});
		
		project.afterEvaluate(set -> {
			this.configureApiToken(extension);
			this.configureUploadFileIfNeeded(extension);
			this.configureVersionIfNeeded(extension);
			this.configureChangelogIfNeeded(extension);
		});
	}
	
	private void configureApiToken(ModrinthUploadExtension extension) {
		if(!extension.getToken().isPresent()) {
			if(System.getenv("MODRINTH_TOKEN") != null) {
				extension.getToken().set(System.getenv("MODRINTH_TOKEN"));
			} else if (project.findProperty("MODRINTH_TOKEN") != null) {
				extension.getToken().set((String) project.findProperty("MODRINTH_TOKEN"));
			} else {
				this.project.getLogger().lifecycle("[Modrinth] Could not set MODRINTH_TOKEN from Environment Variable or Project Property");
				throw new GradleException("[Modrinth] Could not set MODRINTH_TOKEN from Environment Variable or Project Property");
			}
		}
	}
	
	private void configureUploadFileIfNeeded(ModrinthUploadExtension extension) {
		if(!extension.getMainFile().isPresent()) {
			project.getTasks().withType(AbstractArchiveTask.class, task -> {
				if(task.getName().equals("jar")) {
					extension.getMainFile().set(task.getArchiveFile());
				}
			});
		}
	}
	
	private void configureVersionIfNeeded(ModrinthUploadExtension extension) {
		Provider<String> version = project.provider(() -> project.getVersion() == null ? null : String.valueOf(project.getVersion()));
		if(!extension.getVersionNumber().isPresent()) {
			extension.getVersionNumber().set(version);
		}
		
		if(!extension.getVersionType().isPresent()) {
	        for(String val : VersionType.CONSTANTS.keySet()) {
	        	if(version.get().contains(val)) {
	        		extension.getVersionType().set(val);
	        	}
	        }
		}
	}
	
	private void configureChangelogIfNeeded(ModrinthUploadExtension extension) {
        File dir = project.getProjectDir();
        String changelogContent =  null;
		if(extension.getChangelog().isPresent()) {
        	String changelogFile = extension.getChangelog().get();
        	File file = this.project.file(changelogFile);
        	if(file.exists()) {
        		changelogContent = this.readFromFile(file);
        	}
		} else {
            for(File file : dir.listFiles()) {
            	if (file.isFile()) {
            		String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
            		if(filename[0].equalsIgnoreCase("changelog")) {
            			changelogContent = this.readFromFile(file);
            		}
            	}
            }
		}
		if(changelogContent != null) {
			extension.getChangelog().set(changelogContent);
		} else {
			extension.getChangelog().set("No changelog provided");
		}
	}
	
	private String readFromFile(File file) {
		try {
			return Files.asCharSource(file, Charsets.UTF_8).read();
		} catch (Exception e) {}
		return null;
	}
}
