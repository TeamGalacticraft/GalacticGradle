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

package net.galacticraft.plugins.modrinth;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.gradle.api.Project;
import org.gradle.api.plugins.AppliedPlugin;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.modrinth.minotaur.Minotaur;
import com.modrinth.minotaur.ModrinthExtension;
import com.modrinth.minotaur.compat.FabricLoomCompatibility;
import com.modrinth.minotaur.dependencies.Dependency;

import net.galacticraft.plugins.modrinth.internal.Extensions;
import net.galacticraft.plugins.modrinth.internal.GradlePlugin;
import net.galacticraft.plugins.modrinth.internal.property.BooleanProperty;
import net.galacticraft.plugins.modrinth.model.DependencyContainer;
import net.galacticraft.plugins.modrinth.model.type.VersionType;

public class ModrinthUploadPlugin implements GradlePlugin {
	private @MonotonicNonNull Project project;
	
	@Override
	public void apply(@NotNull Project project, @NotNull PluginContainer plugins, @NotNull ExtensionContainer extensions, @NotNull TaskContainer tasks) {
		this.project = project;
		
		if(!project.getPluginManager().hasPlugin("java")) {
			plugins.apply(JavaPlugin.class);
		}
		
		plugins.apply(Minotaur.class);

		ModrinthUploadExtension extension = Extensions.findOrCreate(extensions, "modrinthproject", ModrinthUploadExtension.class, project, project.getObjects());
	
		
		
		project.getGradle().afterProject(set -> {
			addLoaderForPlugin(extension);
			if(extension.getLoaders().get().contains("forge")) {
				detectGameVersionForge(extension);
			}
			if(extension.getLoaders().get().contains("fabric")) {
				detectGameVersionFabric(extension);
			}
			
			String version = extension.getVersionNumnber().get();
	        for(String val : VersionType.CONSTANTS) {
	        	if(version.contains(val)) {
	        		extension.getVersionType().set(val);
	        	}
	        }
	        
			testExtension(extension);
			project.getExtensions().configure(ModrinthExtension.class, e -> {
				e.getToken().set((String) project.findProperty("MODRINTH_TOKEN"));
				e.getProjectId().set(extension.getProjectId().get());
				e.getVersionNumber().set(extension.getVersionNumnber().get());
				e.getVersionType().set(extension.getVersionType().get());
				e.getDebugMode().set(extension.getDebug().get());
				e.getGameVersions().set(extension.getGameVersions().get());
				e.getLoaders().set(extension.getLoaders().get());
				e.getFailSilently().set(false);
				e.getDetectLoaders().set(false);
				if(!extension.getChangelog().get().isEmpty()) {
					e.getChangelog().set(extension.getChangelog());
				}
				for(DependencyContainer dep : extension.getDependencies()) {
					Dependency dependency = new Dependency(dep.getName(), dep.getType().get());
					e.getDependencies().add(dependency);
				}
				project.getTasks().withType(AbstractArchiveTask.class, artifact -> {
					File jarFile = resolveFile(artifact);
					e.getUploadFile().set(jarFile);
				});
			});
		});
	}
	
	private void testExtension(ModrinthUploadExtension extension) {
		if(project.hasProperty("upload.modrinth.test")) {
			BooleanProperty bool = new BooleanProperty(project.findProperty("upload.modrinth.test"));
			if(bool.isTrue()) {
				log("projectId=" + extension.getProjectId().get());
				log("debug=" + extension.getDebug().get());
				log("changelog=" + extension.getChangelog().get());
				log("gameVersions=" + Arrays.toString(extension.getGameVersions().get().toArray()));
				log("loaders=" +  Arrays.toString(extension.getLoaders().get().toArray()));
				log("version=" + extension.getVersionNumnber().get());
				log("versionType=" + extension.getVersionType().get());
				log("dependencies [");
				for(DependencyContainer dep : extension.getDependencies()) {
					log("  (" + dep.getName() + ") {");
					log("    version=" + dep.getVersion().get());
					log("    type=" + dep.getType().get());
					log("  }");
				}
				log("]");
			}
		}
	}
	
	private void log(Object object) {
		this.project.getLogger().lifecycle("{}", object);
	}

