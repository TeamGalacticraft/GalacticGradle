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

package net.galacticraft.addon.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import net.galacticraft.addon.util.StringBuild;
import net.galacticraft.gradle.core.project.GalacticProject;
import net.galacticraft.gradle.core.version.list.VersionSet;

public abstract class ListAvailableVersionsTask extends DefaultTask
{

	@Input
	public abstract Property<GalacticProject> getGalacticProject();

	@TaskAction
	public void run()
	{
		Logger			log			= this.getProject().getLogger();
		StringBuild		string		= StringBuild.start();
		GalacticProject	project		= getGalacticProject().get();
		VersionSet		versions	= project.readVersions();

		if (versions.isEmpty())
		{
			string.appendln("No versions found!");
			log.lifecycle(string.toString());
		} else
		{
			string.ln();
			string.appendln("Available Galacticraft-Legacy Versions");
			string.ln();

			if (!versions.extractSnapshots().isEmpty())
			{
				string.appendln("[SNAPSHOTS]");
				string.appendln("-----------");
				versions.extractSnapshots().forEach(sv ->
				{
					string.appendln("> " + sv.toString());
				});
				string.ln();
				string.appendln("lastestSnapshot() = " + project.readLatestSnapshot().toString());
				string.ln();
			}

			if (!versions.extractReleases().isEmpty())
			{
				string.appendln("[RELEASES]");
				string.appendln("----------");
				versions.extractReleases().forEach(rv ->
				{
					string.appendln("> " + rv.toString());
				});
				string.ln();
				string.appendln("latestRelease() = " + project.readLatestRelease().toString());
				string.ln();
			}

			log.lifecycle(string.toString());
		}
	}
}
