package net.galacticraft.plugins.modrinth.model.api;

import org.gradle.api.Named;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;

public interface ModDependency extends Named {
	
    @Internal
    @Override
    String getName();
    
    Property<String> getType();

    Property<String> getVersion();
    
    default void version(String version) {
    	this.getVersion().set(version);
    }
}
