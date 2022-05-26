package net.galacticraft.plugins.addon.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gradle.api.Project;

public class Util {
    public static Path getCacheBase(Project project) {
        File gradleUserHomeDir = project.getGradle().getGradleUserHomeDir();
        return Paths.get(gradleUserHomeDir.getPath(), "caches", "galactic_gradle");
    }

    public static File getCache(Project project, String... tail) {
    	File cache = Paths.get(getCacheBase(project).toString(), tail).toFile();
    	if(!cache.exists()) {
    		cache.mkdirs();
    	}
        return cache;
    }
}
