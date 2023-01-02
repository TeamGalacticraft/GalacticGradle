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

package net.galacticraft.gradle.core.xml.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class License implements Cloneable, InputLocationTracker
{
	private String name;

	private String url;

	private String distribution;

	private String comments;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation nameLocation;

	private InputLocation urlLocation;

	private InputLocation distributionLocation;

	private InputLocation commentsLocation;

	public License clone()
	{
		try
		{
			License copy = (License) super.clone();
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getComments()
	{
		return this.comments;
	}

	public String getDistribution()
	{
		return this.distribution;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "name":
				return this.nameLocation;
			case "url":
				return this.urlLocation;
			case "distribution":
				return this.distributionLocation;
			case "comments":
				return this.commentsLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
	}

	public String getName()
	{
		return this.name;
	}

	public void setLocation(Object key, InputLocation location)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				this.location = location;
				return;
			case "name":
				this.nameLocation = location;
				return;
			case "url":
				this.urlLocation = location;
				return;
			case "distribution":
				this.distributionLocation = location;
				return;
			case "comments":
				this.commentsLocation = location;
				return;
			}
			setOtherLocation(key, location);
			return;
		}
		setOtherLocation(key, location);
	}

	public void setOtherLocation(Object key, InputLocation location)
	{
		if (location != null)
		{
			if (this.locations == null)
				this.locations = new LinkedHashMap<>();
			this.locations.put(key, location);
		}
	}

	private InputLocation getOtherLocation(Object key)
	{
		return (this.locations != null) ? this.locations.get(key) : null;
	}

	public String getUrl()
	{
		return this.url;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public void setDistribution(String distribution)
	{
		this.distribution = distribution;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}
}
