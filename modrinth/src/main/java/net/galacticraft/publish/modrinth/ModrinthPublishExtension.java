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

package net.galacticraft.publish.modrinth;

import javax.inject.Inject;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import com.modrinth.minotaur.ModrinthExtension;

public class ModrinthPublishExtension
{

	private final Property<String>	projectId;
	private final Property<Boolean>	debug;
	private final ModrinthExtension	modrinth;

	@Inject
	public ModrinthPublishExtension(final ObjectFactory factory, final ModrinthExtension modrinthExtension)
	{
		this.projectId = factory.property(String.class);
		this.debug = factory.property(Boolean.class).convention(false);
		this.modrinth = modrinthExtension;
	}

	public Property<String> getProjectId()
	{
		return this.projectId;
	}

	public void projectId(final String id)
	{
		this.getProjectId().set(id);
	}

	public Property<Boolean> getDebug()
	{
		return this.debug;
	}

	public void debug()
	{
		this.getDebug().set(true);
	}

	public ModrinthExtension getModrinth()
	{
		return this.modrinth;
	}

	public void setApiKey(final String apiKey)
	{
		this.getModrinth().getToken().set(apiKey);
	}
}
