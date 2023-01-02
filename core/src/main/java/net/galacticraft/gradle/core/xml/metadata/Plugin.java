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

package net.galacticraft.gradle.core.xml.metadata;

import java.io.Serializable;

@SuppressWarnings("all")
@Deprecated
public class Plugin implements Cloneable
{

	private String name;

	private String prefix;

	private String artifactId;

	public Plugin clone()
	{
		try
		{
			Plugin copy = (Plugin) super.clone();

			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) new UnsupportedOperationException(getClass().getName() + " does not support clone()").initCause(ex);
		}
	}

	public String getArtifactId()
	{
		return this.artifactId;
	}

	public String getName()
	{
		return this.name;
	}

	public String getPrefix()
	{
		return this.prefix;
	}

	public void setArtifactId(String artifactId)
	{
		this.artifactId = artifactId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

}
