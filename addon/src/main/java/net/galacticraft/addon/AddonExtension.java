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

package net.galacticraft.addon;

import javax.inject.Inject;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import lombok.Getter;
import net.galacticraft.gradle.core.version.Version;

@Getter
public class AddonExtension
{
	public static final String EMPTY_VERSION = Version.Null().toString();
	
	private final Property<String>	version;
	private final Property<Boolean>	useLatestSnapshot, useLatestRelease;

	@Inject
	public AddonExtension(final ObjectFactory factory)
	{
		this.version = factory.property(String.class);
		this.useLatestSnapshot = factory.property(Boolean.class).convention(false);
		this.useLatestRelease = factory.property(Boolean.class).convention(false);
	}

	public void version(final String version)
	{
		this.getVersion().set(version);
	}

	public void latestSnapshot()
	{
		this.getUseLatestSnapshot().set(true);
	}

	public void latestRelease()
	{
		this.getUseLatestRelease().set(true);
	}
}
