package net.galacticraft.plugins.curseforge.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

public class Util {
	private static final Gson gson = new Gson();
	
	public static Gson getGson() {
		return gson;
	}
	
    public static String fromResourceAsString(String fileName) {
        ClassLoader classLoader = Util.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        String output = null;
        
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
				output = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
			} catch (IOException e) {
			}
        }
        return output;
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
}
