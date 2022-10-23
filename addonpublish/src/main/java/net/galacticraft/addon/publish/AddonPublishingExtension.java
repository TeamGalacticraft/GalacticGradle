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

package net.galacticraft.addon.publish;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPom;

import lombok.Getter;

@Getter
public class AddonPublishingExtension {
    
    public static String DEFAULT_GROUP_NAME = "mod.dependency";

    private Property<String> nexusUsername;
    private Property<String> nexusPassword;

    private Property<String> group;
    private Property<String> artifactId;
    private Property<String> version;

    private ListProperty<Object> archives;

    private Action<? super MavenPom> pom;

    @Inject
    public AddonPublishingExtension(final ObjectFactory factory) {
        this.nexusUsername = factory.property(String.class).convention("unset");
        this.nexusPassword = factory.property(String.class).convention("unset");
        this.group = factory.property(String.class);
        this.artifactId = factory.property(String.class);
        this.version = factory.property(String.class);
        this.archives = factory.listProperty(Object.class).empty();
    }

    public void pom(Action<? super MavenPom> configure) {
        this.pom = configure;
    }

    public void username(final String username) {
        this.getNexusUsername().set(username);
    }

    public void archives(final Object... files) {
        this.getArchives().addAll(files);
    }

    public void password(final String password) {
        this.getNexusPassword().set(password);
    }

    public void group(final String group) {
        this.getGroup().set(group);
    }

    public void artifactId(final String artifactId) {
        this.getArtifactId().set(artifactId);
    }

    public void version(final String version) {
        this.getVersion().set(version);
    }

    public boolean isUsernameSet() {
        return !this.getNexusUsername().get().equals("unset");
    }

    public String nexusUsername() {
        return this.getNexusUsername().get();
    }

    public boolean isPasswordSet() {
        return !this.getNexusPassword().get().equals("unset");
    }

    public String nexusPassword() {
        return this.getNexusPassword().get();
    }
}
