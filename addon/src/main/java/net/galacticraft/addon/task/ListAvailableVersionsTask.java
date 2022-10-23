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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import net.galacticraft.addon.util.StringBuild;
import net.galacticraft.gradle.core.version.Version;
import net.galacticraft.gradle.core.project.GalacticProject;

public abstract class ListAvailableVersionsTask extends DefaultTask
{

	@Input
	public abstract Property<GalacticProject> getGalacticProject();

	@TaskAction
	public void run()
	{
		Logger log = this.getProject().getLogger();
		StringBuild string = StringBuild.start();
		GalacticProject project = getGalacticProject().get();
		List<Version> versions = project.getMavenModel().readVersions();

		if (!versions.isEmpty())
		{
			List<Version>	snapshots	= new ArrayList<>();
			List<Version>	releases	= new ArrayList<>();

			snapshots.addAll(versions.stream().filter(Version::isSnapshotVersion).collect(Collectors.toList()));
			releases.addAll(versions.stream().filter(Version::isStable).collect(Collectors.toList()));

			string.ln();
			string.appendln("Available Galacticraft-Legacy Versions");
			string.ln();

			if (!snapshots.isEmpty())
			{
				string.appendln("[SNAPSHOTS]");
				string.appendln("-----------");
				snapshots.forEach(sv -> {
					string.appendln("> " + sv.toString());
				});
				string.ln();
				string.appendln("lastestSnapshot() = " + project.getMavenModel().readLatestSnapshot().toString());
				string.ln();
			}

			if (!releases.isEmpty())
			{
				string.appendln("[RELEASES]");
				string.appendln("----------");
				releases.forEach(rv -> {
					string.appendln("> " + rv.toString());
				});
				string.ln();
				string.appendln("latestRelease() = " + project.getMavenModel().readLatestRelease().toString());
				string.ln();
			}

			if (snapshots.isEmpty() && releases.isEmpty())
			{
				string.appendln("No versions found!");
			}
			
			
			
			log.lifecycle(string.toString());
		}
	}
}
