package net.galacticraft.plugins.modrinth.model;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import org.immutables.value.Generated;

/**
 * A modifiable implementation of the {@link Settings Settings} type.
 * <p>Use the constructor to create new modifiable instances. You may even extend this class to
 * add some convenience methods, however most of the methods in this class are final
 * to preserve safety and predictable invariants.
 * <p><em>ModifiableSettings is not thread-safe</em>
 */
@Generated(from = "Settings", generator = "Modifiables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.Generated({"Modifiables.generator", "Settings"})
@NotThreadSafe
class ModifiableSettings implements Settings {
  private static final long OPT_BIT_DEBUG = 0x1L;
  private static final long OPT_BIT_INCLUDE_JAVADOC_JAR = 0x2L;
  private static final long OPT_BIT_INCLUDE_SOURCES_JAR = 0x4L;
  private long optBits;

  private String apikey;
  private boolean debug;
  private boolean includeJavadocJar;
  private boolean includeSourcesJar;

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code apikey} attribute
   */
  @Override
  public final String apikey() {
    if (apikeyIsSet()) {
      return apikey;
    } else {
      return Settings.super.apikey();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code debug} attribute
   */
  @Override
  public final boolean debug() {
    if (debugIsSet()) {
      return debug;
    } else {
      return Settings.super.debug();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code includeJavadocJar} attribute
   */
  @Override
  public final boolean includeJavadocJar() {
    if (includeJavadocJarIsSet()) {
      return includeJavadocJar;
    } else {
      return Settings.super.includeJavadocJar();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code includeSourcesJar} attribute
   */
  @Override
  public final boolean includeSourcesJar() {
    if (includeSourcesJarIsSet()) {
      return includeSourcesJar;
    } else {
      return Settings.super.includeSourcesJar();
    }
  }

  /**
   * Clears the object by setting all attributes to their initial values.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableSettings clear() {
    optBits = 0;
    apikey = null;
    debug = false;
    includeJavadocJar = false;
    includeSourcesJar = false;
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Settings} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableSettings from(Settings instance) {
    Objects.requireNonNull(instance, "instance");
    if (instance instanceof ModifiableSettings) {
      from((ModifiableSettings) instance);
      return this;
    }
    apikey(instance.apikey());
    debug(instance.debug());
    includeJavadocJar(instance.includeJavadocJar());
    includeSourcesJar(instance.includeSourcesJar());
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Settings} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableSettings from(ModifiableSettings instance) {
    Objects.requireNonNull(instance, "instance");
    apikey(instance.apikey());
    debug(instance.debug());
    includeJavadocJar(instance.includeJavadocJar());
    includeSourcesJar(instance.includeSourcesJar());
    return this;
  }

  /**
   * Assigns a value to the {@code apikey} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code apikey}.</em>
   * @param apikey The value for apikey
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableSettings apikey(String apikey) {
    this.apikey = Objects.requireNonNull(apikey, "apikey");
    return this;
  }

  /**
   * Assigns a value to the {@code debug} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code debug}.</em>
   * @param debug The value for debug
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableSettings debug(boolean debug) {
    this.debug = debug;
    optBits |= OPT_BIT_DEBUG;
    return this;
  }

  /**
   * Assigns a value to the {@code includeJavadocJar} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code includeJavadocJar}.</em>
   * @param includeJavadocJar The value for includeJavadocJar
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableSettings includeJavadocJar(boolean includeJavadocJar) {
    this.includeJavadocJar = includeJavadocJar;
    optBits |= OPT_BIT_INCLUDE_JAVADOC_JAR;
    return this;
  }

  /**
   * Assigns a value to the {@code includeSourcesJar} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code includeSourcesJar}.</em>
   * @param includeSourcesJar The value for includeSourcesJar
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableSettings includeSourcesJar(boolean includeSourcesJar) {
    this.includeSourcesJar = includeSourcesJar;
    optBits |= OPT_BIT_INCLUDE_SOURCES_JAR;
    return this;
  }

  /**
   * Returns {@code true} if the default attribute {@code debug} is set.
   * @return {@code true} if set
   */
  public final boolean debugIsSet() {
    return (optBits & OPT_BIT_DEBUG) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code includeJavadocJar} is set.
   * @return {@code true} if set
   */
  public final boolean includeJavadocJarIsSet() {
    return (optBits & OPT_BIT_INCLUDE_JAVADOC_JAR) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code includeSourcesJar} is set.
   * @return {@code true} if set
   */
  public final boolean includeSourcesJarIsSet() {
    return (optBits & OPT_BIT_INCLUDE_SOURCES_JAR) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code apikey} is set.
   * @return {@code true} if set
   */
  public final boolean apikeyIsSet() {
    return apikey != null;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableSettings unsetDebug() {
    optBits |= 0;
    debug = false;
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableSettings unsetIncludeJavadocJar() {
    optBits |= 0;
    includeJavadocJar = false;
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableSettings unsetIncludeSourcesJar() {
    optBits |= 0;
    includeSourcesJar = false;
    return this;
  }

  /**
   * Returns {@code true} if all required attributes are set, indicating that the object is initialized.
   * @return {@code true} if set
   */
  public final boolean isInitialized() {
    return true;
  }

  /**
   * This instance is equal to all instances of {@code ModifiableSettings} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    if (!(another instanceof ModifiableSettings)) return false;
    ModifiableSettings other = (ModifiableSettings) another;
    return equalTo(other);
  }

  private boolean equalTo(ModifiableSettings another) {
    String apikey = apikey();
    boolean debug = debug();
    boolean includeJavadocJar = includeJavadocJar();
    boolean includeSourcesJar = includeSourcesJar();
    return apikey.equals(another.apikey())
        && debug == another.debug()
        && includeJavadocJar == another.includeJavadocJar()
        && includeSourcesJar == another.includeSourcesJar();
  }

  /**
   * Computes a hash code from attributes: {@code apikey}, {@code debug}, {@code includeJavadocJar}, {@code includeSourcesJar}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    String apikey = apikey();
    h += (h << 5) + apikey.hashCode();
    boolean debug = debug();
    h += (h << 5) + Boolean.hashCode(debug);
    boolean includeJavadocJar = includeJavadocJar();
    h += (h << 5) + Boolean.hashCode(includeJavadocJar);
    boolean includeSourcesJar = includeSourcesJar();
    h += (h << 5) + Boolean.hashCode(includeSourcesJar);
    return h;
  }

  /**
   * Generates a string representation of this {@code Settings}.
   * If uninitialized, some attribute values may appear as question marks.
   * @return A string representation
   */
  @Override
  public String toString() {
    return "ModifiableSettings{"
        + "apikey=" + apikey()
        + ", debug=" + debug()
        + ", includeJavadocJar=" + includeJavadocJar()
        + ", includeSourcesJar=" + includeSourcesJar()
        + "}";
  }
}