    private void detectGameVersionForge(ModrinthUploadExtension extension) {
        try {
        	File mcMeta = new File(project.getBuildDir(), "downloadMCMeta/version.json");
        	if(mcMeta.exists()) {
        		Gson gson = new Gson();
        		Reader reader = Files.newBufferedReader(Paths.get(mcMeta.toURI()));
        		Map<?, ?> map = gson.fromJson(reader, Map.class);
        		
        		if(map.get("id") != null) {
        			String version = (String) map.get("id");
                    project.getLogger().debug("Detected fallback game version {} from ForgeGradle.", version);
                    if (extension.getGameVersions().get().isEmpty()) {
                        project.getLogger().debug("Adding game version {} because the game versions list is empty.", version);
                        extension.getGameVersions().add(version);
                    }
        		}
        	}
        } catch (final Exception e) {
            project.getLogger().debug("Failed to detect ForgeGradle game version.", e);
        }
    }

    private void detectGameVersionFabric(ModrinthUploadExtension extension) {
        if (project.getPluginManager().findPlugin("fabric-loom") != null ||
            project.getPluginManager().findPlugin("org.quiltmc.loom") != null) {
            try {
                String loomGameVersion = FabricLoomCompatibility.detectGameVersion(project);
                if (extension.getGameVersions().get().isEmpty()) {
                    project.getLogger().debug("Detected fallback game version {} from Loom.", loomGameVersion);
                    extension.getGameVersions().add(loomGameVersion);
                } else {
                    project.getLogger().debug("Detected fallback game version {} from Loom, but did not apply because game versions list is not empty.", loomGameVersion);
                }
            } catch (final Exception e) {
                project.getLogger().debug("Failed to detect Loom game version.", e);
            }
        } else {
            project.getLogger().debug("Loom is not present; no game versions were added.");
        }
    }

    private void addLoaderForPlugin(ModrinthUploadExtension extension) {
    	final Map<String, String> loaderMap = new HashMap<>();
    	loaderMap.put("net.minecraftforge.gradle", "forge");
    	loaderMap.put("fabric-loom", "fabric");
    	loaderMap.put("org.quiltmc.loom", "quilt");
    	
    	for(Entry<String, String> entry : loaderMap.entrySet()) {
        	String pluginName = entry.getKey();
        	String loaderName = entry.getValue();
    		try {
                final AppliedPlugin plugin = project.getPluginManager().findPlugin(pluginName);

                if (plugin != null) {
                    extension.getLoaders().add(loaderName);
                    project.getLogger().debug("Applying loader {} because plugin {} was found.", loaderName, pluginName);
                } else {
                	project.getLogger().debug("Could not automatically apply loader {} because plugin {} has not been applied.", loaderName, pluginName);
                }
            } catch (final Exception e) {
            	project.getLogger().debug("Failed to detect plugin {}.", pluginName, e);
            }
    	}
    }
    
    private File resolveFile(Object in) {
        // If input or project is null we can't really do anything...
        if (in == null || project == null) {
        	log("resolveFile returned Null");
            return null;
        }

        // If the file is a Java file handle no additional handling is needed.
        else if (in instanceof File) {
        	log("resolveFile returned File");
            return (File) in;
        }

        // Grabs the file from an archive task. Allows build scripts to do things like the jar task directly.
        else if (in instanceof AbstractArchiveTask) {
        	log("resolveFile returned AbstractArchiveTask File");
            return ((AbstractArchiveTask) in).getArchiveFile().get().getAsFile();
        }

        // Grabs the file from an archive task wrapped in a provider. Allows Kotlin DSL buildscripts to also specify
        // the jar task directly, rather than having to call #get() before running.
        else if (in instanceof TaskProvider<?>) {
        	log("resolveFile returned TaskProvider");
            Object provided = ((TaskProvider<?>) in).get();

            // Check to see if the task provided is actually an AbstractArchiveTask.
            if (provided instanceof AbstractArchiveTask) {
            	log("resolveFile returned AbstractArchiveTask from Provider");
                return ((AbstractArchiveTask) provided).getArchiveFile().get().getAsFile();
            }
        }

        // Fallback to Gradle's built-in file resolution mechanics.
        return project.file(in);
    }
}
