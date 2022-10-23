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

package net.galacticraft.gradle.core.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ResourceLoader
{

	private static final Charset UTF8 = StandardCharsets.UTF_8;

	public static String getResourceOrFile(final String resourceName)
	{
		String templateString = null;
		try
		{
			final Path templatePath = Paths.get(resourceName);
			if (templatePath.toFile().exists())
			{
				templateString = new String(Files.readAllBytes(templatePath), UTF8);
			} else
			{
				InputStream inputStream = getFromClassLoader(resourceName, ResourceLoader.class.getClassLoader());
				if (inputStream == null)
				{
					inputStream = getFromClassLoader(resourceName, Thread.currentThread().getContextClassLoader());
				}

				if (inputStream == null)
				{
					throw new FileNotFoundException("Was unable to find file, or resouce, \"" + resourceName + "\"");
				}
				try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, UTF8)))
				{
					templateString = br.lines().collect(Collectors.joining("\n"));
				}
			}
		} catch (final IOException e)
		{
			throw new RuntimeException(resourceName, e);
		}
		return templateString;
	}

	private static InputStream getFromClassLoader(final String resourceName, final ClassLoader classLoader)
	{
		InputStream inputStream = classLoader.getResourceAsStream(resourceName);
		if (inputStream == null)
		{
			inputStream = classLoader.getResourceAsStream("/" + resourceName);
		}
		return inputStream;
	}
}
