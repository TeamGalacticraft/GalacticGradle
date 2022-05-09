package net.galacticraft.plugins.tinyforge.providers;

import net.minecraftforge.gradle.mcp.ChannelProvider;
import net.minecraftforge.gradle.mcp.MCPRepo;
import org.gradle.api.Project;
import org.gradle.internal.impldep.com.google.common.collect.ImmutableSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Set;

// todo(@JustPyrrha, 02/05/2022): Currently unused, finish allowing general .tiny mappings.
public class TinyChannelProvider implements ChannelProvider {
    @NotNull
    @Override
    public Set<String> getChannels() {
        return ImmutableSet.of("tiny");
    }


    @Nullable
    @Override
    public File getMappingsFile(@NotNull MCPRepo mcpRepo, @NotNull Project project, @NotNull String channel, @NotNull String version) throws IOException {
        return null;
    }
}
