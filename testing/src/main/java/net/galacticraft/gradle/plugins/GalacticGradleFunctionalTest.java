package net.galacticraft.gradle.plugins;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.kyori.mammoth.test.GradleFunctionalTest;
import net.kyori.mammoth.test.GradleParameters;
import net.kyori.mammoth.test.TestVariant;
import net.kyori.mammoth.test.TestVariantResource;

@GradleFunctionalTest
@GradleParameters({"--stacktrace"})
@TestVariant(gradleVersion = "7.4.2")
@TestVariant(gradleVersion = "7.5.1")
@TestVariantResource(value = "/injected-gradle-versions", optional = true)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
public @interface GalacticGradleFunctionalTest
{

}
