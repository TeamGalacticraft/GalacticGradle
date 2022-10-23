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

package net.galacticraft.addon;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.addon.task.GalacticraftVersionTask;
import net.galacticraft.addon.task.ListAvailableVersionsTask;
import net.galacticraft.addon.util.StringBuild;
import net.galacticraft.gradle.core.model.maven.MavenModel;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamGC;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;
import net.galacticraft.gradle.core.plugin.property.StringProperty;
import net.galacticraft.gradle.core.project.GalacticProject;
import net.galacticraft.gradle.core.util.MavenDependency;
import net.galacticraft.gradle.core.version.Version;
import net.minecraftforge.gradle.common.util.MinecraftExtension;
import net.minecraftforge.gradle.userdev.DependencyManagementExtension;
import net.minecraftforge.gradle.userdev.UserDevPlugin;
import wtf.gofancy.fancygradle.FancyExtension;
import wtf.gofancy.fancygradle.FancyGradle;

public class AddonPlugin extends GradlePlugin
{
	private final GalacticProject	GC_LEGACY				= TeamGC.Projects.GALACTICRAFT_LEGACY;
	private final MavenModel		GC_LEGACY_MAVEN_MODEL	= GC_LEGACY.getMavenModel();

	private BooleanProperty	debug;
	private BooleanProperty	applyForgeGradle;
	private BooleanProperty	applyFancyGradle;
	private BooleanProperty	addDependencies;

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{
		target.pluginManagement(pluginManagement ->
		{
			pluginManagement.getRepositories().maven(TeamGC.Repositories.Groups.gradle);
		});
	}

	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins, @NotNull TaskContainer tasks)
	{
		debug = new BooleanProperty(target, "galactic.addon.debug", false);
		applyForgeGradle = new BooleanProperty(target, "galactic.fg5", false);
		applyFancyGradle = new BooleanProperty(target, "galactic.fancygradle", true);
		addDependencies = new BooleanProperty(target, "galactic.dependencies", true);

		conditionalLog.setConditional(debug);

		String version;
		try
		{
			version = TeamGC.version(this).toString();
		} catch (Exception e)
		{
			version = "Development";
		}

		conditionalLog.lifecycle("GalacticGradle Plugin Toolset");
		conditionalLog.lifecycle("  -> Addon Plugin: v" + version);

		AddonExtension extension = extensions.findOrCreate("galacticraft", AddonExtension.class, target.getObjects());

		plugins.apply(JavaPlugin.class);

		extensions.find(JavaPluginExtension.class).toolchain(toolchain ->
		{
			toolchain.getLanguageVersion().set(JavaLanguageVersion.of(8));
		});

		tasks.register("listAvailableVersions", ListAvailableVersionsTask.class, task ->
		{
			task.getGalacticProject().set(GC_LEGACY);
			task.setGroup("galacticgradle");
			task.setDescription("Shows all available Galacticraft-Legacy Versions");
		});

		TaskProvider<GalacticraftVersionTask> versionTask = tasks.register("showVersion", GalacticraftVersionTask.class, task ->
		{
			task.setGroup("galacticgradle");
			task.setDescription("Shows the Galacticraft-Legacy Version");
		});

		if (applyForgeGradle.isTrue())
		{
			plugins.apply(UserDevPlugin.class);
		}
		
		MavenArtifactRepository repo = repositories.addMaven(TeamGC.Repositories.Groups.legacy);
		GC_LEGACY_MAVEN_MODEL.addRepositoryUrl(repo.getUrl());
		
		target.getGradle().afterProject(p ->
		{
			if (checkProperties(versionTask, extension))
			{
				this.handleGradleProperties(extension);
				this.addGalacticraftVersionTask(versionTask, extension);
				this.addGalacticraftDependency(target, extension);
				if (addDependencies.isTrue())
				{
					this.addRequiredCompileDependencies(target);
				}
				if (applyFancyGradle.isTrue())
				{
					this.addFancyGradlePatches(target);
				}
			}
		});
	}

	private void addGalacticraftVersionTask(TaskProvider<GalacticraftVersionTask> task, AddonExtension extension)
	{
		task.configure(action ->
		{
			action.getGalacticraftVersion().set(extension.getVersion());
		});
	}

	private boolean checkProperties(TaskProvider<GalacticraftVersionTask> task, AddonExtension extension)
	{
		BooleanProperty	useLatest	= new BooleanProperty(extension.getUseLatestRelease());
		BooleanProperty	useSnapshot	= new BooleanProperty(extension.getUseLatestSnapshot());
		StringProperty	version		= new StringProperty(extension.getVersion());

		if (useLatest.isFalse() && useSnapshot.isFalse() && version.equals(Version.Null().toString(), false))
		{
			//@noformat
			StringBuild string = StringBuild.start();
				string.appendln("#############################################################")
				.ln()
				.appendln("!! Missing Galacticraft-Legacy Version !!")
				.appendln("Provide a version using 1 of the following methods:")
				.ln()
				.appendln("galacticraft {")
				.appendln("    version('x.x.x')")
				.appendln("    latestSnapshot() // LASTEST SNAPSHOT VERSION")
				.appendln("    latestRelease()  // LASTEST RELEASE VERSION")
				.appendln("}")
				.ln()
				.appendln("#############################################################");

			conditionalLog.error(string.toString());
			//@format
			addGalacticraftVersionTask(task, extension);
			return false;
		}

		return true;
	}

	private void handleGradleProperties(AddonExtension extension)
	{
		if (!extension.getVersion().isPresent())
			if (extension.getUseLatestSnapshot().isPresent())
				if (extension.getUseLatestRelease().isPresent())
				{
					error("Cannot use 'latestSnapshot()' with 'latestRelease()'");
					return;
				} else
					extension.getVersion().set(GC_LEGACY.getMavenModel().readLatestSnapshot().toString());

		if (!extension.getVersion().isPresent())
			if (extension.getUseLatestRelease().isPresent())
				if (extension.getUseLatestSnapshot().isPresent())
				{
					error("Cannot use 'latestRelease()' with 'latestSnapshot()'");
					return;
				} else
					extension.getVersion().set(GC_LEGACY.getMavenModel().readLatestRelease().toString());
	}

	private void addGalacticraftDependency(Project target, AddonExtension extension)
	{
		if (debug.isTrue())
			conditionalLog.lifecycle("-> Using Galacticraft-Legacy Version: " + extension.getVersion().get());

		GC_LEGACY.getMavenModel().setVersion(extension.getVersion().get());
		DependencySet		dependencySet		= target.getConfigurations().getByName(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME).getAllDependencies();
		Dependency			gcDependencyDeobf	= target.getDependencies().create(GC_LEGACY.toDependencyNotation() + ":deobf");
		MinecraftExtension	minecraft			= extensions.find(MinecraftExtension.class);

		if (minecraft.getMappingChannel().get().equals("stable") && minecraft.getMappingVersion().get().equals("39-1.12"))
		{
			if (!dependencySet.contains(gcDependencyDeobf))
			{
				conditionalLog.lifecycle("-> Mappings & Version match 'stable_39-1.12'. Importing :debof Dependency");

				target.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, gcDependencyDeobf);
			}

		} else
		{
			if (!dependencySet.contains(toDeobf(GC_LEGACY.toDependencyNotation())))
			{
				conditionalLog.lifecycle("-> Mappings & Version do not match 'stable_39-1.12'. Importing Non :debof Dependency");

				target.getDependencies().add(JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME, toDeobf(GC_LEGACY.toDependencyNotation()));
			}
		}
	}

	private void addRequiredCompileDependencies(Project target)
	{
		DependencySet dependencySet = target.getConfigurations().getByName(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME).getAllDependencies();
		if (debug.isTrue())
		{
			int count = GC_LEGACY.getMavenModel().getDependencies().size();
			conditionalLog.lifecycle("-> Importing " + count + " Required Dependencies for Galacticraft");
		}
		for (MavenDependency dependency : GC_LEGACY.getMavenModel().getDependencies())
		{
			Dependency compileDep = target.getDependencies().create(dependency.toDependencyNotation());
			if (!dependencySet.contains(compileDep))
			{
				conditionalLog.lifecycle("  -> Importing: " + dependency.getArtifactId() + ":" + dependency.getVersion());

				target.getDependencies().add(JavaPlugin.COMPILE_ONLY_CONFIGURATION_NAME, compileDep);
			}
		}
	}

	private Dependency toDeobf(Object dependency)
	{
		return extensions.find(DependencyManagementExtension.class).deobf(dependency);
	}

	private void addFancyGradlePatches(Project target)
	{
		target.getPluginManager().apply(FancyGradle.class);
		extensions.find(FancyExtension.class).patches(patch ->
		{
			patch.getCoremods();
			patch.getResources();
			patch.getAsm();
			patch.getMergetool();
		});
	}
}
