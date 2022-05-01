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

package net.galacticraft.plugins.publishing;

import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.common.plugins.Extensions;
import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.plugins.curseforge.CurseUploadExtension;
import net.galacticraft.plugins.curseforge.CurseUploadPlugin;
import net.galacticraft.plugins.modrinth.ModrinthUploadExtension;
import net.galacticraft.plugins.modrinth.ModrinthUploadPlugin;

public class ModPublihingPlugin implements GradlePlugin {

	@Override
	public void apply(final @NotNull Project project, final @NotNull PluginContainer plugins,
			final @NotNull ExtensionContainer extensions, final @NotNull TaskContainer tasks) {
		project.getPlugins().apply(CurseUploadPlugin.class);
		project.getPlugins().apply(ModrinthUploadPlugin.class);

		ModrinthUploadExtension modrinthExtension = extensions.findByType(ModrinthUploadExtension.class);
		CurseUploadExtension curseExtension = extensions.findByType(CurseUploadExtension.class);

		ModPublishingExtension extension = Extensions.findOrCreate(extensions, "modpublishing",
				ModPublishingExtension.class, project.getObjects(), modrinthExtension, curseExtension);

		tasks.create("publishToAll", task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Publishes the mod artifact All platforms");
			Task modrinthTask = tasks.named("publishToModrinth").get();
			Task curseForgeTaskTask = tasks.named("publishToCurseforge").get();
			task.finalizedBy(modrinthTask, curseForgeTaskTask);
		});

		project.afterEvaluate(set -> {
			Boolean debugMode = extension.getDebug().get();
			modrinthExtension.getDebug().set(debugMode);
			curseExtension.getDebug().set(debugMode);
		});
	}
}
