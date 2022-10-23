package net.galacticraft.internal.maven;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenArtifact;
import org.gradle.api.publish.maven.MavenArtifactSet;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

public abstract class TestPublishTask extends DefaultTask
{

	private final TaskContainer tasks = this.getProject().getTasks();

	@Input
	public abstract Property<MavenArtifactRepository> getMavenRepository();

	@Input
	public abstract Property<MavenPublication> getMavenPublication();

	@TaskAction
	public void runTest()
	{
		MavenArtifactRepository repository = this.getMavenRepository().get();
		log("| ");
		log("| Maven Repository");
		log("| ----------------");
		log("| URL:	{}", repository.getUrl());
		
		MavenPublication publication = this.getMavenPublication().get();
		log("| ");
		log("| Publication Identity");
		log("| --------------------");
		log("| ArtifactID:	{}", publication.getArtifactId());
		log("| GroupID:	{}", publication.getGroupId());
		log("| Version:	{}", publication.getVersion());
		log("| ");

		MavenArtifactSet artifacts = publication.getArtifacts();
		final Map<MavenArtifact, Task>	artifactToTaskMap = this.getArtifactToTaskMap(artifacts);
		
		log("| Artifacts To Upload");
		log("| -------------------");
		for(Entry<MavenArtifact, Task> entry : artifactToTaskMap.entrySet())
		{
			log("| [{}]", entry.getKey().getFile().getName());
			log("|   > From Task:   {}", entry.getValue().getName());
			log("|   > Classifer:   {}", entry.getKey().getClassifier());
			log("| ");
		}
	}

	private void log(String m, Object... args)
	{
		for (Object arg : args)
		{
			if (arg != null)
			{
				if (arg instanceof Property<?>)
				{
					Property<?> property = (Property<?>) arg;
					if (property.isPresent())
						this.getLogger().lifecycle(m, property.get());
				} else
				{
					this.getLogger().lifecycle(m, args);
				}
			}
		}
	}

	private void log(String m)
	{
		this.getLogger().lifecycle(m);
	}

	private Map<MavenArtifact, Task> getArtifactToTaskMap(MavenArtifactSet artifacts)
	{
		final Map<MavenArtifact, Task>	taskMap			= new LinkedHashMap<>();

		for (MavenArtifact artifact : artifacts)
		{
			for (Task task : this.tasks.withType(Jar.class))
			{
				String artifactName = artifact.getFile().getName();
				tasks.named(task.getName(), Jar.class, jarTask ->
				{
					if (jarTask.getArchiveFileName().get().equals(artifactName))
					{
						taskMap.put(artifact, jarTask);
					}
				});
			}
		}
		
		return taskMap;
	}
}
