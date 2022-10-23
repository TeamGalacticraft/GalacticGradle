package net.galacticraft.addon;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.gradle.testkit.runner.BuildResult;
import org.junit.jupiter.api.DisplayName;

import net.galacticraft.gradle.plugins.GalacticGradleFunctionalTest;
import net.kyori.mammoth.test.TestContext;

class AddonFunctionalTest
{
	@DisplayName("simplebuild")
	@GalacticGradleFunctionalTest
	void testSimpleBuild(final TestContext ctx) throws IOException
	{
		ctx.copyInput("build.gradle");
		ctx.copyInput("gradle.properties");
		ctx.copyInput("settings.gradle");
		
		final BuildResult result = ctx.runner().withArguments("showVersion").build();

		assertTrue(result.getOutput().contains("0.0.0"));
	}
	
	@DisplayName("releaseVersion")
	@GalacticGradleFunctionalTest
	void testReleaseVersion(final TestContext ctx) throws IOException
	{
		ctx.copyInput("build.gradle");
		ctx.copyInput("gradle.properties");
		ctx.copyInput("settings.gradle");
		
		final BuildResult result = ctx.runner().withArguments("showVersion").build();

		assertTrue(result.getOutput().contains("4.0.3"));
	}
}
