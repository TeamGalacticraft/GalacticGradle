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

import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamConstants;

public class ChangelogPlugin extends GradlePlugin
{

	public static final String GENERATE_CHANGELOG_TASK_NAME = "createChangelog";

	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
	{
	    ChangelogExtension ext = extensions.findOrCreate("changelog", ChangelogExtension.class, target);

		tasks.registerTask(ChangelogPlugin.GENERATE_CHANGELOG_TASK_NAME, GenerateChangelogTask.class, a ->
		{
			a.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
		});
		
		afterEval(project -> {
		    if (!ext.getAddedTypes().get().isEmpty())
		    {
		        for(String added : ext.getAddedTypes().get())
		        {
		            ext.getCommitTypes().add(added);
		        }
		    }
		});
	}

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{

	}
}
