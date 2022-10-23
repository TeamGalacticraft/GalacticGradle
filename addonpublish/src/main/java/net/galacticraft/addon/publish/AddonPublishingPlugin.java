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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.jvm.tasks.Jar;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.addon.publish.task.PublishAddonToNexus;
import net.galacticraft.addon.publish.util.ConditionalTaskCollectionBuilder;
import net.galacticraft.gradle.core.internal.SpecialMavenPlugin;
import net.galacticraft.gradle.core.internal.ext.SpecialPublishExtension;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;

public class AddonPublishingPlugin extends GradlePlugin
{

	private Optional<MavenPublication>			modPublication		= Optional.empty();
	private Optional<MavenArtifactRepository>	modMavenRepository	= Optional.empty();

	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins, @NotNull TaskContainer tasks)
	{
		plugins.apply(SpecialMavenPlugin.class);
		AddonPublishingExtension extension = extensions.findOrCreate("nexus", AddonPublishingExtension.class);

		target.afterEvaluate(project ->
		{

			BooleanProperty debugProvider = new BooleanProperty(project, "nexus.debug");

			if (project.hasProperty("nexusUsername"))
			{
				extension.username((String) project.property("nexusUsername"));
			}
			if (project.hasProperty("nexusPassword"))
			{
				extension.password((String) project.property("nexusPassword"));
			}

			ConditionalTaskCollectionBuilder taskList = new ConditionalTaskCollectionBuilder(project);

			TaskProvider<Jar> jarTask = tasks.withType(Jar.class).named("jar");

			// if we have no credentials and at the very least the jar task, lets stop while
			// we are ahead and cry
			if (doWeContinue(extension, jarTask))
			{
				project.getExtensions().configure(SpecialPublishExtension.class, publishing ->
				{

					modPublication = Optional.of(publishing.getPublications().register(cleanName(project.getName()), MavenPublication.class, maven ->
					{

						// If no file objects are provided, lets attempt to get the jars from
						// tasks that produce them. With a little bit of semi-sane logic
						if (extension.getArchives().get().isEmpty())
						{
							
							project.getArtifacts();

							maven.artifact(jarTask.get().getArchiveFile());

							// Check if the additional sources & javadoc jar taks are present
							taskList.getTasksList().forEach(task ->
							{
								if (!task.getArchiveClassifier().get().isEmpty())
								{
									maven.artifact(task.getArchiveFile(), meta ->
									{
										meta.setClassifier(task.getArchiveClassifier().get());
									});
								}
							});

							// If they were provided, then yay, lets just work with those while
							// ensuring additional archives have classifiers so maven doesn't
							// start throwing a fit and cry because maven
						} else
						{
							List<AbstractArchiveTask> archives = new ArrayList<>(extension.getArchives().get().size());
							archives.addAll(extension.getArchives().get().stream().map(o -> resolve(o)).collect(Collectors.toList()));

							// grab first archive
							AbstractArchiveTask f1 = archives.get(0);
							maven.artifact(f1.getArchiveFile());
							// now remove it
							archives.remove(0);

							// then we can just run forEach on the rest since they need classifiers
							archives.forEach(fn ->
							{
								if (!fn.getArchiveClassifier().get().isEmpty())
								{
									maven.artifact(fn.getArchiveFile(), meta ->
									{
										meta.setClassifier(fn.getArchiveClassifier().get());
									});
								}
							});
						}

						// Set the artifactId using the supplied string via our extension if not null,
						// fallback to project's DisplayName if null
						Provider<String> artifactId = project.provider(
							() -> extension.getArtifactId().isPresent() ? extension.getArtifactId().get() : project.getName() == null ? null : cleanName(project.getName()));
						maven.setArtifactId(artifactId.get());

						// Set the groupId using the supplied string via our extension if not null,
						// fallback to project's group declaration if null
						Provider<String> group = project
							.provider(() -> extension.getGroup().isPresent() ? extension.getGroup().get() : AddonPublishingExtension.DEFAULT_GROUP_NAME);
						maven.setGroupId(group.get());

						// Set the version using the supplied string via our extension if not null,
						// fallback to project's version declaration if null
						Provider<String> version = project.provider(
							() -> extension.getVersion().isPresent() ? extension.getVersion().get() : project.getVersion() == null ? null : String.valueOf(project.getVersion()));
						maven.setVersion(version.get());

						if (extension.getPom() != null)
						{
							maven.pom(extension.getPom());
						}

					}).get());

					publishing.repositories(handler ->
					{
						modMavenRepository = Optional.of(handler.maven(repo ->
						{
							repo.setName("CommonMaven");
							repo.setUrl("https://repo.galacticraft.net/repository/maven-common/");
							repo.metadataSources(meta ->
							{
								meta.mavenPom();
								meta.artifact();
								meta.gradleMetadata();
							});
							repo.credentials(PasswordCredentials.class, credential ->
							{
								credential.setUsername(extension.nexusUsername());
								credential.setPassword(extension.nexusPassword());
							});
						}));
					});
				});
			} else
			{
				lifecycle("[GalacticGradle] NexusPublishing Plugin Disabled");
				lifecycle("[GalacticGradle] Reason: No credentials were provided to use for setup");
			}

			if (modPublication.isPresent() && modMavenRepository.isPresent())
			{
				tasks.register("publishMod", PublishAddonToNexus.class, task ->
				{
					task.getIsDebug().set(debugProvider.get());
					task.setPublication(modPublication.get());
					task.setRepository(modMavenRepository.get());
					task.setGroup("GalacticGradle");
					task.setDescription("Publishes the mod artifacts to MavenCommon Repository");
				});
			}
		});
	}

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{
		// TODO Auto-generated method stub

	}

	private AbstractArchiveTask resolve(Object in)
	{
		// Grabs the file from an archive task. Allows build scripts to do things like
		// the jar task directly.
		if (in instanceof AbstractArchiveTask)
		{
			return ((AbstractArchiveTask) in);
		}
		// Grabs the file from an archive task wrapped in a provider. Allows Kotlin DSL
		// buildscripts to also specify
		// the jar task directly, rather than having to call #get() before running.
		else if (in instanceof TaskProvider<?>)
		{
			Object provided = ((TaskProvider<?>) in).get();

			// Check to see if the task provided is actually an AbstractArchiveTask.
			if (provided instanceof AbstractArchiveTask)
			{
				return ((AbstractArchiveTask) provided);
			}
		}
		return null;
	}

	private String cleanName(String name)
	{
		return name.replaceAll("[-_\\+\\.\\\\\\/~\\!@\\#\\$\\%\\^&\\*\\(\\)\\{\\}\\[\\]']", "").trim();
	}

	private boolean doWeContinue(AddonPublishingExtension extension, TaskProvider<?> task)
	{
		return extension.isUsernameSet() && extension.isPasswordSet() && task.isPresent();
	}
}
