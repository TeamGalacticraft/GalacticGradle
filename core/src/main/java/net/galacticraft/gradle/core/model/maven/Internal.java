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

import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

import net.galacticraft.gradle.core.project.GalacticProject;
import net.galacticraft.gradle.core.util.IOHelper;
import net.galacticraft.gradle.core.util.IOWrapper;
import net.galacticraft.gradle.core.util.StringUtil;
import net.galacticraft.gradle.core.version.Version;
import net.galacticraft.gradle.core.xml.MetadataReader;
import net.galacticraft.gradle.core.xml.ModelReader;
import net.galacticraft.gradle.core.xml.metadata.Metadata;
import net.galacticraft.gradle.core.xml.metadata.SnapshotVersion;
import net.galacticraft.gradle.core.xml.model.Model;

public final class Internal
{
	private static String	pomFile			= "%s-%s.pom";
	private static String	metadataFile	= "maven-metadata.xml";
	static Logger			logger = Logging.getLogger(Internal.class.getSimpleName());

	static Optional<Metadata> _getMetadata(URL repositoryUrl, GalacticProject project)
	{
		Metadata	metadata	= null;
		IOWrapper	wrapper		= IOHelper.getIOWrapper(project, repositoryUrl, metadataFile, null);

		logger.debug("Retreiving Metadata from: " + wrapper.getUrl().toString());

		try
		{
			if (wrapper.getInputStream() != null)
				metadata = new MetadataReader().read(wrapper.getInputStream());
		} catch (Exception e)
		{
			throw new GradleException("Failed to parse METADATA from: " + repositoryUrl, e);
		}
		return Optional.ofNullable(metadata);
	}

	static Optional<Model> _getPom(URL repositoryUrl, GalacticProject project, Version version)
	{
		String		filename	= String.format(pomFile, project.getId(), version.toString());
		Model		pomModel	= null;
		IOWrapper	wrapper		= IOHelper.getIOWrapper(project, repositoryUrl, filename, version.toString());

		logger.debug("Retreiving POM from: " + wrapper.getUrl().toString());

		try
		{
			if (wrapper.getInputStream() != null)
				pomModel = new ModelReader().read(wrapper.getInputStream());
		} catch (Exception e)
		{
			throw new GradleException("Failed to parse POM from: " + StringUtil.asPath(repositoryUrl, version, filename), e);
		}
		return Optional.ofNullable(pomModel);
	}

	static Optional<Metadata> _getSnapshotMetadata(URL repositoryUrl, GalacticProject project, Version version)
	{
		Metadata	metadata	= null;
		String		filename	= version.toString() + "/" + metadataFile;
		IOWrapper	wrapper		= IOHelper.getIOWrapper(project, repositoryUrl, filename, null);

		logger.debug("Retreiving Snapshot Metadata from: " + wrapper.getUrl().toString());

		try
		{
			if (wrapper.getInputStream() != null)
				metadata = new MetadataReader().read(wrapper.getInputStream());
		} catch (Exception e)
		{
			throw new GradleException("Failed to parse METADATA from: " + repositoryUrl + filename, e);
		}
		return Optional.ofNullable(metadata);
	}

	static Optional<Model> _getSnapshotPom(URL repositoryUrl, GalacticProject project, Version version)
	{
		String	filename	= null;
		Model	pomModel	= null;

		if (_getSnapshotMetadata(repositoryUrl, project, version).isPresent())
		{
			Metadata				metadata	= _getSnapshotMetadata(repositoryUrl, project, version).get();
			List<SnapshotVersion>	snapshots	= metadata.getVersioning().getSnapshotVersions();

			Optional<SnapshotVersion> sv = snapshots.stream().filter(v -> v.getExtension().equals("pom")).findFirst();
			if (sv.isPresent())
			{
				String snapshotVersion = sv.get().getVersion();
				filename = String.format(pomFile, project.getId(), snapshotVersion);
			} else
			{
				throw new GradleException("Could not get the version for Snapshot: " + version.toString());
			}
		} else
		{
			throw new GradleException("Failed to parse METADATA from: " + repositoryUrl);
		}
		IOWrapper wrapper = IOHelper.getIOWrapper(project, repositoryUrl, filename, version.toString());
		try
		{
			if (wrapper.getInputStream() != null)
				pomModel = new ModelReader().read(wrapper.getInputStream());
		} catch (Exception e)
		{
			throw new GradleException("Failed to parse POM from: " + StringUtil.asPath(repositoryUrl, version, filename));
		}
		return Optional.ofNullable(pomModel);
	}
}
