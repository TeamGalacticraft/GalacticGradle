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
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.addon.publish.AddonPublishingPlugin;
import net.galacticraft.addon.task.GalacticraftVersionTask;
import net.galacticraft.addon.task.ListAvailableVersionsTask;
import net.galacticraft.addon.util.StringBuild;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamConstants;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;
import net.galacticraft.gradle.core.project.GalacticProject;
import net.galacticraft.gradle.core.util.MavenDependency;
import net.minecraftforge.gradle.common.util.MinecraftExtension;
import net.minecraftforge.gradle.userdev.DependencyManagementExtension;
import net.minecraftforge.gradle.userdev.UserDevPlugin;
import wtf.gofancy.fancygradle.FancyExtension;
import wtf.gofancy.fancygradle.FancyGradle;

public class AddonPlugin extends GradlePlugin
{
	private final GalacticProject GC_LEGACY = TeamConstants.Projects.GALACTICRAFT_LEGACY;

	private BooleanProperty	debug;
	private BooleanProperty	applyForgeGradle;
	private BooleanProperty	applyFancyGradle;
	private BooleanProperty	addDependencies;

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{
	    target.getPluginManagement().repositories(manager -> {
	        manager.maven(TeamConstants.Repositories.Groups.gradle);
	    });
	}
	
	private String getVersion()
	{
	    try
        {
            return TeamConstants.version(this).toString();
        } catch (Exception e)
        {
            return "dev";
        }
	}

	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
	{
		debug = new BooleanProperty(target, "galactic.addon.debug", false);
		applyForgeGradle = new BooleanProperty(target, "galactic.forgegradle", false);
		applyFancyGradle = new BooleanProperty(target, "galactic.fancygradle", true);
		addDependencies = new BooleanProperty(target, "galactic.dependencies", true);

		BooleanProperty publishing = new BooleanProperty(target, "galactic.addon.publish", false);
		
		if(publishing.isTrue())
		{
			plugins.apply(AddonPublishingPlugin.class);
		}
		
		conditionalLog.setConditional(debug);

		GC_LEGACY.setLogger(this.conditionalLog);

		conditionalLog.lifecycle("GalacticGradle Plugin Toolset");
		conditionalLog.lifecycle("  -> Addon Plugin: v" + this.getVersion());

		AddonExtension extension = extensions.findOrCreate("galacticraft", AddonExtension.class, target.getObjects());

		plugins.apply(JavaPlugin.class);

		extensions.find(JavaPluginExtension.class).toolchain(toolchain ->
		{
			toolchain.getLanguageVersion().set(JavaLanguageVersion.of(8));
		});

		tasks.registerTask("listAvailableVersions", ListAvailableVersionsTask.class, task ->
		{
			task.getGalacticProject().set(GC_LEGACY);
			task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
			task.setDescription("Shows all available Galacticraft-Legacy Versions");
		});

		TaskProvider<GalacticraftVersionTask> versionTask = tasks.registerTask("showVersion", GalacticraftVersionTask.class, task ->
		{
			task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
			task.setDescription("Shows the Galacticraft-Legacy Version in use");
		});

		if (applyForgeGradle.isTrue())
		{
			plugins.apply(UserDevPlugin.class);
		}

		MavenArtifactRepository repo = repositories.addMaven(TeamConstants.Repositories.Groups.legacy);
		GC_LEGACY.setRepositoryUrl(repo.getUrl());

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

		if (useLatest.isFalse() && useSnapshot.isFalse() && !extension.getVersion().isPresent())
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

			lifecycle(string.toString());
			//@format
			addGalacticraftVersionTask(task, extension);
			return false;
		}

		return true;
	}

	private void handleGradleProperties(AddonExtension extension)
	{
		if (!extension.getVersion().isPresent())
			if (extension.getUseLatestSnapshot().get().equals(true))
				if (extension.getUseLatestRelease().get().equals(true))
				{
					error("Cannot use 'latestSnapshot()' with 'latestRelease()'");
					return;
				} else
				{
					conditionalLog.lifecycle("readLatestSnapshot() = " + GC_LEGACY.readLatestSnapshot().toString());
					extension.getVersion().set(GC_LEGACY.readLatestSnapshot().toString());
				}

		if (!extension.getVersion().isPresent())
			if (extension.getUseLatestRelease().get().equals(true))
				if (extension.getUseLatestSnapshot().get().equals(true))
				{
					error("Cannot use 'latestRelease()' with 'latestSnapshot()'");
					return;
				} else
				{
					conditionalLog.lifecycle("readLatestRelease() = " + GC_LEGACY.readLatestRelease().toString());
					extension.getVersion().set(GC_LEGACY.readLatestRelease().toString());
				}
	}
	
	

	private void addGalacticraftDependency(Project target, AddonExtension extension)
	{
		if (debug.isTrue())
			conditionalLog.lifecycle("-> Using Galacticraft-Legacy Version: " + extension.getVersion().get());

		GC_LEGACY.setVersion(extension.getVersion().get());
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
			int count = GC_LEGACY.getDependencies().size();
			conditionalLog.lifecycle("-> Importing " + count + " Required Dependencies for Galacticraft");
		}
		for (MavenDependency dependency : GC_LEGACY.getDependencies())
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
