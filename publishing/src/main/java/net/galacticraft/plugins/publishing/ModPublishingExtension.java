package net.galacticraft.plugins.publishing;

import java.util.Objects;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

import net.galacticraft.plugins.curseforge.CurseUploadExtension;
import net.galacticraft.plugins.modrinth.ModrinthUploadExtension;

public class ModPublishingExtension {
	
	private final ModrinthUploadExtension modrinthExtension;
	private final CurseUploadExtension curseforgeExtension;
	private final Property<Boolean> debug;
	
	@Inject
	public ModPublishingExtension(ObjectFactory factory, ModrinthUploadExtension modrinthExtension, CurseUploadExtension curseforgeExtension) {
		this.modrinthExtension = modrinthExtension;
		this.curseforgeExtension = curseforgeExtension;
		this.debug = factory.property(Boolean.class).convention(false);
	}
	
	public Property<Boolean> getDebug() {
		return this.debug;
	}
	
	public void debug() {
		this.debug.set(true);
	}
	
    public void modrinth(final Action<? super ModrinthUploadExtension> configureAction) {
    	Objects.requireNonNull(configureAction, "configureAction").execute(this.modrinthExtension);
    }
    
    public void curseforge(final Action<? super CurseUploadExtension> configureAction) {
    	Objects.requireNonNull(configureAction, "configureAction").execute(this.curseforgeExtension);
    }
}
