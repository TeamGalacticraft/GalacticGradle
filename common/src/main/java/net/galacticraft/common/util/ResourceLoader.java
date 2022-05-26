package net.galacticraft.common.util;

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
public class ResourceLoader {

	private static final Charset UTF8 = StandardCharsets.UTF_8;

	public static String getResourceOrFile(final String resourceName) {
		String templateString = null;
		try {
			final Path templatePath = Paths.get(resourceName);
			if (templatePath.toFile().exists()) {
				templateString = new String(Files.readAllBytes(templatePath), UTF8);
			} else {
				InputStream inputStream = getFromClassLoader(resourceName, ResourceLoader.class.getClassLoader());
				if (inputStream == null) {
					inputStream = getFromClassLoader(resourceName, Thread.currentThread().getContextClassLoader());
				}

				if (inputStream == null) {
					throw new FileNotFoundException("Was unable to find file, or resouce, \"" + resourceName + "\"");
				}
				try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, UTF8))) {
					templateString = br.lines().collect(Collectors.joining("\n"));
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(resourceName, e);
		}
		return templateString;
	}

	private static InputStream getFromClassLoader(final String resourceName, final ClassLoader classLoader) {
		InputStream inputStream = classLoader.getResourceAsStream(resourceName);
		if (inputStream == null) {
			inputStream = classLoader.getResourceAsStream("/" + resourceName);
		}
		return inputStream;
	}
}
