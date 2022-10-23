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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import org.checkerframework.checker.nullness.qual.NonNull;

import net.galacticraft.gradle.core.project.GalacticProject;
import net.galacticraft.gradle.core.util.Checks;
import net.galacticraft.gradle.core.util.IOHelper;
import net.galacticraft.gradle.core.version.Version;
import net.galacticraft.gradle.core.xml.metadata.Metadata;
import net.galacticraft.gradle.core.xml.model.Model;

public abstract class MavenModelBase
{
	protected final GalacticProject	project;
	private final List<URL>			repositoryUrlSet;

	MavenModelBase(GalacticProject project)
	{
		this.project = project;
		this.repositoryUrlSet = new ArrayList<>();
	}

	public void addRepositoryUrl(URI url)
	{
		URL repositoryUrl = IOHelper.toURL(url);
		this.repositoryUrlSet.add(repositoryUrl);
	}

	protected Optional<Metadata> getMetadata()
	{
		this.runChecks(repositoryUrlSet, "getMetadata()");

		if(repositoryUrlSet.size() > 1)
		{
			Optional<Metadata> metadata = null;
			for (URL repositoryUrl : this.repositoryUrlSet)
			{
				Optional<Metadata> i = Internal._getMetadata(repositoryUrl, project);
				if (i.isPresent() && metadata == null)
				{
					metadata = i;
				}
			}
			return metadata;
		}
		return Internal._getMetadata(repositoryUrlSet.get(0), project);
	}

	protected Optional<Metadata> getSnapshotMetadata(Version version)
	{
		this.runChecks(repositoryUrlSet, "getSnapshotMetadata()");

		if(repositoryUrlSet.size() > 1)
		{
			Optional<Metadata> metadata = null;
			for (URL repositoryUrl : this.repositoryUrlSet)
			{
				Optional<Metadata> i = Internal._getSnapshotMetadata(repositoryUrl, project, version);
				if (i.isPresent() && metadata == null)
				{
					metadata = i;
				}
			}
			return metadata;
		}
		return Internal._getSnapshotMetadata(repositoryUrlSet.get(0), project, version);
	}

	protected Optional<Model> getSnapshotPom(String artifactId, Version version)
	{
		this.runChecks(repositoryUrlSet, "getSnapshotPom()");
		if(repositoryUrlSet.size() > 1)
		{
			Optional<Model> metadata = null;
			for (URL repositoryUrl : this.repositoryUrlSet)
			{
				Optional<Model> i = Internal._getSnapshotPom(repositoryUrl, project, version);
				if (i.isPresent() && metadata == null)
				{
					metadata = i;
				}
			}
			return metadata;
		}
		return Internal._getSnapshotPom(repositoryUrlSet.get(0), project, version);
	}

	protected Optional<Model> getPom(String artifactId, Version version)
	{
		this.runChecks(repositoryUrlSet, "getPom()");
		if(repositoryUrlSet.size() > 1)
		{
			Optional<Model> pomModel = null;
			for (URL repositoryUrl : this.repositoryUrlSet)
			{
				Optional<Model> i = Internal._getPom(repositoryUrl, project, version);
				if (i.isPresent() && pomModel == null)
				{
					pomModel = i;
				}
			}
			return pomModel;
		}
		return Internal._getPom(repositoryUrlSet.get(0), project, version);
	}

	private <T extends @NonNull Collection<?>> void runChecks(@Nullable T collection, String method)
	{
		String errm = String.format("[%s] MavenArtifactRepository Set<URL> for " + project.getId() + "is Null and shouldn't be", method);
		String emtym = String.format("[%s] MavenArtifactRepository Set<URL> for " + project.getId() + "is empty", method);
		
		Checks.notNullOrEmpty(collection, errm, emtym);
	}

}
