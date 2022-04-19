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
		.addProperty("TeamGCUsername", "testing")
		.addProperty("TeamGCPassword", "123345")
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
