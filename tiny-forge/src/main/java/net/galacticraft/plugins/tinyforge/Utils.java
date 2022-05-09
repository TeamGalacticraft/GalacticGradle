package net.galacticraft.plugins.tinyforge;

import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    @NotNull
    public static Path getCacheBase(Project project) {
        File gradleUserHomeDir = project.getGradle().getGradleUserHomeDir();
        return Paths.get(gradleUserHomeDir.getPath(), "caches", "tiny-forge");
    }

    @NotNull
    public static File getCache(Project project, String... tail) {
        return Paths.get(getCacheBase(project).toString(), tail).toFile();
    }
}
