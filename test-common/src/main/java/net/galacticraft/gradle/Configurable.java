package net.galacticraft.gradle;

import org.gradle.api.Action;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public final class Configurable {
	  private Configurable() {
	  }

	  /**
	   * Apply a configuration action to an instance and return it.
	   *
	   * @param instance the instance to configure
	   * @param configureAction the action to configure with
	   * @param <T> type being configured
	   * @return the provided {@code instance}
	   * @since 1.0.0
	   */
	  public static <T> @NotNull T configure(final @NotNull T instance, final @NotNull Action<T> configureAction) {
	    requireNonNull(configureAction, "configureAction").execute(instance);
	    return instance;
	  }

	  /**
	   * Configure the instance if an action is provided, otherwise pass it through.
	   *
	   * @param instance the instance to configure
	   * @param configureAction the action to configure with
	   * @param <T> type being configured
	   * @return the provided {@code instance}
	   * @since 1.0.0
	   */
	  public static <T> @NotNull T configureIfNonNull(final @NotNull T instance, final @Nullable Action<T> configureAction) {
	    if(configureAction != null) {
	      configureAction.execute(instance);
	    }
	    return instance;
	  }
}
