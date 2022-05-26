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

package net.galacticraft.plugins.addon;

import org.gradle.api.GradleException;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.jvm.toolchain.JavaLanguageVersion;
import org.gradle.plugins.ide.eclipse.EclipsePlugin;
import org.gradle.plugins.ide.eclipse.model.EclipseModel;
import org.gradle.plugins.ide.idea.IdeaPlugin;

import net.galacticraft.common.Constants;
import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.common.plugins.GradleVersionUtil;
import net.galacticraft.plugins.addon.ext.AddonExtension;
import net.galacticraft.plugins.addon.ext.current.CurrentAddonExtension;
import net.galacticraft.plugins.addon.ext.legacy.LegacyAddonExtension;
import net.galacticraft.plugins.addon.task.ListAvailableVersionsTask;
import net.galacticraft.plugins.addon.util.ForgeGradle;
import net.galacticraft.plugins.addon.util.XMLParse;
import net.galacticraft.plugins.addon.util.XMLUrl;
import net.minecraftforge.gradle.userdev.DependencyManagementExtension;
import wtf.gofancy.fancygradle.FancyExtension;
import wtf.gofancy.fancygradle.FancyGradle;

public class AddonPlugin extends GradlePlugin {

	public static String GC_CONFIGURATION_NAME = "galacticraft";
	public static String GC_EXTENSION_NAME = "galacticraft";

	private AddonExtension<?> addonExtension;

	@Override
	public void plugin() {

		lifecycle("GalacticGradle Plugin Toolset Version " + Constants.VERSION.string());

		applyPlugin(JavaPlugin.class);
		applyPlugin(FancyGradle.class);
		applyPlugin(EclipsePlugin.class);
		applyPlugin(IdeaPlugin.class);

		if (GradleVersionUtil.currentGradleVersionIsGreaterOrEqualThan("6.0")) {
			addonExtension = extensions().findOrCreate(AddonPlugin.GC_EXTENSION_NAME, CurrentAddonExtension.class,
					project().getObjects());
		} else {
			debug("Detected older Gradle version, Implementing LegacyAddonExtension");
			addonExtension = extensions().findOrCreate(AddonPlugin.GC_EXTENSION_NAME, LegacyAddonExtension.class);
		}

		extensions().find(EclipseModel.class).classpath(cp -> {
			cp.setDownloadSources(true);
			cp.setDownloadJavadoc(true);
		});

		addRepositories();

		extensions().find(JavaPluginExtension.class).toolchain(toolchain -> {
			toolchain.getLanguageVersion().set(JavaLanguageVersion.of(8));
		});

		extensions().find(FancyExtension.class).patches(patch -> {
			patch.getCoremods();
			patch.getResources();
		});

		registerTask("listAvailableVersions", ListAvailableVersionsTask.class, task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Shows all available GC-Legacy Versions");
		});

		project().afterEvaluate(p -> {
			this.handleProperties();
			this.addGalacticraftDependency(p);

		});
	}

	private void addRepositories() {
		repositories().addMaven("galacticraft-common", Constants.Repositories.MAVEN_COMMON).content(c -> {
			c.includeModule("net.minecraftforge", "legacydev");
		});

		repositories().addMaven(Constants.Repositories.MAVEN);
	}

	private void handleProperties() {
		if (this.addonExtension.get() instanceof CurrentAddonExtension) {
			handleGradleProperties();
		} else {
			handleLegacyGradleProperties();
		}
	}

	private void handleGradleProperties() {
		CurrentAddonExtension extension = (CurrentAddonExtension) this.addonExtension.get();

		if (extension.getUseLatestRelease().isPresent()) {
			if (extension.getGcVersion().isPresent()) {
				throw new GradleException("useLatestRelease() cannot be used with 'gcVersion'");
			}
			if (extension.getUseLatestSnapshot().isPresent()) {
				throw new GradleException("useLatestRelease() cannot be used with useLatestSnapshot()");
			}
			String latestRelease = XMLParse.readLatestRelease(XMLUrl.LEGACY_RELEASE);
			extension.gcVersion(latestRelease);
		} else if (extension.getUseLatestSnapshot().isPresent()) {
			if (extension.getGcVersion().isPresent()) {
				throw new GradleException("useLatestSnapshot() cannot be used with 'gcVersion'");
			}
			if (extension.getUseLatestRelease().isPresent()) {
				throw new GradleException("useLatestSnapshot() cannot be used with useLatestRelease()");
			}
			String latestSnapshot = XMLParse.readLatestVersion(XMLUrl.LEGACY_SNAPSHOTS);
			extension.gcVersion(latestSnapshot);
		}

		if (!extension.isGcVersionSet()) {
			throw new GradleException("Galcticraft version has not been specified");
		}

	}

	private void handleLegacyGradleProperties() {
		LegacyAddonExtension extension = (LegacyAddonExtension) this.addonExtension.get();

		if (extension.getUseLatestRelease()) {
			if (extension.isGcVersionSet()) {
				throw new GradleException("useLatestRelease() cannot be used with 'gcVersion'");
			}
			if (extension.getUseLatestSnapshot()) {
				throw new GradleException("useLatestRelease() cannot be used with useLatestSnapshot()");
			}
			String latestRelease = XMLParse.readLatestRelease(XMLUrl.LEGACY_RELEASE);
			extension.gcVersion(latestRelease);
		} else if (extension.getUseLatestSnapshot()) {
			if (extension.isGcVersionSet()) {
				throw new GradleException("useLatestSnapshot() cannot be used with 'gcVersion'");
			}
			if (extension.getUseLatestRelease()) {
				throw new GradleException("useLatestSnapshot() cannot be used with useLatestRelease()");
			}
			String latestSnapshot = XMLParse.readLatestVersion(XMLUrl.LEGACY_SNAPSHOTS);
			extension.gcVersion(latestSnapshot);
		}

		if (!extension.isGcVersionSet()) {
			throw new GradleException("Galcticraft version has not been specified");
		}

	}

	private void addGalacticraftDependency(Project project) {
		final NamedDomainObjectProvider<Configuration> galacticraft = configurations()
				.register("galacticraft");
		if (addonExtension.getVersion().isPresent()) {
			final String version = addonExtension.getVersion().get();
			lifecycle(version);
			switch (ForgeGradle.getVersion(project())) {
			case VERSION_2:
				galacticraft.configure(config -> {
					config.defaultDependencies(deps -> {
						deps.add(this.createGalacticraftDependency(version));
					});
				});

				project().getConfigurations().named("deobfCompile")
						.configure(config -> config.extendsFrom(galacticraft.get()));
				break;
			case VERSION_5:
				Dependency dep = project.getExtensions().getByType(DependencyManagementExtension.class)
						.deobf(createGalacticraftDependency(version));
				project.getDependencies().add("implementation", dep);
				break;
			default:
				break;
			}
		}
	}

	private String getDependencyString(String version) {
		return Constants.Dependencies.GC_LEGACY + ":" + version;
	}

	private Dependency createGalacticraftDependency(String version) {
		return project().getDependencies().create(getDependencyString(version));
	}
}
