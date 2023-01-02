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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Dependency implements Cloneable, InputLocationTracker
{
	private String groupId;

	private String artifactId;

	private String version;

	private String type = "jar";

	private String classifier;

	private String scope;

	private String systemPath;

	private List<Exclusion> exclusions;

	private String optional;

	private Map<Object, InputLocation> locations;

	private InputLocation location;

	private InputLocation groupIdLocation;

	private InputLocation artifactIdLocation;

	private InputLocation versionLocation;

	private InputLocation typeLocation;

	private InputLocation classifierLocation;

	private InputLocation scopeLocation;

	private InputLocation systemPathLocation;

	private InputLocation exclusionsLocation;

	private InputLocation optionalLocation;

	private String managementKey;

	public void addExclusion(Exclusion exclusion)
	{
		getExclusions().add(exclusion);
	}

	public Dependency clone()
	{
		try
		{
			Dependency copy = (Dependency) super.clone();
			if (this.exclusions != null)
			{
				copy.exclusions = new ArrayList<>();
				for (Exclusion item : this.exclusions)
					copy.exclusions.add(item.clone());
			}
			if (copy.locations != null)
				copy.locations = new LinkedHashMap<>(copy.locations);
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getArtifactId()
	{
		return this.artifactId;
	}

	public String getClassifier()
	{
		return this.classifier;
	}

	public List<Exclusion> getExclusions()
	{
		if (this.exclusions == null)
			this.exclusions = new ArrayList<>();
		return this.exclusions;
	}

	public String getGroupId()
	{
		return this.groupId;
	}

	public InputLocation getLocation(Object key)
	{
		if (key instanceof String)
		{
			switch ((String) key)
			{
			case "":
				return this.location;
			case "groupId":
				return this.groupIdLocation;
			case "artifactId":
				return this.artifactIdLocation;
			case "version":
				return this.versionLocation;
			case "type":
				return this.typeLocation;
			case "classifier":
				return this.classifierLocation;
			case "scope":
				return this.scopeLocation;
			case "systemPath":
				return this.systemPathLocation;
			case "exclusions":
				return this.exclusionsLocation;
			case "optional":
				return this.optionalLocation;
			}
			return getOtherLocation(key);
		}
		return getOtherLocation(key);
	}

	public String getOptional()
	{
		return this.optional;
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
			case "groupId":
				this.groupIdLocation = location;
				return;
			case "artifactId":
				this.artifactIdLocation = location;
				return;
			case "version":
				this.versionLocation = location;
				return;
			case "type":
				this.typeLocation = location;
				return;
			case "classifier":
				this.classifierLocation = location;
				return;
			case "scope":
				this.scopeLocation = location;
				return;
			case "systemPath":
				this.systemPathLocation = location;
				return;
			case "exclusions":
				this.exclusionsLocation = location;
				return;
			case "optional":
				this.optionalLocation = location;
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

	public String getScope()
	{
		return this.scope;
	}

	public String getSystemPath()
	{
		return this.systemPath;
	}

	public String getType()
	{
		return this.type;
	}

	public String getVersion()
	{
		return this.version;
	}

	public void removeExclusion(Exclusion exclusion)
	{
		getExclusions().remove(exclusion);
	}

	public void setArtifactId(String artifactId)
	{
		this.artifactId = artifactId;
	}

	public void setClassifier(String classifier)
	{
		this.classifier = classifier;
	}

	public void setExclusions(List<Exclusion> exclusions)
	{
		this.exclusions = exclusions;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}

	public void setOptional(String optional)
	{
		this.optional = optional;
	}

	public void setScope(String scope)
	{
		this.scope = scope;
	}

	public void setSystemPath(String systemPath)
	{
		this.systemPath = systemPath;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public boolean isOptional()
	{
		return (this.optional != null) ? Boolean.parseBoolean(this.optional) : false;
	}

	public void setOptional(boolean optional)
	{
		this.optional = String.valueOf(optional);
	}

	public String toString()
	{
		return "Dependency {groupId=" + this.groupId + ", artifactId=" + this.artifactId + ", version=" + this.version + ", type=" + this.type + "}";
	}

	public String getManagementKey()
	{
		if (this.managementKey == null)
			this.managementKey = this.groupId + ":" + this.artifactId + ":" + this.type + ((this.classifier != null) ? (":" + this.classifier) : "");
		return this.managementKey;
	}

	public void clearManagementKey()
	{
		this.managementKey = null;
	}
}
