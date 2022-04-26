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

import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.plugins.JavaLibraryPlugin;

import net.galacticraft.plugins.addon.util.ForgeGradle;
import net.minecraftforge.gradle.userdev.DependencyManagementExtension;

public class AddonPlugin implements Plugin<Project> {

    private AddonExtension addonExtension;
	
	private @MonotonicNonNull Project project;

	@Override
    public void apply(final Project project) {
        this.project = project;
        
        project.getLogger().lifecycle("Galacticraft Addon 'GRADLE' Toolset Version " + Constants.VERSION.string());
        project.getPlugins().apply(JavaLibraryPlugin.class);
        
        addonExtension = project.getExtensions().create("addon", AddonExtension.class);
        
        project.getRepositories().maven(maven -> {
        	maven.setName("galacticraft");
        	maven.setUrl(Constants.Repositories.GALACTICRAFT);
        	maven.metadataSources(m -> {
				m.gradleMetadata();
				m.mavenPom();
				m.artifact();
			});
        });
        
        this.project.afterEvaluate(set -> this.addGalacticraftDependency());
    }

	private void addGalacticraftDependency() {
		final NamedDomainObjectProvider<Configuration> galacticraft = this.project.getConfigurations()
				.register("galacticraft", config -> config.setVisible(true));

		switch (ForgeGradle.getVersion(this.project)) {
		case VERSION_2:
				galacticraft.configure(config -> config.defaultDependencies(deps -> {
					if (addonExtension.getGalacticraftVersion().isPresent()) {
						final String version = addonExtension.getGalacticraftVersion().get();
						deps.add(this.project.getDependencies().create(Constants.Dependencies.GC_LEGACY + ":" + version));
					}
				}));
				this.project.getConfigurations().named("deobfCompile")
						.configure(config -> config.extendsFrom(galacticraft.get()));
			break;
		case VERSION_5:
				final String version = addonExtension.getGalacticraftVersion().get();
				Dependency dep = project.getExtensions().getByType(DependencyManagementExtension.class)
				.deobf(project.getDependencies().create(Constants.Dependencies.GC_LEGACY + ":" + version));
				project.getDependencies().add("implementation", dep);
			break;
		default:
			break;
		}

	}
}
