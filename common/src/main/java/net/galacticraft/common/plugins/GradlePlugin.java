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

package net.galacticraft.common.plugins;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.ConfigurationContainer;
import org.gradle.api.logging.Logger;
import org.gradle.api.plugins.AppliedPlugin;
import org.gradle.api.plugins.PluginContainer;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.galacticraft.common.plugins.helpers.Extensions;
import net.galacticraft.common.plugins.helpers.Repositories;

/**
 * Base abstract class for Plugin classes to extend that provides commonly used
 * methods and classes. Can help simply the readability of plugins code-wise
 * 
 * @author ROMVoid95
 *
 */
@Accessors(fluent = true)
@NoArgsConstructor
public abstract class GradlePlugin implements PluginInterface {

	Class<? extends GradlePlugin> pluginClass;

	@Getter
	private Project project;
	private Logger logger;
	@Getter
	private PluginContainer plugins;
	@Getter
	private TaskContainer tasks;
	@Getter
	private Extensions extensions;
	@Getter
	private Repositories repositories;
	@Getter
	private ConfigurationContainer configurations;

	/**
	 * Calls the apply method from PluginInterface, then calls the abstract plugin()
	 * method
	 *
	 * @param project the project
	 */
	@Override
	public void apply(Project project) {
		this.project = project;
		this.plugins = project.getPlugins();
		this.tasks = project.getTasks();
		this.configurations = project.getConfigurations();
		this.extensions = new Extensions(project);
		this.repositories = new Repositories(project);
		this.logger = project.getLogger();
		
        project.afterEvaluate(new Action<Project>() {
            @Override
            public void execute(Project project)
            {
                // dont continue if its already failed!
                if (project.getState().getFailure() != null)
                    return;

                afterEvaluate();
            }
        });
		
		plugin();
	}

    protected void afterEvaluate()
    {
    	
    }
	
	/**
	 * Replaces the method
	 * 
	 * <pre>
	 *  void apply(Project project)
	 * </pre>
	 */
	public abstract void plugin();

	/**
	 * Apply plguin.
	 *
	 * @param <T>  the generic type
	 * @param type the type
	 */
	protected <T extends Plugin<?>> T applyPlugin(Class<T> type) {
		return this.plugins.hasPlugin(type) ? null : this.plugins.apply(type);
	}
	
	protected <T extends GradlePlugin> T applyGradlePlugin(Class<T> type) {
		return this.plugins.hasPlugin(type) ? null : this.plugins.apply(type);
	}

	/**
	 * Apply plguin.
	 *
	 * @param <T>      the generic type
	 * @param pluginId the plugin id
	 */
	protected <T extends Plugin<?>> void applyPlguin(String pluginId) {
		this.plugins.apply(pluginId);
	}

	protected AppliedPlugin findPlugin(String pluginId) {
		return this.project.getPluginManager().findPlugin(pluginId);
	}

	/**
	 * Register task.
	 *
	 * @param name the name
	 * @return the task provider
	 */
	protected TaskProvider<Task> registerTask(String name) {
		return this.tasks.register(name);
	}

	/**
	 * Register task.
	 *
	 * @param <T>  the generic type
	 * @param name the name
	 * @param type the type
	 * @return the task provider
	 */
	protected <T extends Task> TaskProvider<T> registerTask(String name, Class<T> type) {
		return this.tasks.register(name, type);
	}

	protected DefaultTask makeTask(String name)
    {
        return makeTask(name, DefaultTask.class);
    }

	protected DefaultTask maybeMakeTask(String name)
    {
        return maybeMakeTask(name, DefaultTask.class);
    }

	protected <T extends Task> T makeTask(String name, Class<T> type)
    {
        return makeTask(project, name, type);
    }

	protected <T extends Task> T maybeMakeTask(String name, Class<T> type)
    {
        return maybeMakeTask(project, name, type);
    }

	protected static <T extends Task> T maybeMakeTask(Project proj, String name, Class<T> type)
    {
        return (T) proj.getTasks().maybeCreate(name, type);
    }

	protected static <T extends Task> T makeTask(Project proj, String name, Class<T> type)
    {
        return (T) proj.getTasks().create(name, type);
    }
	
	/**
	 * Register task.
	 *
	 * @param <T>                 the generic type
	 * @param name                the name
	 * @param type                the type
	 * @param configurationAction the configuration action
	 * @return the task provider
	 */
	protected <T extends Task> TaskProvider<T> registerTask(String name, Class<T> type,
			Action<? super T> configurationAction) {
		return this.tasks.register(name, type, configurationAction);
	}

	/**
	 * Register task.
	 *
	 * @param name                the name
	 * @param configurationAction the configuration action
	 * @return the task provider
	 */
	protected TaskProvider<Task> registerTask(String name, Action<? super Task> configurationAction) {
		return this.tasks.register(name, configurationAction);
	}

	/**
	 * Register task.
	 *
	 * @param <T>             the generic type
	 * @param name            the name
	 * @param type            the type
	 * @param constructorArgs the constructor args
	 * @return the task provider
	 */
	protected <T extends Task> TaskProvider<T> registerTask(String name, Class<T> type, Object... constructorArgs) {
		return this.tasks.register(name, type, constructorArgs);
	}

	protected boolean checkForProperty(String propertyName) {
		return project.hasProperty(propertyName);
	}

	protected Boolean booleanProperty(String propertyName, String... object) {
		return (Boolean) project.findProperty(propertyName);
	}

	protected String stringProperty(String propertyName, Boolean... obj) {
		return (String) project.findProperty(propertyName);
	}

	protected void lifecycle(String format, Object... args) {
		this.logger.lifecycle(format, args);
	}

	protected void lifecycle(String message) {
		this.logger.lifecycle(message);
	}

	protected void debug(String format, Object... args) {
		this.logger.debug(format, args);
	}

	protected void debug(String message) {
		this.logger.debug(message);
	}

	protected void error(String format, Object... args) {
		this.logger.error(format, args);
	}

	protected void error(String message) {
		this.logger.error(message);
	}

	protected void info(String format, Object... args) {
		this.logger.info(format, args);
	}

	protected void info(String message) {
		this.logger.info(message);
	}
}
