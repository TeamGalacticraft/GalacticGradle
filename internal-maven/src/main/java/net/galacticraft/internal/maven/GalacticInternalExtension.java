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

package net.galacticraft.internal.maven;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.XmlProvider;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import lombok.Getter;

@Getter
public class GalacticInternalExtension
{
	public static final String	NEXUS_USER_DEFAULT_PROPERTY		= "GC_NEXUS_USERNAME";
	public static final String	NEXUS_PASSWORD_DEFAULT_PROPERTY	= "GC_NEXUS_PASSWORD";

	private Property<String>	publicationName;
	private Property<String>	nexusUsername;
	private Property<String>	nexusPassword;

	private Action<? super XmlProvider>	xml;

	private ListProperty<AbstractArchiveTask> tasks;

	@Inject
	public GalacticInternalExtension(final Project project)
	{
		this.publicationName = project.getObjects().property(String.class).convention(project.getName().replaceAll("[^\\w\\d]", ""));
		this.tasks = project.getObjects().listProperty(AbstractArchiveTask.class).empty();

		this.nexusUsername = project.getObjects().property(String.class).convention(NEXUS_USER_DEFAULT_PROPERTY);
		this.nexusPassword = project.getObjects().property(String.class).convention(NEXUS_PASSWORD_DEFAULT_PROPERTY);

		this.xml = null;
		
	}

	public void artifacts(final Object... objects)
	{
		artifacts(Arrays.asList(objects));
	}

	public void artifacts(final Collection<Object> objects)
	{
		objects.forEach(o -> artifact(o));;
	}

	public void artifact(final Object object)
	{
		AbstractArchiveTask task = resolve(object);
		if(task != null)
		{
			this.tasks.add(task);
		}
	}

	public void username(final String name)
	{
		this.getNexusUsername().set(name);
	}

	public void password(final String name)
	{
		this.getNexusPassword().set(name);
	}

	public void publicationName(final String name)
	{
		this.getPublicationName().set(name);
	}

	public void xml(Action<? super XmlProvider> xml)
	{
		this.xml = xml;
	}

	@Nullable
	private AbstractArchiveTask resolve(Object in)
	{
		if (in == null)
		{
			return null;
		}

		else if (in instanceof AbstractArchiveTask)
		{
			return ((AbstractArchiveTask) in);
		}

		else if (in instanceof TaskProvider<?>)
		{
			Object provided = ((TaskProvider<?>) in).get();

			if (provided instanceof AbstractArchiveTask)
			{
				return ((AbstractArchiveTask) provided);
			}
		}

		return null;
	}
}
