package net.galacticraft.plugins.curseforge.internal;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.groovy.runtime.IOGroovyMethods;
import org.codehaus.groovy.runtime.ResourceGroovyMethods;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;

import groovy.lang.Closure;
import net.galacticraft.plugins.curseforge.internal.json.CurseError;

public class Util {
	/**
	 * Resolve an object into a file
	 *
	 * @param project The project
	 * @param obj     The object to resolve
	 * @return A file instance
	 */
	public static File resolveFile(Project project, Object obj) {
		if (obj == null) {
			throw new NullPointerException("Null path");
		}

		if (obj instanceof File) {
			return (File) obj;
		}

		if (obj instanceof AbstractArchiveTask) {
			return ((File) (((AbstractArchiveTask) obj).getArchiveFile().get()));
		}

		return project.file(obj);
	}

	/**
	 * Resolve an object into a String. If a file is passed, it will be read and
	 * it's contents returned
	 *
	 * @param obj The object to resolve
	 * @return A string
	 */
	public static String resolveString(Object obj) {
		Preconditions.checkNotNull(obj);

		while (obj instanceof Closure) {
			obj = ((Closure) obj).call();
		}

		if (obj instanceof String) {
			return (String) obj;
		}

		if (obj instanceof File) {
			String data = new String(ResourceGroovyMethods.getText(((File) obj), "UTF-8").getBytes("UTF-8"));
			return data;
		}

		if (obj instanceof AbstractArchiveTask) {
			String data = new String(ResourceGroovyMethods
					.getText(((File) ((AbstractArchiveTask) obj).getArchiveFile().get()), "UTF-8").getBytes("UTF-8"));
			return data;
		}

		return obj.toString();
	}

	/**
	 * Issue an HTTP GET to a CurseForge API URL
	 *
	 * @param apiKey The apiKey to use for connecting
	 * @param url    The url
	 * @return The data
	 */
	public static String httpGet(String apiKey, String url) {
		log.debug("HTTP GET to URL: " + url);

		HttpClient client = HttpClientBuilder.create()
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build())
				.build();

		HttpGet get = new HttpGet(new URI(url));
		get.setHeader("X-Api-Token", apiKey);

		HttpResponse response = client.execute(get);

		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == 200) {
			return getContent(response);
		} else {
			if (response.getFirstHeader("content-type").getValue().contains("json")) {
				try {
					InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
					final CurseError error = gson.fromJson(reader, CurseError.class);
					reader.close();
					throw new RuntimeException("[CurseForge] Error Code " + String.valueOf(error.getErrorCode()) + ": "
							+ error.getErrorMessage());
				} catch (Exception e) {
				}
			} else {
				throw new RuntimeException(
						"[CurseForge] HTTP Error Code " + String.valueOf(response.getStatusLine().getStatusCode())
								+ ": " + response.getStatusLine().getReasonPhrase());
			}
		}
	}

	private static String getContent(HttpResponse response) {
		String content = null;
		try {
			StringBuffer result = new StringBuffer();
			try (BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
				String line;
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
			}
			content = result.toString();
		} catch (Exception e) {
		}
		return content;
	}

	/**
	 * Check if a condition is true, and raise an exception if not
	 *
	 * @param condition The condition
	 * @param message   The message to display
	 */
	public static void check(boolean condition, String message) {
		if (!condition) {
			throw new RuntimeException(message);
		}

	}

	public static Gson getGson() {
		return gson;
	}

	private static final Logger log = Logging.getLogger(Util.class);
	private static final Gson gson = new Gson();
}
