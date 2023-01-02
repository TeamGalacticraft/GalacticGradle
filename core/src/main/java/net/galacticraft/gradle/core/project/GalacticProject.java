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

package net.galacticraft.gradle.core.project;

import net.galacticraft.gradle.core.model.maven.MavenModel;
import net.galacticraft.gradle.core.util.Checks;

public class GalacticProject extends MavenModel
{
	private String		group		= "dev.galacticraft";
	private String		groupPath	= "dev/galacticraft/";
	private String		artifactId;

	public static GalacticProject create(String artifactId)
	{
		return new GalacticProject(artifactId);
	}

	private GalacticProject(String artifactId)
	{
		this.artifactId = artifactId;
		setProject(this);
	}

	public String toDependencyNotation()
	{
		Checks.notNull(getVersion(), "No Version has been set for GalacticProject with id: " + artifactId);
		return toDependencyNotation(getVersion().toString());
	}

	public String toDependencyNotation(String version)
	{
		this.setVersion(version);
		return this.group + ":" + this.artifactId + ":" + version;
	}

	public String getId()
	{
		return this.artifactId;
	}

	public String toPath()
	{
		return groupPath + this.artifactId + "/";
	}

	public String toPath(String version)
	{
		return toPath() + version;
	}
}
