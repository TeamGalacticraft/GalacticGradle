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

package net.galacticraft.gradle.core.model.maven;

import java.net.URI;
import java.net.URL;
import java.util.Optional;

import javax.annotation.Nullable;

import net.galacticraft.gradle.core.plugin.GradlePlugin.ConditionalLog;
import net.galacticraft.gradle.core.project.GalacticProject;
import net.galacticraft.gradle.core.util.Checks;
import net.galacticraft.gradle.core.util.IOHelper;
import net.galacticraft.gradle.core.version.Version;
import net.galacticraft.gradle.core.xml.metadata.Metadata;
import net.galacticraft.gradle.core.xml.model.Model;

public abstract class MavenModelBase
{
	protected GalacticProject	project;
	private URL					repositoryUrl;

	public void setProject(GalacticProject project)
	{
		this.project = project;
	}

	public void setLogger(ConditionalLog logger)
	{
		Internal.logger = logger;
	}

	public void setRepositoryUrl(URI url)
	{
		URL repositoryUrl = IOHelper.toURL(url);
		this.repositoryUrl = repositoryUrl;
	}

	protected Optional<Metadata> getMetadata()
	{
		this.runChecks(repositoryUrl, "getMetadata()");
		return Internal._getMetadata(repositoryUrl, project);
	}

	protected Optional<Metadata> getSnapshotMetadata(Version version)
	{
		this.runChecks(repositoryUrl, "getSnapshotMetadata()");
		return Internal._getSnapshotMetadata(repositoryUrl, project, version);
	}

	protected Optional<Model> getSnapshotPom(String artifactId, Version version)
	{
		this.runChecks(repositoryUrl, "getSnapshotPom()");
		return Internal._getSnapshotPom(repositoryUrl, project, version);
	}

	protected Optional<Model> getPom(String artifactId, Version version)
	{
		this.runChecks(repositoryUrl, "getPom()");
		return Internal._getPom(repositoryUrl, project, version);
	}

	private <T extends Object> void runChecks(@Nullable T object, String method)
	{
		Checks.notNull(project, "GalacticraftProject is not set and returning null. Did you use 'setProject(GalacticProject project)'?");
		String errm = String.format("[%s] MavenArtifactRepository Set<URL> for " + project.getId() + "is Null and shouldn't be", method);
		Checks.notNull(object, errm);
	}

}
