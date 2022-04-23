package net.galacticraft.plugins.modrinth.model;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import net.galacticraft.plugins.modrinth.model.type.LoaderType;
import net.galacticraft.plugins.modrinth.model.type.VersionType;
import org.immutables.value.Generated;

/**
 * A modifiable implementation of the {@link Modrinth Modrinth} type.
 * <p>Use the constructor to create new modifiable instances. You may even extend this class to
 * add some convenience methods, however most of the methods in this class are final
 * to preserve safety and predictable invariants.
 * <p><em>ModifiableModrinth is not thread-safe</em>
 */
@Generated(from = "Modrinth", generator = "Modifiables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.Generated({"Modifiables.generator", "Modrinth"})
@NotThreadSafe
class ModifiableModrinth implements Modrinth {
  private static final long OPT_BIT_DEPENDENCIES = 0x1L;
  private static final long OPT_BIT_GAME_VERSIONS = 0x2L;
  private static final long OPT_BIT_LOADERS = 0x4L;
  private static final long OPT_BIT_ADDITIONAL_FILES = 0x8L;
  private long optBits;

  private String projectId;
  private String versionNumber;
  private String changelog;
  private ArrayList<Dependency> dependencies = null;
  private ArrayList<String> gameVersions = null;
  private VersionType versionType;
  private ArrayList<LoaderType> loaders = null;
  private ArrayList<String> additionalFiles = null;

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code projectId} attribute
   */
  @Override
  public final String projectId() {
    if (projectIdIsSet()) {
      return projectId;
    } else {
      return Modrinth.super.projectId();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code versionNumber} attribute
   */
  @Override
  public final String versionNumber() {
    if (versionNumberIsSet()) {
      return versionNumber;
    } else {
      return Modrinth.super.versionNumber();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code changelog} attribute
   */
  @Override
  public final String changelog() {
    if (changelogIsSet()) {
      return changelog;
    } else {
      return Modrinth.super.changelog();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code dependencies} attribute
   */
  @Override
  public final List<Dependency> dependencies() {
    if (dependenciesIsSet()) {
      return dependencies;
    } else {
      return Modrinth.super.dependencies();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code gameVersions} attribute
   */
  @Override
  public final List<String> gameVersions() {
    if (gameVersionsIsSet()) {
      return gameVersions;
    } else {
      return Modrinth.super.gameVersions();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code versionType} attribute
   */
  @Override
  public final VersionType versionType() {
    if (versionTypeIsSet()) {
      return versionType;
    } else {
      return Modrinth.super.versionType();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code loaders} attribute
   */
  @Override
  public final List<LoaderType> loaders() {
    if (loadersIsSet()) {
      return loaders;
    } else {
      return Modrinth.super.loaders();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code additionalFiles} attribute
   */
  @Override
  public final List<String> additionalFiles() {
    if (additionalFilesIsSet()) {
      return additionalFiles;
    } else {
      return Modrinth.super.additionalFiles();
    }
  }

  /**
   * Clears the object by setting all attributes to their initial values.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth clear() {
    optBits = 0;
    projectId = null;
    versionNumber = null;
    changelog = null;
    if (dependencies != null) {
      dependencies.clear();
    }
    if (gameVersions != null) {
      gameVersions.clear();
    }
    versionType = null;
    if (loaders != null) {
      loaders.clear();
    }
    if (additionalFiles != null) {
      additionalFiles.clear();
    }
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Modrinth} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableModrinth from(Modrinth instance) {
    Objects.requireNonNull(instance, "instance");
    if (instance instanceof ModifiableModrinth) {
      from((ModifiableModrinth) instance);
      return this;
    }
    projectId(instance.projectId());
    versionNumber(instance.versionNumber());
    changelog(instance.changelog());
    addAllDependencies(instance.dependencies());
    addAllGameVersions(instance.gameVersions());
    versionType(instance.versionType());
    addAllLoaders(instance.loaders());
    addAllAdditionalFiles(instance.additionalFiles());
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Modrinth} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableModrinth from(ModifiableModrinth instance) {
    Objects.requireNonNull(instance, "instance");
    projectId(instance.projectId());
    versionNumber(instance.versionNumber());
    changelog(instance.changelog());
    addAllDependencies(instance.dependencies());
    addAllGameVersions(instance.gameVersions());
    versionType(instance.versionType());
    addAllLoaders(instance.loaders());
    addAllAdditionalFiles(instance.additionalFiles());
    return this;
  }

  /**
   * Assigns a value to the {@code projectId} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code projectId}.</em>
   * @param projectId The value for projectId
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth projectId(String projectId) {
    this.projectId = Objects.requireNonNull(projectId, "projectId");
    return this;
  }

  /**
   * Assigns a value to the {@code versionNumber} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code versionNumber}.</em>
   * @param versionNumber The value for versionNumber
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth versionNumber(String versionNumber) {
    this.versionNumber = Objects.requireNonNull(versionNumber, "versionNumber");
    return this;
  }

  /**
   * Assigns a value to the {@code changelog} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code changelog}.</em>
   * @param changelog The value for changelog
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth changelog(String changelog) {
    this.changelog = Objects.requireNonNull(changelog, "changelog");
    return this;
  }

  /**
   * Adds one element to {@code dependencies} list.
   * @param element The dependencies element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addDependencies(Dependency element) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<Dependency>();
    }
    Objects.requireNonNull(element, "dependencies element");
    this.dependencies.add(element);
    optBits |= OPT_BIT_DEPENDENCIES;
    return this;
  }

  /**
   * Adds elements to {@code dependencies} list.
   * @param elements An array of dependencies elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth addDependencies(Dependency... elements) {
    for (Dependency e : elements) {
      addDependencies(e);
    }
    optBits |= OPT_BIT_DEPENDENCIES;
    return this;
  }

  /**
   * Sets or replaces all elements for {@code dependencies} list.
   * @param elements An iterable of dependencies elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth dependencies(Iterable<? extends Dependency> elements) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<Dependency>();
    } else {
      this.dependencies.clear();
    }
    addAllDependencies(elements);
    return this;
  }

  /**
   * Adds elements to {@code dependencies} list.
   * @param elements An iterable of dependencies elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addAllDependencies(Iterable<? extends Dependency> elements) {
    if (elements == null) return this;
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<Dependency>();
    }
    for (Dependency e : elements) {
      addDependencies(e);
    }
    optBits |= OPT_BIT_DEPENDENCIES;
    return this;
  }

  /**
   * Adds one element to {@code gameVersions} list.
   * @param element The gameVersions element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addGameVersions(String element) {
    if (this.gameVersions == null) {
      this.gameVersions = new ArrayList<String>();
    }
    Objects.requireNonNull(element, "gameVersions element");
    this.gameVersions.add(element);
    optBits |= OPT_BIT_GAME_VERSIONS;
    return this;
  }

  /**
   * Adds elements to {@code gameVersions} list.
   * @param elements An array of gameVersions elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth addGameVersions(String... elements) {
    for (String e : elements) {
      addGameVersions(e);
    }
    optBits |= OPT_BIT_GAME_VERSIONS;
    return this;
  }

  /**
   * Sets or replaces all elements for {@code gameVersions} list.
   * @param elements An iterable of gameVersions elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth gameVersions(Iterable<String> elements) {
    if (this.gameVersions == null) {
      this.gameVersions = new ArrayList<String>();
    } else {
      this.gameVersions.clear();
    }
    addAllGameVersions(elements);
    return this;
  }

  /**
   * Adds elements to {@code gameVersions} list.
   * @param elements An iterable of gameVersions elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addAllGameVersions(Iterable<String> elements) {
    if (elements == null) return this;
    if (this.gameVersions == null) {
      this.gameVersions = new ArrayList<String>();
    }
    for (String e : elements) {
      addGameVersions(e);
    }
    optBits |= OPT_BIT_GAME_VERSIONS;
    return this;
  }

  /**
   * Assigns a value to the {@code versionType} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code versionType}.</em>
   * @param versionType The value for versionType
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth versionType(VersionType versionType) {
    this.versionType = Objects.requireNonNull(versionType, "versionType");
    return this;
  }

  /**
   * Adds one element to {@code loaders} list.
   * @param element The loaders element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addLoaders(LoaderType element) {
    if (this.loaders == null) {
      this.loaders = new ArrayList<LoaderType>();
    }
    Objects.requireNonNull(element, "loaders element");
    this.loaders.add(element);
    optBits |= OPT_BIT_LOADERS;
    return this;
  }

  /**
   * Adds elements to {@code loaders} list.
   * @param elements An array of loaders elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth addLoaders(LoaderType... elements) {
    for (LoaderType e : elements) {
      addLoaders(e);
    }
    optBits |= OPT_BIT_LOADERS;
    return this;
  }

  /**
   * Sets or replaces all elements for {@code loaders} list.
   * @param elements An iterable of loaders elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth loaders(Iterable<? extends LoaderType> elements) {
    if (this.loaders == null) {
      this.loaders = new ArrayList<LoaderType>();
    } else {
      this.loaders.clear();
    }
    addAllLoaders(elements);
    return this;
  }

  /**
   * Adds elements to {@code loaders} list.
   * @param elements An iterable of loaders elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addAllLoaders(Iterable<? extends LoaderType> elements) {
    if (elements == null) return this;
    if (this.loaders == null) {
      this.loaders = new ArrayList<LoaderType>();
    }
    for (LoaderType e : elements) {
      addLoaders(e);
    }
    optBits |= OPT_BIT_LOADERS;
    return this;
  }

  /**
   * Adds one element to {@code additionalFiles} list.
   * @param element The additionalFiles element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addAdditionalFiles(String element) {
    if (this.additionalFiles == null) {
      this.additionalFiles = new ArrayList<String>();
    }
    Objects.requireNonNull(element, "additionalFiles element");
    this.additionalFiles.add(element);
    optBits |= OPT_BIT_ADDITIONAL_FILES;
    return this;
  }

  /**
   * Adds elements to {@code additionalFiles} list.
   * @param elements An array of additionalFiles elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth addAdditionalFiles(String... elements) {
    for (String e : elements) {
      addAdditionalFiles(e);
    }
    optBits |= OPT_BIT_ADDITIONAL_FILES;
    return this;
  }

  /**
   * Sets or replaces all elements for {@code additionalFiles} list.
   * @param elements An iterable of additionalFiles elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth additionalFiles(Iterable<String> elements) {
    if (this.additionalFiles == null) {
      this.additionalFiles = new ArrayList<String>();
    } else {
      this.additionalFiles.clear();
    }
    addAllAdditionalFiles(elements);
    return this;
  }

  /**
   * Adds elements to {@code additionalFiles} list.
   * @param elements An iterable of additionalFiles elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableModrinth addAllAdditionalFiles(Iterable<String> elements) {
    if (elements == null) return this;
    if (this.additionalFiles == null) {
      this.additionalFiles = new ArrayList<String>();
    }
    for (String e : elements) {
      addAdditionalFiles(e);
    }
    optBits |= OPT_BIT_ADDITIONAL_FILES;
    return this;
  }

  /**
   * Returns {@code true} if the default attribute {@code dependencies} is set.
   * @return {@code true} if set
   */
  public final boolean dependenciesIsSet() {
    return (optBits & OPT_BIT_DEPENDENCIES) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code gameVersions} is set.
   * @return {@code true} if set
   */
  public final boolean gameVersionsIsSet() {
    return (optBits & OPT_BIT_GAME_VERSIONS) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code loaders} is set.
   * @return {@code true} if set
   */
  public final boolean loadersIsSet() {
    return (optBits & OPT_BIT_LOADERS) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code additionalFiles} is set.
   * @return {@code true} if set
   */
  public final boolean additionalFilesIsSet() {
    return (optBits & OPT_BIT_ADDITIONAL_FILES) != 0;
  }

  /**
   * Returns {@code true} if the default attribute {@code projectId} is set.
   * @return {@code true} if set
   */
  public final boolean projectIdIsSet() {
    return projectId != null;
  }

  /**
   * Returns {@code true} if the default attribute {@code versionNumber} is set.
   * @return {@code true} if set
   */
  public final boolean versionNumberIsSet() {
    return versionNumber != null;
  }

  /**
   * Returns {@code true} if the default attribute {@code changelog} is set.
   * @return {@code true} if set
   */
  public final boolean changelogIsSet() {
    return changelog != null;
  }

  /**
   * Returns {@code true} if the default attribute {@code versionType} is set.
   * @return {@code true} if set
   */
  public final boolean versionTypeIsSet() {
    return versionType != null;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth unsetDependencies() {
    optBits |= 0;
    if (dependencies != null) {
      dependencies.clear();
    }
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth unsetGameVersions() {
    optBits |= 0;
    if (gameVersions != null) {
      gameVersions.clear();
    }
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth unsetLoaders() {
    optBits |= 0;
    if (loaders != null) {
      loaders.clear();
    }
    return this;
  }
  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableModrinth unsetAdditionalFiles() {
    optBits |= 0;
    if (additionalFiles != null) {
      additionalFiles.clear();
    }
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
   * This instance is equal to all instances of {@code ModifiableModrinth} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    if (!(another instanceof ModifiableModrinth)) return false;
    ModifiableModrinth other = (ModifiableModrinth) another;
    return equalTo(other);
  }

  private boolean equalTo(ModifiableModrinth another) {
    String projectId = projectId();
    String versionNumber = versionNumber();
    String changelog = changelog();
    List<Dependency> dependencies = dependencies();
    List<String> gameVersions = gameVersions();
    VersionType versionType = versionType();
    List<LoaderType> loaders = loaders();
    List<String> additionalFiles = additionalFiles();
    return projectId.equals(another.projectId())
        && versionNumber.equals(another.versionNumber())
        && changelog.equals(another.changelog())
        && dependencies.equals(another.dependencies())
        && gameVersions.equals(another.gameVersions())
        && versionType.equals(another.versionType())
        && loaders.equals(another.loaders())
        && additionalFiles.equals(another.additionalFiles());
  }

  /**
   * Computes a hash code from attributes: {@code projectId}, {@code versionNumber}, {@code changelog}, {@code dependencies}, {@code gameVersions}, {@code versionType}, {@code loaders}, {@code additionalFiles}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    String projectId = projectId();
    h += (h << 5) + projectId.hashCode();
    String versionNumber = versionNumber();
    h += (h << 5) + versionNumber.hashCode();
    String changelog = changelog();
    h += (h << 5) + changelog.hashCode();
    List<Dependency> dependencies = dependencies();
    h += (h << 5) + (dependencies == null ? 1 : dependencies.hashCode());
    List<String> gameVersions = gameVersions();
    h += (h << 5) + (gameVersions == null ? 1 : gameVersions.hashCode());
    VersionType versionType = versionType();
    h += (h << 5) + versionType.hashCode();
    List<LoaderType> loaders = loaders();
    h += (h << 5) + (loaders == null ? 1 : loaders.hashCode());
    List<String> additionalFiles = additionalFiles();
    h += (h << 5) + (additionalFiles == null ? 1 : additionalFiles.hashCode());
    return h;
  }

  /**
   * Generates a string representation of this {@code Modrinth}.
   * If uninitialized, some attribute values may appear as question marks.
   * @return A string representation
   */
  @Override
  public String toString() {
    return "ModifiableModrinth{"
        + "projectId=" + projectId()
        + ", versionNumber=" + versionNumber()
        + ", changelog=" + changelog()
        + ", dependencies=" + dependencies()
        + ", gameVersions=" + gameVersions()
        + ", versionType=" + versionType()
        + ", loaders=" + loaders()
        + ", additionalFiles=" + additionalFiles()
        + "}";
  }

  private static <T> List<T> createSafeList(Iterable<? extends T> iterable, boolean checkNulls, boolean skipNulls) {
    ArrayList<T> list;
    if (iterable instanceof Collection<?>) {
      int size = ((Collection<?>) iterable).size();
      if (size == 0) return Collections.emptyList();
      list = new ArrayList<>();
    } else {
      list = new ArrayList<>();
    }
    for (T element : iterable) {
      if (skipNulls && element == null) continue;
      if (checkNulls) Objects.requireNonNull(element, "element");
      list.add(element);
    }
    return list;
  }

  private static <T> List<T> createUnmodifiableList(boolean clone, List<T> list) {
    switch(list.size()) {
    case 0: return Collections.emptyList();
    case 1: return Collections.singletonList(list.get(0));
    default:
      if (clone) {
        return Collections.unmodifiableList(new ArrayList<>(list));
      } else {
        if (list instanceof ArrayList<?>) {
          ((ArrayList<?>) list).trimToSize();
        }
        return Collections.unmodifiableList(list);
      }
    }
  }
}
