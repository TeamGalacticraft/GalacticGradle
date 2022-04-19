package net.galacticraft.gradle.test;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.kyori.mammoth.test.GradleParameters;

@GradleParameters({"--warning-mode", "fail", "--stacktrace"})
@GalacticFunctionalTestBase
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface GalacticFunctionalTest {
}
