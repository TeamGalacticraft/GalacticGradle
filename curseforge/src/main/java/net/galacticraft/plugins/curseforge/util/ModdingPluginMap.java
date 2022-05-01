package net.galacticraft.plugins.curseforge.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.gradle.api.Project;
import org.gradle.api.plugins.AppliedPlugin;

import net.galacticraft.plugins.curseforge.CurseUploadExtension;

public class ModdingPluginMap extends HashMap<String, String> {
	private static final long serialVersionUID = -3864757207654878766L;
	
	public static final ModdingPluginMap instance = new ModdingPluginMap();
	
	private ModdingPluginMap() {
		super();
		super.put("net.minecraftforge.gradle", "Forge");
		super.put("net.minecraftforge.gradle.forge", "Forge");
		super.put("fabric-loom", "Fabric");
		// uncomment when CurseForge adds QuiltMC versions
		//this.put("org.quiltmc.loom", "quilt");
	}
	
	public void runCheck(Project project, CurseUploadExtension extension) {
		this.keySet().forEach(key -> {
	        try {
	            final AppliedPlugin plugin = project.getPluginManager().findPlugin(key);

	            if (plugin != null) {
	                extension.getGameVersions().add(this.get(key));
	                project.getLogger().debug("[CurseForge] Applying gameVersion {} because plugin {} was found.", this.get(key), key);
	            } else {
	            	project.getLogger().debug("[CurseForge] Could not automatically apply gameVersion {} because plugin {} has not been applied.", this.get(key), key);
	            }
	        } catch (final Exception e) {
	        	project.getLogger().error("[CurseForge] Failed to detect plugin {}.", key, e);
	        }
		});
	}
	
	@Override
	public String put(String key, String value) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public String putIfAbsent(String key, String value) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public void putAll(Map<? extends String, ? extends String> m) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public void clear() {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public String compute(String key, BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public String computeIfAbsent(String key, Function<? super String, ? extends String> mappingFunction) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public String computeIfPresent(String key,
			BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public String merge(String key, String value,
			BiFunction<? super String, ? super String, ? extends String> remappingFunction) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public String remove(Object key) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public boolean remove(Object key, Object value) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public boolean replace(String key, String oldValue, String newValue) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	@Override
	public String replace(String key, String value) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
	
	@Override
	public void replaceAll(BiFunction<? super String, ? super String, ? extends String> function) {
		throw new UnsupportedOperationException("ModdingPluginMap is immutable");
	}
}
