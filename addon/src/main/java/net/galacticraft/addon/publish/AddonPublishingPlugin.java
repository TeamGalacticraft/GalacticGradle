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
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.provider.Provider;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.jvm.tasks.Jar;
import org.gradle.plugins.signing.Sign;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.addon.task.TestPublishTask;
import net.galacticraft.addon.util.ConditionalTaskCollectionBuilder;
import net.galacticraft.gradle.core.internal.SpecialMavenPlugin;
import net.galacticraft.gradle.core.internal.ext.SpecialPublishExt;
import net.galacticraft.gradle.core.model.RepositoryCredentials;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamConstants;
import net.galacticraft.gradle.core.plugin.helpers.RepositoryHelper.Metadata;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;

public class AddonPublishingPlugin extends GradlePlugin
{
	private BooleanProperty	signPublication;
	private List<Sign>		signingTasks	= null;

	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
	{
		this.signPublication = new BooleanProperty(target, "addon.publish.sign", true);

		plugins.apply(SpecialMavenPlugin.class);
		AddonPublishingExtension extension = extensions.findOrCreate("nexus", AddonPublishingExtension.class);

		TaskProvider<PublishToMavenRepository>	publishTask		= tasks.registerTask("publishMod", PublishToMavenRepository.class);
		TaskProvider<TestPublishTask>			testPublishTask	= tasks.registerTask("testModPublish", TestPublishTask.class);

		afterEval(p ->
		{
			if (signPublication.get())
			{
				plugins.apply(SigningPlugin.class);
			}

			//@noformat
			RepositoryCredentials credentials = RepositoryCredentials.create(target)
				.username(extension.getNexusUsername())
				.password(extension.getNexusPassword());
			
			String projectName = target.getName().toLowerCase().replaceAll("[^\\w\\d]", "-");
			
			// if we have no credentials lets stop while we are ahead
			if (credentials.notNull())
			{
				Jar jarTask = tasks.container().withType(Jar.class).findByName("jar");
				
				if(jarTask != null)
				{
					ConditionalTaskCollectionBuilder taskList = new ConditionalTaskCollectionBuilder(target);
					
					extensions.container().configure(SpecialPublishExt.class, publishing ->
					{
						MavenArtifactRepository repository = publishing.getRepositories().maven(common -> {
							common.setName("legacy-common");
							common.setUrl(TeamConstants.Repositories.Hosted.uriLegacyCommon);
							common.metadataSources(Metadata.ALL.set());
							common.credentials(credentials.get());
						});
						MavenPublication publication = publishing.getPublications().create(projectName, MavenPublication.class, addon -> {
							// If no file objects are provided, lets attempt to get the jars from
							// tasks that produce them. With a little bit of semi-sane logic
							if (extension.getArchives().get().isEmpty())
							{
								target.getArtifacts();

								addon.artifact(jarTask);

								// Check if the additional sources & javadoc jar taks are present
								taskList.getTasksList().forEach(task ->
								{
									if (!task.getArchiveClassifier().get().isEmpty())
									{
										addon.artifact(task, meta ->
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
								addon.artifact(f1);
								// now remove it
								archives.remove(0);

								// then we can just run forEach on the rest since they need classifiers
								archives.forEach(fn ->
								{
									if (!fn.getArchiveClassifier().get().isEmpty())
									{
										addon.artifact(fn, meta ->
										{
											meta.setClassifier(fn.getArchiveClassifier().get());
										});
									}
								});
							}

							// Set the artifactId using the supplied string via our extension if not null,
							// fallback to project's DisplayName if null
							Provider<String> artifactId = target.provider(
								() -> extension.getArtifactId().isPresent() ? extension.getArtifactId().get() : addon.getName().toLowerCase().replaceAll("[^\\w\\d]", "-"));
							addon.setArtifactId(artifactId.get());

							// Set the groupId using the supplied string via our extension if not null,
							// fallback to project's group declaration if null
							Provider<String> group = target.provider(
								() -> extension.getGroup().isPresent() ? extension.getGroup().get() : AddonPublishingExtension.DEFAULT_GROUP_NAME);
							addon.setGroupId(group.get());

							// Set the version using the supplied string via our extension if not null,
							// fallback to project's version declaration if null
							Provider<String> version = target.provider(
								() -> extension.getVersion().isPresent() ? extension.getVersion().get() : target.getVersion() == null ? null : String.valueOf(target.getVersion()));
							addon.setVersion(version.get());

							if (extension.getPom() != null)
							{
								addon.pom(extension.getPom());
							}
						});
						
						String	keyId		= variables.get("signing.keyId");
						String	password	= variables.get("signing.password");
						
						if (signPublication.get() && allNotNull(keyId, password))
						{
							SigningExtension signing = extensions.find(SigningExtension.class);
							signingTasks = signing.sign(publication);
						}
						
						testPublishTask.configure(task ->
						{
							task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
							task.getMavenRepository().set(repository);
							task.getMavenPublication().set(publication);
							task.setDescription("[Internal Maven] Prints out the Publications identity and all artifacts that would be published");
						});
						
						publishTask.configure(task ->
						{
							task.setPublication(publication);
							task.setRepository(repository);
							task.setGroup(TeamConstants.Properties.TASK_GROUP_NAME);
							task.setDescription("[Addon Publish] Publishes " + publication.getName() + " to GalacticMaven Repositpory");
							if (signingTasks != null)
							{
								task.dependsOn(signingTasks);
							}
						});
					});
				}
			}
		});
	}

	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{
	}

	private AbstractArchiveTask resolve(Object in)
	{
		if (in instanceof AbstractArchiveTask)
		{
			return ((AbstractArchiveTask) in);
		}
		else if (in instanceof TaskProvider<?>)
		{
			Object provided = ((TaskProvider<?>) in).get();

			if (provided instanceof AbstractArchiveTask)
			{
				return ((AbstractArchiveTask) provided);
			}
		}
		return null;
	}

	@SafeVarargs
	static final <T extends Object> boolean allNotNull(@Nullable T... objects)
	{
		for(T obj : objects)
		{
			if(obj == null)
			{
				return false;
			}
		}
		
		return true;
	}
}
