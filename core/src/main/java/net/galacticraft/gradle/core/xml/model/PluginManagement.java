package net.galacticraft.gradle.core.xml.model;

public class PluginManagement extends PluginContainer implements Cloneable
{
	public PluginManagement clone()
	{
		try
		{
			PluginManagement copy = (PluginManagement) super.clone();
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}
}
