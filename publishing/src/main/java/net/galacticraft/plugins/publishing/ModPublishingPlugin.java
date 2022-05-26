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

import org.gradle.api.Task;

import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.common.plugins.property.BooleanProperty;
import net.galacticraft.plugins.publishing.curseforge.CurseExtension;
import net.galacticraft.plugins.publishing.curseforge.CursePlugin;
import net.galacticraft.plugins.publishing.modrinth.ModrinthPlugin;
import net.galacticraft.plugins.publishing.modrinth.ModrinthPublishExtension;

public class ModPublishingPlugin extends GradlePlugin {

	ModrinthPublishExtension modrinthExtension;
	CurseExtension curseforgeExtension;
	ModPublishingExtension extension;

	@Override
	public void plugin() {
		applyPlugin(CursePlugin.class);
		applyPlugin(ModrinthPlugin.class);

		modrinthExtension = extensions().find(ModrinthPublishExtension.class);
		curseforgeExtension = extensions().find(CurseExtension.class);
		extension = extensions().findOrCreate("modpublishing", ModPublishingExtension.class, project().getObjects(),
				modrinthExtension, curseforgeExtension);

		registerTask("publishToAll", task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Publishes the mod artifact All platforms");
			Task modrinthTask = tasks().named("publishToModrinth").get();
			Task curseForgeTaskTask =  tasks().named("publishToCurseforge").get();
			task.finalizedBy(modrinthTask, curseForgeTaskTask);
		});

		BooleanProperty debugProvider = new BooleanProperty(project(), "publishing.debug");

		project().getGradle().afterProject(set -> {
			if (debugProvider.isTrue()) {
				modrinthExtension.getModrinth().getToken().set("debug-token");
				curseforgeExtension.getApiKey().set("debug-key");
			}
		});

		project().afterEvaluate(set -> {
			modrinthExtension.getDebug().set(debugProvider.value());
			curseforgeExtension.getDebug().set(debugProvider.value());
		});
	}
}
