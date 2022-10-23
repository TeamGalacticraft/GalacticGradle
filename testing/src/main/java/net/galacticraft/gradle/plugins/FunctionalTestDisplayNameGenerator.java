package net.galacticraft.gradle.plugins;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayNameGenerator;

public final class FunctionalTestDisplayNameGenerator extends DisplayNameGenerator.Standard {

    @Override
    public String generateDisplayNameForMethod(final Class<?> testClass, final Method testMethod) {
        final String name = testMethod.getName();
        if (name.startsWith("test") && name.length() > 5) {
            return Character.toLowerCase(name.charAt(4)) + name.substring(5);
        } else {
            return name;
        }
    }

}