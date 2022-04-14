package net.galacticraft.gradle.convention;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;

import net.galacticraft.gradle.common.AbstractPluginTest;

public class ConventionPublishingTest extends AbstractPluginTest {

	@Test
	public void testUsingReleaseVersion() throws IOException {
		createGradleConfiguration("ConventionTest")
		.applyPlugin("java")
		.applyPlugin("net.galacticraft.gradle.dev")
		.addCustomConfigurationBlock("version='1.0.0'")
		.addCustomConfigurationBlock("group='net.galacticraft'")

		.addCustomConfigurationBlock(
				new StringBuilder()
				.append("galacticraftConvention {").append("\n")
				.append("	repository(\"GalacticGradle\") {").append("\n")
				.append("		ci(true)").append("\n")
				.append("		publishing(true)").append("\n")
				.append("	}").append("\n")
				.append("	mitLicense()").append("\n")
				.append("	licenseParameters {").append("\n")
				.append("		name = \"GalacticGradle\"").append("\n")
				.append("		organization = \"${organization}\"").append("\n")
				.append("		url = \"${url}\"").append("\n")
				.append("	}").append("\n")
				.append("}")
				.toString()
		)
		
		.addProperty("url", "https://galacticraft.net")
		.addProperty("organization", "https://galacticraft.net")
		.addProperty("GalacticSnapshotsUsername", "testing")
		.addProperty("GalacticSnapshotsPassword", "123345")
		.addProperty("GalacticReleasesUsername", "testing")
		.addProperty("GalacticReleasesPassword", "123345")
		.write();
		
        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir)	
                .withPluginClasspath()
                .forwardOutput()
                .withArguments("tasks")
                .withDebug(true)
                .build();

        assertThat(result.getOutput().contains("publishAllPublicationsToGalacticReleasesRepository")).isTrue();
        assertThat(result.getOutput().contains("publishAllPublicationToGalacticSnapshotsRepository")).isFalse();
	}
}
