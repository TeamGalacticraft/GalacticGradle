package net.galacticraft.gradle.test;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayNameGenerator;

/**
 * An extension of the standard display name generator that only uses method names for test display names.
 *
 * <p>This is better suited for test directory selection.</p>
 *
 * @since 1.0.2
 */
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
