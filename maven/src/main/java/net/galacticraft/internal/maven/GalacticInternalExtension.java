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

import javax.inject.Inject;

import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import lombok.Getter;

@Getter
public class GalacticInternalExtension
{
	private Property<String>	publicationName;
	private Property<String>	nexusUser;
	private Property<String>	nexusPassword;

	@Inject
	public GalacticInternalExtension(final Project project)
	{
		this.publicationName = project.getObjects().property(String.class).convention(project.getName().replaceAll("[^\\w\\d]", ""));

		Object	user		= project.findProperty("teamgc.nexus.user");
		Object	password	= project.findProperty("teamgc.nexus.password");

		this.nexusUser = isNull(user) ? property(project.getObjects()) : convention(project.getObjects(), user);
		this.nexusPassword = isNull(password) ? property(project.getObjects()) : convention(project.getObjects(), password);
	}

	private boolean isNull(Object obj)
	{
		return obj == null;
	}

	private Property<String> property(ObjectFactory factory)
	{
		return factory.property(String.class);
	}

	private Property<String> convention(ObjectFactory factory, Object convention)
	{
		return factory.property(String.class).convention(String.valueOf(convention));
	}

	public void nexusUser(final String name)
	{
		this.getNexusUser().set(name);
	}
	
	public void nexusPassword(final String name)
	{
		this.getNexusPassword().set(name);
	}
	
	public void publicationName(final String name)
	{
		this.getPublicationName().set(name);
	}
}
