package net.galacticraft.gradle.core.xml.model;

public class Developer extends Contributor implements Cloneable
{
	private String id;

	public Developer clone()
	{
		try
		{
			Developer copy = (Developer) super.clone();
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
}
