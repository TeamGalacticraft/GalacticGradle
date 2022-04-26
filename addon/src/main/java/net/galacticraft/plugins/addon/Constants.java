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

package net.galacticraft.plugins.addon;

import java.util.regex.Pattern;

import net.galacticraft.plugins.addon.util.Version;

public final class Constants {
	public static final String NAME = "GalacticGradle";
	public static final Version VERSION = Version.set(Constants.rawVersion());
	public static final String TASK_GROUP = "Galacticraft-Addon";
	public static final String GITHUB_ORG = "TeamGalacticraft";
	
	public static final String version() {
		return Constants.VERSION.toString();
	}
	
    public static class Repositories {
        public static final String GALACTICRAFT = "https://repo.galacticraft.net/repository/maven-public/";
        
        private Repositories() {
        }
    }
	
    public static class Dependencies {
        public static final String GC_GROUP = "dev.galacticraft";
        
    	public static final String GC_LEGACY = GC_GROUP + ":" + "galacticraft-legacy";
    	public static final String GALACTICRAFT = GC_GROUP + ":" + "galacticraft";
    	public static final String GALACTICRAFT_API = GC_GROUP + ":" + "galacticraft-api";
    
        public static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";
        
        private Dependencies() {
        }
    }
    
    private Constants() {
    }
	
	private static String rawVersion() {
		final String rawVersion = Constants.class.getPackage().getImplementationVersion();
		return (rawVersion == null) ? "dev" : rawVersion;
	}
	
    /**
     * <p>Ids must conform to the following requirements:</p>
     *
     * <ul>
     *     <li>Must be between 2 and 64 characters in length</li>
     *     <li>Must start with a lower case letter (a-z)</li>
     *     <li>May only contain a mix of lower case letters (a-z),
     *     numbers (0-9), dashes (-), and underscores (_)</li>
     * </ul>
     */
    public static final Pattern VALID_ID_PATTERN = Pattern.compile("^[a-z][a-z0-9-_]{1,63}$");

    public static final String INVALID_ID_REQUIREMENTS_MESSAGE = "IDs can be between 2 and 64 characters long and must start with a lower case"
            + " letter, followed by any mix of lower case letters (a-z), numbers (0-9), dashes (-) and underscores (_).";
}
