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

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.NotNull;

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

		ModrinthUploadExtension extension = Extensions.findOrCreate(extensions, "modrinth", ModrinthUploadExtension.class, project, project.getObjects());

		tasks.register("publishToModrinth", ModrinthUploadTask.class, task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Publishes the mod artifact to the Modrinth Mod Platform");
			task.dependsOn(tasks.named("build"));
		});
		
		project.afterEvaluate(set -> {
			project.getTasks().withType(AbstractArchiveTask.class, task -> {
				extension.getUploadFile().set(task.getArchiveFile());
			});
			
			String version = extension.getVersionNumber().get();
	        for(String val : VersionType.CONSTANTS) {
	        	if(version.contains(val)) {
	        		extension.getVersionType().set(val);
	        	}
	        }
		});
	}
}
