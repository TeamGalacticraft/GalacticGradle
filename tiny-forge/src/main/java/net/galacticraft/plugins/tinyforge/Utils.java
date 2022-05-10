package net.galacticraft.plugins.tinyforge;

import net.minecraftforge.gradle.common.config.MCPConfigV2;
import net.minecraftforge.gradle.common.util.MavenArtifactDownloader;
import net.minecraftforge.srgutils.IMappingBuilder;
import net.minecraftforge.srgutils.IMappingFile;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraftforge.gradle.common.util.Utils.getZipData;

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

    public static IMappingFile getSrgToOfficial(Project project, String mcVersion) throws IOException {
        File client = MavenArtifactDownloader.generate(project, "net.minecraft:client:" + mcVersion + ":mapping@txt", true);
        if(client == null)
            throw new IllegalStateException("Missing proguard mappings for " + mcVersion);

        File mcp = MavenArtifactDownloader.manual(project, "de.oceanlabs.mcp:mcp_config:" + mcVersion + "@zip", false);
        if(mcp == null) return null;

        MCPConfigV2 mcpConfig = MCPConfigV2.getFromArchive(mcp);
        IMappingFile obfToSrg = IMappingFile.load(new ByteArrayInputStream(getZipData(mcp, mcpConfig.getData("mappings"))));
        if(obfToSrg == null)
            throw new IllegalStateException("Missing MCP TSRG for " + mcVersion);

        IMappingFile mojToObf = IMappingFile.load(client);
        IMappingFile mojToSrg = obfToSrg.reverse().chain(mojToObf.reverse()).reverse();
        return mojToSrg.reverse();
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
