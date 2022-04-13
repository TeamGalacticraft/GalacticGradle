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
package net.galacticraft.gradle.convention;

import java.io.File;

import org.gradle.api.artifacts.dsl.RepositoryHandler;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ConventionConstants {
	
	@UtilityClass
	public static class PropertyAttributes {
        public static final String GALACTIC_SIGNING_KEY = "galacticSigningKey";
        public static final String GALACTIC_SIGNING_PASSWORD = "galacticSigningPassword";
        public static final String GALACTIC_SNAPSHOT_REPO = "galacticSnapshotRepo";
        public static final String GALACTIC_RELEASE_REPO = "galacticReleaseRepo";
        public static final String GALACTIC_KEY_STORE = "galacticKeyStore";
        public static final String GALACTIC_KEY_STORE_ALIAS = "galacticKeyStoreAlias";
        public static final String GALACTIC_KEY_STORE_PASSWORD = "galacticKeyStorePassword";
	}
	
	@UtilityClass
	public static class License {
		public static final String HEADER_FILENAME = "HEADER.txt";
		public static final File HEADER_FILE = new File(HEADER_FILENAME);
	}
	
	@UtilityClass
	public static class Configurations {
		public static final String PARENT = "parent";
	}
	
	@UtilityClass
    public static class Repositories {
        public static final String SNAPSHOTS = "https://repo.galacticraft.net/repository/maven-snapshots/";
        public static final String RELEASES = "https://repo.galacticraft.net/repository/maven-releases/";
    }
	
	public static void galacticraftRepo(final RepositoryHandler repos) {
		repos.maven(repo -> {
			repo.setUrl("https://repo.galacticraft.net/repository/maven-public/");
			repo.setName("galacticraft");
		});
	}
}
