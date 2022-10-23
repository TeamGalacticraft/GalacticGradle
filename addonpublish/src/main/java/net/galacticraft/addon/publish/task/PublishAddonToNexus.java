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

package net.galacticraft.addon.publish.task;

import java.util.List;

import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPomIssueManagement;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.internal.publication.MavenPomDistributionManagementInternal;
import org.gradle.api.publish.maven.internal.publication.MavenPomInternal;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class PublishAddonToNexus extends PublishToMavenRepository
{

	Logger log = this.getLogger();

	@Input
	public abstract Property<Boolean> getIsDebug();

	@TaskAction
	@Override
	public void publish()
	{
		if (getIsDebug().get())
		{
			log("Nexus Publish Task is set to Debug");
			log("  ");
			log("Artifacts To Upload");
			log("-------------------");
			MavenPublicationInternal publication = getPublicationInternal();

			publication.getArtifacts().forEach(artifact ->
			{
				log(" -> {}", artifact.getFile().getName());
				log("   - Extension: {}", artifact.getExtension());
				log("   - Classifer: {}", (artifact.getClassifier() != null) ? artifact.getClassifier() : "none");
				log("  ");
			});

			this.logMavenPom(publication.getPom());
		} else
		{
			super.publish();
		}
	}

	private void logMavenPom(MavenPomInternal pom)
	{
		log("[POM]");
		log("-----");

		log(" - Name", pom.getName());
		log(" - Description", pom.getDescription());
		log(" - InceptionYear", pom.getInceptionYear());
		log(" - Url", pom.getUrl());
		log(" - Packaging", pom.getPackaging());

		List<MavenPomLicense> license = pom.getLicenses();
		log("   ");
		if (!license.isEmpty())
		{
			log(" - Licenses");
			license.forEach(lic ->
			{
				log("     Name", lic.getName());
				log("     Url", lic.getUrl());
				log("     Distribution", lic.getDistribution());
				log("     Comments", lic.getComments());
				if (license.size() > 1)
				{
					log(" ------ ");
				}
			});
		}

		MavenPomDistributionManagementInternal dist = pom.getDistributionManagement();
		if (dist != null)
		{
			log("  ");
			log(" - DistributionManagement");
			log("     Url", dist.getDownloadUrl());
		}

		MavenPomIssueManagement issue = pom.getIssueManagement();
		if (issue != null)
		{
			log("  ");
			log(" - IssueManagement");
			log("     System", issue.getSystem());
			log("     Url", issue.getUrl());
		}

		log("  ");
		if (!pom.getDevelopers().isEmpty())
		{
			log(" - Developers");
			pom.getDevelopers().forEach(dev ->
			{
				log("     Name", dev.getName());
				log("     Id", dev.getId());
				log("     Email", dev.getEmail());
				log("     Roles", dev.getRoles());
				log("     Timezone", dev.getTimezone());
			});
		}
	}

	private void log(String m, Object... args)
	{
		this.log.lifecycle(m, args);
	}

	private void log(String m, String args)
	{
		if (args != null)
		{
			this.log.lifecycle(m + ": ", args);
		}
	}

	private void log(String m)
	{
		this.log.lifecycle(m);
	}

	private void log(String name, Property<?> m)
	{
		if (m.isPresent())
		{
			this.log.lifecycle("{}: {}", name, String.valueOf(m.get()));
		}
	}
}
