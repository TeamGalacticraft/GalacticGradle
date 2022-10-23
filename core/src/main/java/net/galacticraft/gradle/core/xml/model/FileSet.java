package net.galacticraft.gradle.core.xml.model;

public class FileSet extends PatternSet implements Cloneable
{
	private String directory;

	public FileSet clone()
	{
		try
		{
			FileSet copy = (FileSet) super.clone();
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getDirectory()
	{
		return this.directory;
	}

	public void setDirectory(String directory)
	{
		this.directory = directory;
	}

	public String toString()
	{
		return "FileSet {directory: " + getDirectory() + ", " + super.toString() + "}";
	}
}
