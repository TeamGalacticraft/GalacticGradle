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
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenArtifact;
import org.gradle.api.publish.maven.MavenArtifactSet;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class TestPublishTask extends DefaultTask
{

	@Input
	public abstract Property<MavenArtifactRepository> getMavenRepository();

	@Input
	public abstract Property<MavenPublication> getMavenPublication();

	@TaskAction
	public void runTest()
	{
		MavenArtifactRepository repository = this.getMavenRepository().get();
		log("| ");
		log("| Maven Repository");
		log("| ----------------");
		log("| URL:	{}", repository.getUrl());
		
		MavenPublication publication = this.getMavenPublication().get();
		log("| ");
		log("| Publication Identity");
		log("| --------------------");
		log("| ArtifactID:	{}", publication.getArtifactId());
		log("| GroupID:	{}", publication.getGroupId());
		log("| Version:	{}", publication.getVersion());
		log("| ");

		MavenArtifactSet artifacts = publication.getArtifacts();
		
		log("| Artifacts To Upload");
		log("| -------------------");
		for(MavenArtifact entry : artifacts)
		{
			log("| [{}]", entry.getFile().getName());
			log("|   > Classifer:   {}", entry.getClassifier());
			log("| ");
		}
	}

	private void log(String m, Object... args)
	{
		for (Object arg : args)
		{
			if (arg != null)
			{
				if (arg instanceof Property<?>)
				{
					Property<?> property = (Property<?>) arg;
					if (property.isPresent())
						this.getLogger().lifecycle(m, property.get());
				} else
				{
					this.getLogger().lifecycle(m, args);
				}
			}
		}
	}

	private void log(String m)
	{
		this.getLogger().lifecycle(m);
	}
}
