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
			  project.getLogger().lifecycle(project.getVersion().toString());
			  Version projectVersion = new Version(project.getVersion().toString());
			  return projectVersion.isStable() && !isSnapshot(project);
		  }

		  private Versions() {
		  }
}
