package net.galacticraft.plugins.modrinth.model;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.NotThreadSafe;
import net.galacticraft.plugins.modrinth.model.type.DependencyType;
import org.immutables.value.Generated;

/**
 * A modifiable implementation of the {@link Dependency Dependency} type.
 * <p>Use the constructor to create new modifiable instances. You may even extend this class to
 * add some convenience methods, however most of the methods in this class are final
 * to preserve safety and predictable invariants.
 * <p><em>ModifiableDependency is not thread-safe</em>
 */
@Generated(from = "Dependency", generator = "Modifiables")
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@javax.annotation.Generated({"Modifiables.generator", "Dependency"})
@NotThreadSafe
class ModifiableDependency implements Dependency {
  private String versionId;
  private String projectId;
  private DependencyType dependencyType;

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code versionId} attribute
   */
  @Override
  public final String versionId() {
    if (versionIdIsSet()) {
      return versionId;
    } else {
      return Dependency.super.versionId();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code projectId} attribute
   */
  @Override
  public final String projectId() {
    if (projectIdIsSet()) {
      return projectId;
    } else {
      return Dependency.super.projectId();
    }
  }

  /**
   * @return assigned or, otherwise, newly computed, not cached value of {@code dependencyType} attribute
   */
  @Override
  public final DependencyType dependencyType() {
    if (dependencyTypeIsSet()) {
      return dependencyType;
    } else {
      return Dependency.super.dependencyType();
    }
  }

  /**
   * Clears the object by setting all attributes to their initial values.
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableDependency clear() {
    versionId = null;
    projectId = null;
    dependencyType = null;
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Dependency} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableDependency from(Dependency instance) {
    Objects.requireNonNull(instance, "instance");
    if (instance instanceof ModifiableDependency) {
      from((ModifiableDependency) instance);
      return this;
    }
    versionId(instance.versionId());
    projectId(instance.projectId());
    dependencyType(instance.dependencyType());
    return this;
  }

  /**
   * Fill this modifiable instance with attribute values from the provided {@link Dependency} instance.
   * Regular attribute values will be overridden, i.e. replaced with ones of an instance.
   * Any of the instance's absent optional values will not be copied (will not override current values).
   * @param instance The instance from which to copy values
   * @return {@code this} for use in a chained invocation
   */
  public ModifiableDependency from(ModifiableDependency instance) {
    Objects.requireNonNull(instance, "instance");
    versionId(instance.versionId());
    projectId(instance.projectId());
    dependencyType(instance.dependencyType());
    return this;
  }

  /**
   * Assigns a value to the {@code versionId} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code versionId}.</em>
   * @param versionId The value for versionId
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableDependency versionId(String versionId) {
    this.versionId = Objects.requireNonNull(versionId, "versionId");
    return this;
  }

  /**
   * Assigns a value to the {@code projectId} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code projectId}.</em>
   * @param projectId The value for projectId
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableDependency projectId(String projectId) {
    this.projectId = Objects.requireNonNull(projectId, "projectId");
    return this;
  }

  /**
   * Assigns a value to the {@code dependencyType} attribute.
   * <p><em>If not set, this attribute will have a default value returned by the initializer of {@code dependencyType}.</em>
   * @param dependencyType The value for dependencyType
   * @return {@code this} for use in a chained invocation
   */
  @CanIgnoreReturnValue
  public ModifiableDependency dependencyType(DependencyType dependencyType) {
    this.dependencyType = Objects.requireNonNull(dependencyType, "dependencyType");
    return this;
  }

  /**
   * Returns {@code true} if the default attribute {@code versionId} is set.
   * @return {@code true} if set
   */
  public final boolean versionIdIsSet() {
    return versionId != null;
  }

  /**
   * Returns {@code true} if the default attribute {@code projectId} is set.
   * @return {@code true} if set
   */
  public final boolean projectIdIsSet() {
    return projectId != null;
  }

  /**
   * Returns {@code true} if the default attribute {@code dependencyType} is set.
   * @return {@code true} if set
   */
  public final boolean dependencyTypeIsSet() {
    return dependencyType != null;
  }


  /**
   * Returns {@code true} if all required attributes are set, indicating that the object is initialized.
   * @return {@code true} if set
   */
  public final boolean isInitialized() {
    return true;
  }

  /**
   * This instance is equal to all instances of {@code ModifiableDependency} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    if (!(another instanceof ModifiableDependency)) return false;
    ModifiableDependency other = (ModifiableDependency) another;
    return equalTo(other);
  }

  private boolean equalTo(ModifiableDependency another) {
    String versionId = versionId();
    String projectId = projectId();
    DependencyType dependencyType = dependencyType();
    return versionId.equals(another.versionId())
        && projectId.equals(another.projectId())
        && dependencyType.equals(another.dependencyType());
  }

  /**
   * Computes a hash code from attributes: {@code versionId}, {@code projectId}, {@code dependencyType}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    String versionId = versionId();
    h += (h << 5) + versionId.hashCode();
    String projectId = projectId();
    h += (h << 5) + projectId.hashCode();
    DependencyType dependencyType = dependencyType();
    h += (h << 5) + dependencyType.hashCode();
    return h;
  }

  /**
   * Generates a string representation of this {@code Dependency}.
   * If uninitialized, some attribute values may appear as question marks.
   * @return A string representation
   */
  @Override
  public String toString() {
    return "ModifiableDependency{"
        + "versionId=" + versionId()
        + ", projectId=" + projectId()
        + ", dependencyType=" + dependencyType()
        + "}";
  }
}
