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

package net.galacticraft.internal.artifacts.legacy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.initialization.Settings;
import org.gradle.api.java.archives.Manifest;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.Jar;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.gradle.core.plugin.Configurable;
import net.galacticraft.gradle.core.plugin.GradlePlugin;

public class LegacyArtifactsPlugin extends GradlePlugin
{
	
	public static final String SOURCES_JAR_TASK_NAME = "sourcesJar";
	public static final String DEOBF_JAR_TASK_NAME = "deobfJar";
	public static final String MAVEN_JAR_TASK_NAME = "mavenJar";
	
	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins, @NotNull TaskContainer tasks)
	{
		target.getGradle().afterProject(project -> {
			String artifactVersion = target.findProperty("mc_version") + "-" + target.findProperty("version");
			
			tasks.named(JavaPlugin.JAR_TASK_NAME, Jar.class, jar -> {
				jar.manifest(getManifest(target));
				jar.getArchiveVersion().set(artifactVersion);
			});
			
//			TaskProvider<Jar> sourcesJarTaskProvider = 
				registerTask(tasks, SOURCES_JAR_TASK_NAME, sourcesJar -> {
				sourcesJar.dependsOn(JavaPlugin.CLASSES_TASK_NAME);
				sourcesJar.setDuplicatesStrategy(DuplicatesStrategy.FAIL);
				sourcesJar.getArchiveClassifier().set("sources");
				sourcesJar.getArchiveVersion().set(artifactVersion);
				sourcesJar.from(sourceSets(SourceSet.MAIN_SOURCE_SET_NAME).getAllSource());
			});

//			TaskProvider<Jar> deobfJarTaskProvider = 
				registerTask(tasks, DEOBF_JAR_TASK_NAME, deobfJar -> {
				deobfJar.setDuplicatesStrategy(DuplicatesStrategy.FAIL);
				deobfJar.getArchiveClassifier().set("deobf");
				deobfJar.getArchiveVersion().set(artifactVersion);
				deobfJar.from(sourceSets(SourceSet.MAIN_SOURCE_SET_NAME).getOutput());
				deobfJar.from(sourceSets(SourceSet.MAIN_SOURCE_SET_NAME).getAllJava());
			});
			
//			TaskProvider<Jar> mavenJarTaskProvider = 
				registerTask(tasks, MAVEN_JAR_TASK_NAME, mavenJar -> {
				mavenJar.setDuplicatesStrategy(DuplicatesStrategy.FAIL);
				mavenJar.getArchiveClassifier().set("maven");
				mavenJar.getArchiveVersion().set(artifactVersion);
				mavenJar.from(sourceSets(SourceSet.MAIN_SOURCE_SET_NAME).getOutput(), main -> {
					main.exclude("micdoodle8/mods/miccore/IntCache.*");
				});
				mavenJar.from(new File(target.getProjectDir(), "/etc/replace"), dir -> {
					dir.include("IntCache.class");
					dir.into("micdoodle8/mods/miccore");
				});
			});
			
//			PublishArtifact sourcesJarArtifact = createPublishArtifact(sourcesJarTaskProvider, target);
//			PublishArtifact deobfJarArtifact = createPublishArtifact(deobfJarTaskProvider, target);
//			PublishArtifact mavenJarArtifact = createPublishArtifact(mavenJarTaskProvider, target);
//			
//			GalacticInternalExtension ext = extensions.find(GalacticInternalExtension.class);
//			SpecialPublishExt publishExt = extensions.find(SpecialPublishExt.class);
//			
//			publishExt.getPublications().named(ext.getPublication().get(), MavenPublication.class, pub -> {
//				pub.artifact(sourcesJarArtifact);
//				pub.artifact(deobfJarArtifact);
//				pub.artifact(mavenJarArtifact);
//			});

		});
	}
	
//	private LazyPublishArtifact createPublishArtifact(TaskProvider<Jar> taskProvider, Project target)
//	{
//		return new LazyPublishArtifact(taskProvider, ((ProjectInternal) target).getFileResolver()); 
//	}
	
	private TaskProvider<Jar> registerTask(TaskContainer tasks, String taskName, Action<Jar> action)
	{
		TaskProvider<Jar> provider = tasks.register(taskName, Jar.class, action);
		provider.configure(jar -> {
			jar.setGroup(BasePlugin.BUILD_GROUP);
		});
		return provider;
	}
	
    private SourceSet sourceSets(String sourceSet) {
        return extensions.find(JavaPluginExtension.class).getSourceSets().getByName(sourceSet);
    }
	
	private Action<Manifest> getManifest(final Project target)
	{
		return manifest -> {
			manifest.attributes(Configurable.configure(manifest.getAttributes(), entry -> {
				entry.put("Specification-Title"         , "Galacticraft");
				entry.put("Specification-Vendor"        , "TeamGalacticraft");
				entry.put("Specification-Version"       , target.getVersion());
				entry.put("Implementation-Title"        , "Galacticraft");
				entry.put("Implementation-Version"      , target.getVersion());
				entry.put("Implementation-Vendor"       , "TeamGalacticraft");
				entry.put("Implementation-Build-Date"   , new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));
				entry.put("FMLAT"                       , "accesstransformer.cfg");
				entry.put("FMLCorePluginContainsFMLMod" , true);
				entry.put("FMLCorePlugin"               , "micdoodle8.mods.miccore.MicdoodlePlugin");
				entry.put("Built-On-Minecraft"          , target.findProperty("mc_version"));
				entry.put("Built-On-Forge"              , target.findProperty("forge_version"));
				entry.put("Built-On-Mapping"            , target.findProperty("mapping_channel") + "-" + target.findProperty("mapping_version"));
				entry.put("Built-Using"                 , "ForgeGradle: 5.1.+");
			}));
		};
	}

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{
	}
}
