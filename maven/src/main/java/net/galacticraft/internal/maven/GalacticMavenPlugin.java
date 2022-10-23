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

package net.galacticraft.internal.maven;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.jvm.tasks.Jar;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.gradle.core.internal.SpecialMavenPlugin;
import net.galacticraft.gradle.core.internal.ext.SpecialPublishExt;
import net.galacticraft.gradle.core.plugin.Configurable;
import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.TeamGC;
import net.galacticraft.gradle.core.plugin.Variables;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;
import net.galacticraft.internal.maven.pom.PomActionImpl;
import net.galacticraft.internal.maven.pom.PomIO;

public class GalacticMavenPlugin extends GradlePlugin
{
	public static final String PUBLISH_to_GALACTIC_MAVEN_TASKNAME = "publishToGalacticMaven";
	public static final String PUBLISH_TO_LOCAL_MAVEN_TASKNAME = "publishToLocalMaven";
	
	private BooleanProperty signPublication;
	private Variables variables;
	
	@Override
	protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins, @NotNull TaskContainer tasks)
	{
		this.variables = new Variables(target);
		
		plugins.apply(SpecialMavenPlugin.class);
		plugins.apply(SigningPlugin.class);
		
		extensions.findOrCreate("signing", SigningExtension.class, target);
		GalacticInternalExtension ext = extensions.findOrCreate("configureInternal", GalacticInternalExtension.class, target);

		TaskProvider<PublishToMavenRepository> publishMavenTask = tasks.register(PUBLISH_to_GALACTIC_MAVEN_TASKNAME, PublishToMavenRepository.class);
		TaskProvider<PublishToMavenRepository> publishLocalTask = tasks.register(PUBLISH_TO_LOCAL_MAVEN_TASKNAME, PublishToMavenRepository.class);
		TaskProvider<TestPublishTask> testPublishTask = tasks.register("testPublish", TestPublishTask.class);

		this.signPublication = new BooleanProperty(target, "internal.maven.sign", true);
		
		PomIO io = new PomIO(target);

		target.getGradle().afterProject(project ->
		{
			extensions.container().configure(SpecialPublishExt.class, special ->
			{
				MavenArtifactRepository repository = special.getRepositories().maven(this.configureDefaultRepository(target));
				Configurable.configureIfNonNull(repository, repo ->
				{
					if (ext.getNexusUser().isPresent() && ext.getNexusPassword().isPresent())
					{
						repo.credentials(PasswordCredentials.class, credentials ->
						{
							credentials.setUsername(ext.getNexusUser().get());
							credentials.setPassword(ext.getNexusPassword().get());
						});
					}
				});

				MavenPublication publication = special.getPublications().create(ext.getPublicationName().get(), MavenPublication.class);
				String publicationArtifactId = this.configureArtifactId(target, io);
				Configurable.configureIfNonNull(publication, pub ->
				{
					pub.setArtifactId(publicationArtifactId);
					pub.setGroupId(target.getGroup().toString());
					pub.setVersion(target.getVersion().toString());

					pub.pom(PomActionImpl.getPomAction(io));
					
					tasks.withType(Jar.class, jar -> {
						pub.artifact(jar);
					});
				});
				
				testPublishTask.configure(task -> {
					task.setGroup("galacticgradle");
					task.getMavenRepository().set(repository);
					task.getMavenPublication().set(publication);
				});

				publishMavenTask.configure(task ->
				{
					
					task.setPublication(publication);
					task.setRepository(repository);
					task.setGroup("galacticgradle");
					task.setDescription("Publishes " + ext.getPublicationName().get() + " to the Team Galacticraft Repository");
				});
				
				this.configureSigning(publishMavenTask, publication, plugins);
				
				publishLocalTask.configure(task -> {
					task.setPublication(publication);
					task.setRepository(repositories.handler().mavenLocal());
					task.setGroup("galacticgradle");
					task.setDescription("Publishes " + ext.getPublicationName().get() + " to your local Maven repository");
				});
				
				this.configureSigning(publishLocalTask, publication, plugins);
			});
		});
	}
	
	private void configureSigning(final TaskProvider<PublishToMavenRepository> task, MavenPublication publication, final PluginManager plugins)
	{
		String	keyId		= variables.getString("signing.keyId");
		String	password	= variables.getString("signing.password");
		String	keyRingFile	= variables.getString("signing.secretKeyRingFile");
		
		if(task.getName().equals(PUBLISH_TO_LOCAL_MAVEN_TASKNAME))
		{
			if (signPublication.get() && Util.allNotNull(keyId, password, keyRingFile))
			{
				
				this.setSigningConfiguration(publication, plugins, new PgpKeysWrapper(keyId, password, keyRingFile));
			}
		} else {
			Util.cantBeNull(keyId, password, keyRingFile);
			this.setSigningConfiguration(publication, plugins, new PgpKeysWrapper(keyId, password, keyRingFile));
		}
	}
	
	private void setSigningConfiguration(final MavenPublication publication, final PluginManager plugins, PgpKeysWrapper wrapper) 
	{
		extensions.container().configure(SigningExtension.class, signing -> {
			signing.useInMemoryPgpKeys(wrapper.keyId, wrapper.password, wrapper.keyRingFile);
			signing.sign(publication);
		});
	}
	
	class PgpKeysWrapper {
		
		String keyId;
		String password;
		String keyRingFile;
		
		PgpKeysWrapper(String keyId, String password, String keyRingFile)
		{
			super();
			this.keyId = keyId;
			this.password = password;
			this.keyRingFile = keyRingFile;
		}
	}
	
	private String configureArtifactId(final Project project, final PomIO io)
	{
		if(io.jsonSpecHasArtifactId())
		{
			return io.getJsonSpec().get().getArtifactId();
		} else if (project.hasProperty("mod.id"))
		{
			return project.property("mod.id").toString();
		} else if (project.hasProperty("mod_id"))
		{
			return project.property("mod_id").toString();
		} else if (project.hasProperty("modid"))
		{
			return project.property("modid").toString();
		} else
		{
			return project.getName().toLowerCase().replaceAll("[^\\w\\d]", "-");
		}
	}

	private Action<MavenArtifactRepository> configureDefaultRepository(final Project project)
	{
		boolean isSnapshotVersion = project.getVersion().toString().endsWith("SNAPSHOT");

		if (Util.containsIgnoreCase(project.getName(), "legacy"))
		{
			if (isSnapshotVersion)
			{
				return TeamGC.Repositories.Hosted.legacySnapshots;
			}

			return TeamGC.Repositories.Hosted.legacyReleases;
		}

		if (isSnapshotVersion)
		{
			return TeamGC.Repositories.Hosted.mavenSnapshots;
		}

		return TeamGC.Repositories.Hosted.mavenReleases;
	}
	


	@Override
	protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
	{

	}
}
