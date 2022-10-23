package net.galacticraft.addon;

import org.gradle.api.Project;
import org.gradle.internal.impldep.org.junit.Test;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Assertions;

class AddonPluginTest
{
	public static String PLUGIN_ID = "net.galacticraft.addon";
	
	@Test
	void testApplyPlugin()
	{
        final Project project = ProjectBuilder.builder().build();
        project.getPlugins().apply(PLUGIN_ID);

        Assertions.assertNotNull(project.getTasks().findByName("listAvailableVersions"));
	}
}
