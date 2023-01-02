package net.galacticraft.internal.legacy;

import org.gradle.api.Project;
import org.gradle.api.provider.Property;
import org.jetbrains.annotations.NotNull;

public class LegacyExtension
{

	Property<String> mapping_channel;
	Property<String> mapping_version;
	Property<String> mapped_suffix;
	
	public LegacyExtension(final @NotNull Project target)
	{
		this.mapping_channel = target.getObjects().property(String.class).convention("stable");
		this.mapping_version = target.getObjects().property(String.class).convention("39-1.12");
		this.mapped_suffix = target.getObjects().property(String.class).convention("_mapped_stable_39-1.12");
	}

	public Property<String> getMappingChannel()
	{
		return mapping_channel;
	}

	public Property<String> getMappingVersion()
	{
		return mapping_version;
	}

	public Property<String> getMappedSuffix()
	{
		return mapped_suffix;
	}
}
