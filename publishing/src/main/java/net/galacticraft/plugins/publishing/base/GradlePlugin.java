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

package net.galacticraft.plugins.publishing.base;

import javax.annotation.Nonnull;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.Convention;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;

/**
 * A more friendly interface for creating a {@link Plugin} that operates on a {@link Project}.
 *
 * <p>Implementations should override the non-deprecated overload of {@code apply}, but until {@code Convention} is removed, either will work.</p>
 *
 * @since 1.0.0
 */
public interface GradlePlugin extends Plugin<Project> {
	
	@Override
	@SuppressWarnings("deprecation") // workaround
	default void apply(final @Nonnull Project project) {
		if (GradleCompatibility.HAS_CONVENTION) {
			this.apply(project, project.getPlugins(), project.getExtensions(), project.getConvention(),
					project.getTasks());
		} else {
			this.apply(project, project.getPlugins(), project.getExtensions(), project.getTasks());
		}
	}

	/**
	 * Applies the plugin.
	 *
	 * @param project    the project
	 * @param plugins    the plugin container
	 * @param extensions the extension container
	 * @param convention the convention
	 * @param tasks      the task container
	 * @since 1.0.0
	 * @deprecated for removal since 1.1.0, use
	 *             {@link #apply(Project, PluginContainer, ExtensionContainer, TaskContainer)}
	 *             instead
	 */
	@Deprecated
	default void apply(final @Nonnull Project project, final @Nonnull PluginContainer plugins,
			final @Nonnull ExtensionContainer extensions, final @Nonnull Convention convention,
			final @Nonnull TaskContainer tasks) {
		this.apply(project, plugins, extensions, tasks);
	}

	/**
	 * Applies the plugin.
	 *
	 * @param project    the project
	 * @param plugins    the plugin container
	 * @param extensions the extension container
	 * @param tasks      the task container
	 * @since 1.1.0
	 */
	default void apply(final @Nonnull Project project, final @Nonnull PluginContainer plugins,
			final @Nonnull ExtensionContainer extensions, final @Nonnull TaskContainer tasks) {
	}
}
