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
package net.galacticraft.gradle.convention.util;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.gradle.api.JavaVersion;
import org.gradle.api.Project;

import net.galacticraft.gradle.common.Version;

public final class Versions {
	  public static int versionNumber(final @NonNull JavaVersion version) {
		    return version.ordinal() + 1;
		  }

		  public static String versionString(final int version) {
		    if(version <= 8) {
		      return "1." + version;
		    } else {
		      return String.valueOf(version);
		    }
		  }

		  public static String versionString(final @NonNull JavaVersion version) {
		    if(version == JavaVersion.VERSION_1_9) {
		      return "9";
		    } else if(version == JavaVersion.VERSION_1_10) {
		      return "10";
		    } else {
		      return version.toString();
		    }
		  }

		  public static boolean isSnapshot(final @NonNull Project project) {
		    return project.getVersion().toString().contains("-SNAPSHOT");
		  }

		  public static boolean isRelease(final @NonNull Project project) {
			  Version projectVersion = new Version(project.getVersion().toString());
			  return projectVersion.isStable() && !isSnapshot(project);
		  }

		  private Versions() {
		  }
}
