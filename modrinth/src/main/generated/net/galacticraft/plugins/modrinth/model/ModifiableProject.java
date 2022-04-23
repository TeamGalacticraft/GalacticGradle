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
import net.galacticraft.plugins.modrinth.model.type.VersionType;
import org.immutables.value.Generated;

/**
 * A modifiable implementation of the {@link Project Project} type.
 * <p>Use the constructor to create new modifiable instances. You may even extend this class to
 * add some convenience methods, however most of the methods in this class are final
 * to preserve safety and predictable invariants.
 * <p><em>ModifiableProject is not thread-safe</em>
 */
@Generated(from = "Project", generator = "Modifiables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.Generated({"Modifiables.generator", "Project"})
@NotThreadSafe
class ModifiableProject implements Project {
  private static final long INIT_BIT_PROJECT_ID = 0x1L;
  private static final long INIT_BIT_VERSION_NUMBER = 0x2L;
  private static final long INIT_BIT_CHANGELOG = 0x4L;
  private static final long INIT_BIT_VERSION_TYPE = 0x8L;
  private static final long INIT_BIT_LOADERS = 0x10L;
  private static final long INIT_BIT_UPLOAD_FILE = 0x20L;
  private long initBits = 0x3fL;

  private String projectId;
  private String versionNumber;
  private String changelog;
  private ArrayList<Dependency> dependencies = null;
  private ArrayList<Dependency> gameVersions = null;
  private VersionType versionType;
  private VersionType loaders;
  private ArrayList<String> additionalFiles = null;
  private String uploadFile;

  /**
   * @return value of {@code projectId} attribute
   */
  @Override
  public final String projectId() {
    if (!projectIdIsSet()) {
      checkRequiredAttributes();
    }
    return projectId;
  }

  /**
   * @return value of {@code versionNumber} attribute
   */
  @Override
  public final String versionNumber() {
    if (!versionNumberIsSet()) {
      checkRequiredAttributes();
    }
    return versionNumber;
  }

  /**
   * @return value of {@code changelog} attribute
   */
  @Override
  public final String changelog() {
    if (!changelogIsSet()) {
      checkRequiredAttributes();
    }
    return changelog;
  }

  /**
   * @return modifiable list {@code dependencies}
   */
  @Override
  public final List<Dependency> dependencies() {
    if (dependencies == null) {
      dependencies = new ArrayList<Dependency>(0);
    }
    return dependencies;
  }

  /**
   * @return modifiable list {@code gameVersions}
   */
  @Override
  public final List<Dependency> gameVersions() {
    if (gameVersions == null) {
      gameVersions = new ArrayList<Dependency>(0);
    }
    return gameVersions;
  }

  /**
   * @return value of {@code versionType} attribute
   */
  @Override
  public final VersionType versionType() {
    if (!versionTypeIsSet()) {
      checkRequiredAttributes();
    }
    return versionType;
  }

  /**
   * @return value of {@code loaders} attribute
   */
  @Override
  public final VersionType loaders() {
    if (!loadersIsSet()) {
      checkRequiredAttributes();
    }
    return loaders;
  }

  /**
   * @return modifiable list {@code additionalFiles}
   */
  @Override
  public final List<String> additionalFiles() {
    if (additionalFiles == null) {
      additionalFiles = new ArrayList<String>(0);
    }
    return additionalFiles;
  }

  /**
   * @return value of {@code uploadFile} attribute
   */
  @Override
  public final String uploadFile() {
    if (!uploadFileIsSet()) {
      checkRequiredAttributes();
    }
    return uploadFile;
  }

  /**
   * Clears the object by setting all attributes to their initial values.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject clear() {
    initBits = 0x3fL;
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
    loaders = null;
    if (additionalFiles != null) {
      additionalFiles.clear();
    }
    uploadFile = null;
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Project} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableProject from(Project instance) {
    Objects.requireNonNull(instance, "instance");
    if (instance instanceof ModifiableProject) {
      from((ModifiableProject) instance);
      return this;
    }
    projectId(instance.projectId());
    versionNumber(instance.versionNumber());
    changelog(instance.changelog());
    addAllDependencies(instance.dependencies());
    addAllGameVersions(instance.gameVersions());
    versionType(instance.versionType());
    loaders(instance.loaders());
    addAllAdditionalFiles(instance.additionalFiles());
    uploadFile(instance.uploadFile());
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Project} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * Collection elements and entries will be added, not replaced.
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableProject from(ModifiableProject instance) {
    Objects.requireNonNull(instance, "instance");
    if (instance.projectIdIsSet()) {
      projectId(instance.projectId());
    }
    if (instance.versionNumberIsSet()) {
      versionNumber(instance.versionNumber());
    }
    if (instance.changelogIsSet()) {
      changelog(instance.changelog());
    }
    addAllDependencies(instance.dependencies());
    addAllGameVersions(instance.gameVersions());
    if (instance.versionTypeIsSet()) {
      versionType(instance.versionType());
    }
    if (instance.loadersIsSet()) {
      loaders(instance.loaders());
    }
    addAllAdditionalFiles(instance.additionalFiles());
    if (instance.uploadFileIsSet()) {
      uploadFile(instance.uploadFile());
    }
    return this;
  }

  /**
   * Assigns a value to the {@code projectId} attribute.
   * @param projectId The value for projectId
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject projectId(String projectId) {
    this.projectId = Objects.requireNonNull(projectId, "projectId");
    initBits &= ~INIT_BIT_PROJECT_ID;
    return this;
  }

  /**
   * Assigns a value to the {@code versionNumber} attribute.
   * @param versionNumber The value for versionNumber
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject versionNumber(String versionNumber) {
    this.versionNumber = Objects.requireNonNull(versionNumber, "versionNumber");
    initBits &= ~INIT_BIT_VERSION_NUMBER;
    return this;
  }

  /**
   * Assigns a value to the {@code changelog} attribute.
   * @param changelog The value for changelog
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject changelog(String changelog) {
    this.changelog = Objects.requireNonNull(changelog, "changelog");
    initBits &= ~INIT_BIT_CHANGELOG;
    return this;
  }

  /**
   * Adds one element to {@code dependencies} list.
   * @param element The dependencies element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject addDependencies(Dependency element) {
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<Dependency>();
    }
    Objects.requireNonNull(element, "dependencies element");
    this.dependencies.add(element);
    return this;
  }

  /**
   * Adds elements to {@code dependencies} list.
   * @param elements An array of dependencies elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject addDependencies(Dependency... elements) {
    for (Dependency e : elements) {
      addDependencies(e);
    }
    return this;
  }

  /**
   * Sets or replaces all elements for {@code dependencies} list.
   * @param elements An iterable of dependencies elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject dependencies(Iterable<? extends Dependency> elements) {
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
  public ModifiableProject addAllDependencies(Iterable<? extends Dependency> elements) {
    if (elements == null) return this;
    if (this.dependencies == null) {
      this.dependencies = new ArrayList<Dependency>();
    }
    for (Dependency e : elements) {
      addDependencies(e);
    }
    return this;
  }

  /**
   * Adds one element to {@code gameVersions} list.
   * @param element The gameVersions element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject addGameVersions(Dependency element) {
    if (this.gameVersions == null) {
      this.gameVersions = new ArrayList<Dependency>();
    }
    Objects.requireNonNull(element, "gameVersions element");
    this.gameVersions.add(element);
    return this;
  }

  /**
   * Adds elements to {@code gameVersions} list.
   * @param elements An array of gameVersions elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject addGameVersions(Dependency... elements) {
    for (Dependency e : elements) {
      addGameVersions(e);
    }
    return this;
  }

  /**
   * Sets or replaces all elements for {@code gameVersions} list.
   * @param elements An iterable of gameVersions elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject gameVersions(Iterable<? extends Dependency> elements) {
    if (this.gameVersions == null) {
      this.gameVersions = new ArrayList<Dependency>();
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
  public ModifiableProject addAllGameVersions(Iterable<? extends Dependency> elements) {
    if (elements == null) return this;
    if (this.gameVersions == null) {
      this.gameVersions = new ArrayList<Dependency>();
    }
    for (Dependency e : elements) {
      addGameVersions(e);
    }
    return this;
  }

  /**
   * Assigns a value to the {@code versionType} attribute.
   * @param versionType The value for versionType
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject versionType(VersionType versionType) {
    this.versionType = Objects.requireNonNull(versionType, "versionType");
    initBits &= ~INIT_BIT_VERSION_TYPE;
    return this;
  }

  /**
   * Assigns a value to the {@code loaders} attribute.
   * @param loaders The value for loaders
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject loaders(VersionType loaders) {
    this.loaders = Objects.requireNonNull(loaders, "loaders");
    initBits &= ~INIT_BIT_LOADERS;
    return this;
  }

  /**
   * Adds one element to {@code additionalFiles} list.
   * @param element The additionalFiles element
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject addAdditionalFiles(String element) {
    if (this.additionalFiles == null) {
      this.additionalFiles = new ArrayList<String>();
    }
    Objects.requireNonNull(element, "additionalFiles element");
    this.additionalFiles.add(element);
    return this;
  }

  /**
   * Adds elements to {@code additionalFiles} list.
   * @param elements An array of additionalFiles elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject addAdditionalFiles(String... elements) {
    for (String e : elements) {
      addAdditionalFiles(e);
    }
    return this;
  }

  /**
   * Sets or replaces all elements for {@code additionalFiles} list.
   * @param elements An iterable of additionalFiles elements
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject additionalFiles(Iterable<String> elements) {
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
  public ModifiableProject addAllAdditionalFiles(Iterable<String> elements) {
    if (elements == null) return this;
    if (this.additionalFiles == null) {
      this.additionalFiles = new ArrayList<String>();
    }
    for (String e : elements) {
      addAdditionalFiles(e);
    }
    return this;
  }

  /**
   * Assigns a value to the {@code uploadFile} attribute.
   * @param uploadFile The value for uploadFile
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableProject uploadFile(String uploadFile) {
    this.uploadFile = Objects.requireNonNull(uploadFile, "uploadFile");
    initBits &= ~INIT_BIT_UPLOAD_FILE;
    return this;
  }

  /**
   * Returns {@code true} if the required attribute {@code projectId} is set.
   * @return {@code true} if set
   */
  public final boolean projectIdIsSet() {
    return (initBits & INIT_BIT_PROJECT_ID) == 0;
  }

  /**
   * Returns {@code true} if the required attribute {@code versionNumber} is set.
   * @return {@code true} if set
   */
  public final boolean versionNumberIsSet() {
    return (initBits & INIT_BIT_VERSION_NUMBER) == 0;
  }

  /**
   * Returns {@code true} if the required attribute {@code changelog} is set.
   * @return {@code true} if set
   */
  public final boolean changelogIsSet() {
    return (initBits & INIT_BIT_CHANGELOG) == 0;
  }

  /**
   * Returns {@code true} if the required attribute {@code versionType} is set.
   * @return {@code true} if set
   */
  public final boolean versionTypeIsSet() {
    return (initBits & INIT_BIT_VERSION_TYPE) == 0;
  }

  /**
   * Returns {@code true} if the required attribute {@code loaders} is set.
   * @return {@code true} if set
   */
  public final boolean loadersIsSet() {
    return (initBits & INIT_BIT_LOADERS) == 0;
  }

  /**
   * Returns {@code true} if the required attribute {@code uploadFile} is set.
   * @return {@code true} if set
   */
  public final boolean uploadFileIsSet() {
    return (initBits & INIT_BIT_UPLOAD_FILE) == 0;
  }

  /**
   * Returns {@code true} if the {@code dependencies} has not been initialized
   * and will default to an empty list.
   * @return {@code true} if set
   */
  public final boolean dependenciesIsSet() {
    return dependencies != null;
  }

  /**
   * Returns {@code true} if the {@code gameVersions} has not been initialized
   * and will default to an empty list.
   * @return {@code true} if set
   */
  public final boolean gameVersionsIsSet() {
    return gameVersions != null;
  }

  /**
   * Returns {@code true} if the {@code additionalFiles} has not been initialized
   * and will default to an empty list.
   * @return {@code true} if set
   */
  public final boolean additionalFilesIsSet() {
    return additionalFiles != null;
  }


  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject unsetProjectId() {
    initBits |= INIT_BIT_PROJECT_ID;
    projectId = null;
    return this;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject unsetVersionNumber() {
    initBits |= INIT_BIT_VERSION_NUMBER;
    versionNumber = null;
    return this;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject unsetChangelog() {
    initBits |= INIT_BIT_CHANGELOG;
    changelog = null;
    return this;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject unsetVersionType() {
    initBits |= INIT_BIT_VERSION_TYPE;
    versionType = null;
    return this;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject unsetLoaders() {
    initBits |= INIT_BIT_LOADERS;
    loaders = null;
    return this;
  }

  /**
   * Reset an attribute to its initial value.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public final ModifiableProject unsetUploadFile() {
    initBits |= INIT_BIT_UPLOAD_FILE;
    uploadFile = null;
    return this;
  }

  /**
   * Returns {@code true} if all required attributes are set, indicating that the object is initialized.
   * @return {@code true} if set
   */
  public final boolean isInitialized() {
    return initBits == 0;
  }

  private void checkRequiredAttributes() {
    if (!isInitialized()) {
      throw new IllegalStateException(formatRequiredAttributesMessage());
    }
  }

  private String formatRequiredAttributesMessage() {
    List<String> attributes = new ArrayList<>();
    if (!projectIdIsSet()) attributes.add("projectId");
    if (!versionNumberIsSet()) attributes.add("versionNumber");
    if (!changelogIsSet()) attributes.add("changelog");
    if (!versionTypeIsSet()) attributes.add("versionType");
    if (!loadersIsSet()) attributes.add("loaders");
    if (!uploadFileIsSet()) attributes.add("uploadFile");
    return "Project is not initialized, some of the required attributes are not set " + attributes;
  }

  /**
   * This instance is equal to all instances of {@code ModifiableProject} that have equal attribute values.
   * An uninitialized instance is equal only to itself.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    if (!(another instanceof ModifiableProject)) return false;
    ModifiableProject other = (ModifiableProject) another;
    if (!isInitialized() || !other.isInitialized()) {
      return false;
    }
    return equalTo(other);
  }

  private boolean equalTo(ModifiableProject another) {
    List<Dependency> dependencies = this.dependencies != null
        ? this.dependencies
        : Collections.<Dependency>emptyList();
    List<Dependency> gameVersions = this.gameVersions != null
        ? this.gameVersions
        : Collections.<Dependency>emptyList();
    List<String> additionalFiles = this.additionalFiles != null
        ? this.additionalFiles
        : Collections.<String>emptyList();
    return projectId.equals(another.projectId)
        && versionNumber.equals(another.versionNumber)
        && changelog.equals(another.changelog)
        && dependencies.equals(another.dependencies())
        && gameVersions.equals(another.gameVersions())
        && versionType.equals(another.versionType)
        && loaders.equals(another.loaders)
        && additionalFiles.equals(another.additionalFiles())
        && uploadFile.equals(another.uploadFile);
  }

  /**
   * Computes a hash code from attributes: {@code projectId}, {@code versionNumber}, {@code changelog}, {@code dependencies}, {@code gameVersions}, {@code versionType}, {@code loaders}, {@code additionalFiles}, {@code uploadFile}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + projectId.hashCode();
    h += (h << 5) + versionNumber.hashCode();
    h += (h << 5) + changelog.hashCode();
    h += (h << 5) + (dependencies == null ? 1 : dependencies.hashCode());
    h += (h << 5) + (gameVersions == null ? 1 : gameVersions.hashCode());
    h += (h << 5) + versionType.hashCode();
    h += (h << 5) + loaders.hashCode();
    h += (h << 5) + (additionalFiles == null ? 1 : additionalFiles.hashCode());
    h += (h << 5) + uploadFile.hashCode();
    return h;
  }

  /**
   * Generates a string representation of this {@code Project}.
   * If uninitialized, some attribute values may appear as question marks.
   * @return A string representation
   */
  @Override
  public String toString() {
    return "ModifiableProject{"
        + "projectId="  + (projectIdIsSet() ? projectId() : "?")
        + ", versionNumber="  + (versionNumberIsSet() ? versionNumber() : "?")
        + ", changelog="  + (changelogIsSet() ? changelog() : "?")
        + ", dependencies=" + dependencies()
        + ", gameVersions=" + gameVersions()
        + ", versionType="  + (versionTypeIsSet() ? versionType() : "?")
        + ", loaders="  + (loadersIsSet() ? loaders() : "?")
        + ", additionalFiles=" + additionalFiles()
        + ", uploadFile="  + (uploadFileIsSet() ? uploadFile() : "?")
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
