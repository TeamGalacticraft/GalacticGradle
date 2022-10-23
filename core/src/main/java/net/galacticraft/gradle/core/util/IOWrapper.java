package net.galacticraft.gradle.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.annotation.Nullable;

public class IOWrapper
{
	private final URL url;
	private InputStream inputStream;
	
	public IOWrapper(URL url)
	{
		super();
		this.url = url;
		try
		{
			this.inputStream = url.openStream();
		} catch (IOException e)
		{
			this.inputStream = null;
			e.printStackTrace();
		}
	}

	public URL getUrl()
	{
		return url;
	}

	@Nullable
	public InputStream getInputStream()
	{
		return inputStream;
	}
	
}
