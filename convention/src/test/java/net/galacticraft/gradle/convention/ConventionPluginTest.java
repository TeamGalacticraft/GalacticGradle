package net.galacticraft.gradle.convention;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class ConventionPluginTest {

	private Project project;
	
    @BeforeEach
    public void setUp() {
        project = ProjectBuilder.builder().build();
    }
	
    @Test
    public void apply() {
        project.getPlugins().apply(ConventionPlugin.class);
        
        assertThat(project.getPlugins().hasPlugin(ConventionPlugin.class)).isTrue();
    }
}
