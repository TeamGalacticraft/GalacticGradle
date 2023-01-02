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

package net.galacticraft.internal.legacy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.initialization.Settings;
import org.gradle.api.java.archives.Manifest;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.api.tasks.bundling.Jar;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.jetbrains.annotations.NotNull;

import groovy.util.Node;
import net.galacticraft.gradle.core.plugin.Configurable;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamConstants;
import net.galacticraft.gradle.core.plugin.helpers.ExtensionsHelper;
import net.galacticraft.internal.maven.GalacticInternalExtension;
import net.galacticraft.internal.maven.GalacticMavenPlugin;
import net.minecraftforge.gradle.common.util.MinecraftExtension;
import net.minecraftforge.gradle.common.util.RunConfig;
import net.minecraftforge.gradle.mcp.tasks.GenerateSRG;
import net.minecraftforge.gradle.userdev.UserDevPlugin;
import net.minecraftforge.gradle.userdev.tasks.RenameJarInPlace;
import wtf.gofancy.fancygradle.FancyExtension;
import wtf.gofancy.fancygradle.FancyGradle;

public class LegacyDefaultsPlugin extends GradlePlugin
{

	public static final String	SOURCES_JAR_TASK_NAME	= "sourcesJar";
	public static final String	DEOBF_JAR_TASK_NAME		= "deobfJar";
	public static final String	MAVEN_JAR_TASK_NAME		= "mavenJar";

	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
	{
		LegacyExtension extension = extensions.findOrCreate("legacy", LegacyExtension.class, target);

		plugins.apply(UserDevPlugin.class);
		plugins.apply(FancyGradle.class);
		plugins.apply(GalacticMavenPlugin.class);

		repositories.addMaven(TeamConstants.Repositories.Hosted.legacyCommon);

		final File accessTransformer = target.file("src/main/resources/META-INF/accesstransformer.cfg");

		MinecraftExtension			forgeGradle		= extensions.find(MinecraftExtension.class);
		FancyExtension				fancyGradle		= extensions.find(FancyExtension.class);
		GalacticInternalExtension	internalMaven	= extensions.find(GalacticInternalExtension.class);

		Configurable.configureIfNonNull(forgeGradle, minecraft ->
		{
			minecraft.getMappingChannel().set(extension.getMappingChannel());
			minecraft.getMappingVersion().set(extension.getMappingVersion());
			minecraft.setAccessTransformer(accessTransformer);
			minecraft.getRuns().addAll(Arrays.asList(this.clientRunConfig(target), this.serverRunConfig(target)));
		});

		Configurable.configureIfNonNull(fancyGradle, fancy ->
		{
			fancy.patches(apply ->
			{
				apply.getResources();
				apply.getCoremods();
				apply.getAsm();
			});
		});

		final String mcVersion = (String) target.findProperty("mc_version");
		final String modVersion = (String) target.findProperty("mod_version");
		final String version_suffix = (String) target.findProperty("version_suffix");
		
		String artifactVersion = mcVersion + "-" + modVersion + version_suffix;

		tasks.container().named(JavaPlugin.JAR_TASK_NAME, Jar.class, jar ->
		{
			jar.manifest(getManifest(target));
			jar.getArchiveVersion().set(artifactVersion);
			jar.getArchiveBaseName().set("Galacticraft");
		});

		TaskProvider<Jar> sourcesJarTask = registerTask(SOURCES_JAR_TASK_NAME, sourcesJar ->
		{
			sourcesJar.setGroup("build");
			sourcesJar.setDescription("[Legacy-Artifacts] Assembles a jar archive containing the main sources");
			sourcesJar.dependsOn(JavaPlugin.CLASSES_TASK_NAME);
			sourcesJar.setDuplicatesStrategy(DuplicatesStrategy.FAIL);
			sourcesJar.getArchiveClassifier().set("sources");
			sourcesJar.getArchiveVersion().set(artifactVersion);
			sourcesJar.from(sourceSets(extensions, SourceSet.MAIN_SOURCE_SET_NAME).getAllSource());
			sourcesJar.getArchiveBaseName().set("Galacticraft");
			sourcesJar.manifest(getManifest(target));
		});

		TaskProvider<Jar> deobfJarTask = registerTask(DEOBF_JAR_TASK_NAME, deobfJar ->
		{
			deobfJar.setGroup("build");
			deobfJar.setDescription("[Legacy-Artifacts] Assembles a jar archive containing the main classes and java files");
			deobfJar.setDuplicatesStrategy(DuplicatesStrategy.FAIL);
			deobfJar.getArchiveClassifier().set("deobf");
			deobfJar.getArchiveVersion().set(artifactVersion);
			deobfJar.from(sourceSets(extensions, SourceSet.MAIN_SOURCE_SET_NAME).getOutput());
			deobfJar.from(sourceSets(extensions, SourceSet.MAIN_SOURCE_SET_NAME).getAllJava());
			deobfJar.manifest(getManifest(target));
			deobfJar.getArchiveBaseName().set("Galacticraft");
			deobfJar.dependsOn(JavaPlugin.JAR_TASK_NAME);
		});

		TaskProvider<Jar> mavenJarTask = registerTask(MAVEN_JAR_TASK_NAME, mavenJar ->
		{
			mavenJar.setGroup("build");
			mavenJar.setDescription("[Legacy-Artifacts] Custom extension of the main jar with a different IntCache class injected at build");
			mavenJar.setDuplicatesStrategy(DuplicatesStrategy.FAIL);
			mavenJar.getArchiveVersion().set(artifactVersion);
			mavenJar.getArchiveAppendix().set("maven");
			mavenJar.getArchiveBaseName().set("Galacticraft");
			mavenJar.from(sourceSets(extensions, SourceSet.MAIN_SOURCE_SET_NAME).getOutput(), main ->
			{
				main.exclude("micdoodle8/mods/miccore/IntCache.*");
			});
			mavenJar.from(new File(target.getProjectDir(), "/etc/replace"), dir ->
			{
				dir.include("IntCache.class");
				dir.into("micdoodle8/mods/miccore");
			});
			mavenJar.manifest(getManifest(target));
		});

		internalMaven.artifacts(mavenJarTask, deobfJarTask, sourcesJarTask);
		internalMaven.xml(xml ->
		{
			Node	repositories	= xml.asNode().appendNode("repositories");
			Node	repository		= repositories.appendNode("repository");

			repository.appendNode("id", "legacy-common");
			repository.appendNode("name", "Legacy Common");
			repository.appendNode("url", "https://maven.galacticraft.net/repository/legacy-common/");

			Node dependencies = xml.asNode().appendNode("dependencies");

			target.getConfigurations().named("compileRequire").configure(c ->
			{
				c.getAllDependencies().forEach(dep ->
				{
					Node dependency = dependencies.appendNode("dependency");

					dependency.appendNode("groupId", dep.getGroup());
					dependency.appendNode("artifactId", dep.getName());
					dependency.appendNode("version", dep.getVersion().replace(extension.getMappedSuffix().get(), ""));
					dependency.appendNode("scope", "compile");
				});
			});
		});

		target.afterEvaluate(project ->
		{
			final JavaPluginExtension javaConv = project.getExtensions().getByType(JavaPluginExtension.class);
            final RenameJarInPlace renameTask = project.getTasks().maybeCreate("reobfuscateMavenJar", RenameJarInPlace.class);

            renameTask.getClasspath().from(javaConv.getSourceSets().named(SourceSet.MAIN_SOURCE_SET_NAME).map(SourceSet::getCompileClasspath));
            renameTask.getMappings().set(project.getTasks().named("createMcpToSrg", GenerateSRG.class).flatMap(GenerateSRG::getOutput));

            project.getTasks().named(LifecycleBasePlugin.ASSEMBLE_TASK_NAME).configure(t -> t.dependsOn(renameTask));

            final TaskProvider<Jar> jarTask = project.getTasks().named("mavenJar", Jar.class);
            renameTask.getInput().set(jarTask.flatMap(AbstractArchiveTask::getArchiveFile));

			tasks.container().named(MAVEN_JAR_TASK_NAME, task ->
			{
				task.finalizedBy("reobfuscateMavenJar");
			});

			tasks.container().named(JavaPlugin.JAR_TASK_NAME, task ->
			{
				task.finalizedBy("reobfJar");
			});
			
			tasks.container().withType(PublishToMavenRepository.class).forEach(task ->
			{
				task.dependsOn(mavenJarTask.get(), deobfJarTask.get(), sourcesJarTask.get());
			});
		});
	}

