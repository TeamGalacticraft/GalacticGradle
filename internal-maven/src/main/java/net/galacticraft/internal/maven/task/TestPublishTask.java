package net.galacticraft.internal.maven.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenArtifact;
import org.gradle.api.publish.maven.MavenArtifactSet;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class TestPublishTask extends DefaultTask
{

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
		
		log("| Artifacts To Upload");
		log("| -------------------");
		for(MavenArtifact entry : artifacts)
		{
			log("| [{}]", entry.getFile().getName());
			log("|   > Classifer:   {}", entry.getClassifier());
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
}
