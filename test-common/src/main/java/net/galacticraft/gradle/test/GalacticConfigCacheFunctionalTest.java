package net.galacticraft.gradle.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.kyori.mammoth.test.GradleParameters;

/**
 * A functional test that runs the build with the configuration cache enabled.
 */
@GradleParameters({"--warning-mode", "fail", "--stacktrace", "--configuration-cache"})
@GalacticFunctionalTestBase
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface GalacticConfigCacheFunctionalTest {
}