	private RunConfig clientRunConfig(Project target)
	{
		String	uuid		= this.variables.get("uuid");
		String	username	= this.variables.get("username");

		RunConfig client = new RunConfig(target, "client");
		client.workingDirectory(new File(target.getRootDir(), "run/client"));
		client.property("forge.logging.markers", "REGISTRIES");
		client.property("forge.logging.console.level", "debug");
		if (uuid != null && username != null)
			client.args("--uuid", uuid, "--username", username);
		client.jvmArg("-Dfml.coreMods.load=micdoodle8.mods.miccore.MicdoodlePlugin");

		return client;
	}

	private RunConfig serverRunConfig(Project target)
	{
		RunConfig server = new RunConfig(target, "server");
		server.workingDirectory(new File(target.getRootDir(), "run/server"));
		server.property("forge.logging.markers", "REGISTRIES");
		server.property("forge.logging.console.level", "debug");
		server.jvmArg("-Dfml.coreMods.load=micdoodle8.mods.miccore.MicdoodlePlugin");

		return server;
	}

	private TaskProvider<Jar> registerTask(String taskName, Action<Jar> action)
	{
		TaskProvider<Jar> provider = tasks.registerTask(taskName, Jar.class, action);
		return provider;
	}

	private SourceSet sourceSets(ExtensionsHelper extensions, String sourceSet)
	{
		return extensions.find(JavaPluginExtension.class).getSourceSets().getByName(sourceSet);
	}

	private Action<Manifest> getManifest(final Project target)
	{
		return manifest ->
		{
			manifest.attributes(Configurable.configure(manifest.getAttributes(), entry ->
			{
				entry.put("Specification-Title", "Galacticraft");
				entry.put("Specification-Vendor", "TeamGalacticraft");
				entry.put("Specification-Version", target.getVersion());
				entry.put("Implementation-Title", "Galacticraft");
				entry.put("Implementation-Version", target.getVersion());
				entry.put("Implementation-Vendor", "TeamGalacticraft");
				entry.put("Implementation-Build-Date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date()));
				entry.put("FMLAT", "accesstransformer.cfg");
				entry.put("FMLCorePluginContainsFMLMod", true);
				entry.put("FMLCorePlugin", "micdoodle8.mods.miccore.MicdoodlePlugin");
				entry.put("Built-On-Minecraft", target.findProperty("mc_version"));
				entry.put("Built-On-Forge", target.findProperty("forge_version"));
				entry.put("Built-On-Mapping", target.findProperty("mapping_channel") + "-" + target.findProperty("mapping_version"));
				entry.put("Built-Using", "ForgeGradle: 5.1.+");
			}));
		};
	}

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{
		target.pluginManagement(pluginManagement ->
		{
			pluginManagement.getRepositories().maven(TeamConstants.Repositories.Groups.gradle);
		});
	}
}
