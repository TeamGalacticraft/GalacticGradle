package net.galacticraft.plugins.tinyforge;

import net.minecraftforge.gradle.common.util.MavenArtifactDownloader;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Utils {

    private static final Map<String, File> dependencyCache = new ConcurrentHashMap<>();

    @NotNull
    public static Path getCacheBase(Project project) {
        File gradleUserHomeDir = project.getGradle().getGradleUserHomeDir();
        return Paths.get(gradleUserHomeDir.getPath(), "caches", "tiny-forge");
    }

    @NotNull
    public static File getCache(Project project, String... tail) {
        return Paths.get(getCacheBase(project).toString(), tail).toFile();
    }

    // Thanks Parchment <3
    public static File getDependency(Project project, String dependencyNotation) {
        File cached = dependencyCache.get(dependencyNotation);
        if(cached != null) return cached;

        File dependency = null;
        try {
            Iterator<File> iterator = project.getConfigurations().detachedConfiguration(project.getDependencies().create(dependencyNotation)).resolve().iterator();
            dependency = iterator.hasNext() ? iterator.next() : null;
        } catch (Exception e) {
            project.getLogger().debug("Error when retrieving dependency using Gradle configuration resolution, using MavenArtifactDownloader", e);
        }

        // Fallback to MavenArtifactDownloader, however it doesn't support snapshot versions
        if (dependency == null)
            dependency = MavenArtifactDownloader.manual(project, dependencyNotation, false);

        if (dependency != null)
            dependencyCache.put(dependencyNotation, dependency);
        return dependency;
    }
}
