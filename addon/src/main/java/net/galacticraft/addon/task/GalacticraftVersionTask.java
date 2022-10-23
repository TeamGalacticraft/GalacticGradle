package net.galacticraft.addon.task;

import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

public abstract class GalacticraftVersionTask extends DefaultTask
{
	@Input
	public abstract Property<String> getGalacticraftVersion();

	@TaskAction
	public void run()
	{
		if (getGalacticraftVersion().isPresent())
		{
			String setVersion = getGalacticraftVersion().get();
			this.getLogger().lifecycle(setVersion);
		} else
		{
			this.getLogger().lifecycle("unspecified");
		}
	}
}
