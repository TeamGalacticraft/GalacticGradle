package net.galacticraft.plugins.tinyforge;

import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.plugins.tinyforge.providers.YarnChannelProvider;
import net.minecraftforge.gradle.mcp.ChannelProvidersExtension;
import org.gradle.api.Project;
import org.jetbrains.annotations.NotNull;

public class TinyForgePlugin implements GradlePlugin {
    @Override
    public void apply(@NotNull Project project) {
        GradlePlugin.super.apply(project);

        // attempt to apply ForgeGradle to the project
        project.getPluginManager().apply(UserDevPlugin.class);
        
        // then ensure ForgeGradle is applied
        ChannelProvidersExtension channelProviders = project.getExtensions().findByType(ChannelProvidersExtension.class);
        if (channelProviders == null)
            throw new IllegalStateException("The TinyForge plugin must be applied after ForgeGradle.");

        // register mappings channels
        channelProviders.addProvider(new YarnChannelProvider(project));
    }
}
