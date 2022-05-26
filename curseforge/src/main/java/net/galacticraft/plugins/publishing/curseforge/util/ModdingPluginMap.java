/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.plugins.publishing.curseforge.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.gradle.api.Project;
import org.gradle.api.plugins.AppliedPlugin;

import net.galacticraft.plugins.publishing.curseforge.CurseExtension;

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
	
	public void runCheck(Project project, CurseExtension extension) {
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
