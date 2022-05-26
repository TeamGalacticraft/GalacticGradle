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

package net.galacticraft.plugins.convention;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Inject;

import org.cadixdev.gradle.licenser.LicenseExtension;
import org.gradle.api.Action;
import org.gradle.api.java.archives.Manifest;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.bundling.Jar;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.common.Constants;
import net.galacticraft.common.util.Checks;
import net.galacticraft.plugins.convention.model.Developer;
import net.kyori.indra.IndraExtension;
import net.kyori.indra.api.model.ApplyTo;

public class ConventionExtension {
	private final IndraExtension indra;
	private final LicenseExtension licenseExtension;
	private final Manifest combinedManifest;
	
	private final List<Developer> developers;
	
	@Inject
	public ConventionExtension(final IndraExtension indra, final LicenseExtension license, final JavaPluginExtension extension) {
        this.indra = indra;
        this.licenseExtension = license;
        this.combinedManifest = extension.manifest();
        this.developers = new LinkedList<Developer>();
    }
	
    /**
     * Set the repository used in Maven metadata.
     *
     * @param repoName the target repository name
     */
	public void repository(final String repoName) {
		this.indra.github(Constants.GITHUB_ORG, repoName);
	}
	
    /**
     * Set the repository used in Maven metadata.
     *
     * @param repoName the target repository name
     * @param extraConfig extra options to apply to the repository
     */
    public void repository(final String repoName, final Action<ApplyTo> extraConfig) {
        this.indra.github(Constants.GITHUB_ORG, repoName, extraConfig);
    }
    
    public void mitLicense() {
        this.indra.mitLicense();
    }

    /**
     * Get template parameters used by the license plugin for headers.
     *
     * @return the parameters
     */
    public ExtraPropertiesExtension licenseParameters() {
        return ((ExtensionAware) this.licenseExtension).getExtensions().getExtraProperties();
    }

    /**
     * Act on template parameters used by the license plugin for headers.
     *
     * @param action the action to use to configure license header properties
     */
    public void licenseParameters(final Action<ExtraPropertiesExtension> action) {
    	Checks.notNull(action, "action").execute(((ExtensionAware) this.licenseExtension).getExtensions().getExtraProperties());
    }

    public void developer(final @NotNull String id, final @NotNull String name) {
    	developer(id, name, null, null);
    }
    
    public void developer(final @NotNull String id, final @NotNull String name, final @NotNull String... roles) {
    	developer(id, name, null, roles);
    }
    
    public void developer(final @NotNull String id, final @NotNull String name, @Nullable String email) {
    	developer(id, name, email);
    }
    
    public void developer(final @NotNull String id, final @NotNull String name, @Nullable String email, @Nullable String[] roles) {
    	this.addDeveloper(developerBuilder(id, name, email, roles));
    }
    
    private Developer developerBuilder(String id, String name, String email, String[] roles) {
    	Checks.notNull(id, "id");
    	Checks.notNull(name, "name");
    	Developer.Builder builder = Developer.builder();
    	builder.id(id).name(name);
    	if(!email.isEmpty())
    		builder.email(email);
    	if(roles.length > 0)
    		builder.roles(roles);
    	
    	return builder.build();
    }
    
    private void addDeveloper(Developer developer) {
    	if(!this.developers.contains(developer)) {
    		this.developers.add(developer);
    	}
    }
    
    public List<Developer> developers() {
    	return this.developers;
    }
    
    /**
     * Get a manifest that will be included in all {@link Jar} tasks.
     *
     * <p>This allows applying project-wide identifying metadata.</p>
     *
     * @return the combined manifest
     */
    public Manifest combinedManifest() {
        return this.combinedManifest;
    }


    /**
     * Configure a manifest that will be included in all {@link Jar} tasks.
     *
     * <p>This allows applying project-wide identifying metadata.</p>
     *
     * @param configureAction action to configure with
     */
    public void combinedManifest(final Action<Manifest> configureAction) {
    	Checks.notNull(configureAction, "configureAction").execute(this.combinedManifest);
    }
}
