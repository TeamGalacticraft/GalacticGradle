
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
