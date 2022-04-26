/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.plugins.convention.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link Developer}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code new DeveloperImpl.BuilderImpl()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Immutable
public final class DeveloperImpl implements Developer {
  private final String id;
  private final String name;
  private final String email;
  private final List<String> roles;

  private DeveloperImpl(
      String id,
      String name,
      String email,
      List<String> roles) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.roles = roles;
  }

  /**
   * @return The value of the {@code id} attribute
   */
  @Override
  public String id() {
    return id;
  }

  /**
   * @return The value of the {@code name} attribute
   */
  @Override
  public String name() {
    return name;
  }

  /**
   * @return The value of the {@code email} attribute
   */
  @Override
  public String email() {
    return email;
  }

  /**
   * @return The value of the {@code roles} attribute
   */
  @Override
  public List<String> roles() {
    return roles;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Developer#id() id} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for id
   * @return A modified copy of the {@code this} object
   */
  public final DeveloperImpl id(String value) {
    String newValue = Objects.requireNonNull(value, "id");
    if (this.id.equals(newValue)) return this;
    return new DeveloperImpl(newValue, this.name, this.email, this.roles);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Developer#name() name} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for name
   * @return A modified copy of the {@code this} object
   */
  public final DeveloperImpl name(String value) {
    String newValue = Objects.requireNonNull(value, "name");
    if (this.name.equals(newValue)) return this;
    return new DeveloperImpl(this.id, newValue, this.email, this.roles);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Developer#email() email} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for email
   * @return A modified copy of the {@code this} object
   */
  public final DeveloperImpl email(String value) {
    String newValue = Objects.requireNonNull(value, "email");
    if (this.email.equals(newValue)) return this;
    return new DeveloperImpl(this.id, this.name, newValue, this.roles);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link Developer#roles() roles}.
   * @param elements The elements to set
   * @return A modified copy of {@code this} object
   */
  public final DeveloperImpl roles(String... elements) {
    List<String> newValue = createUnmodifiableList(false, createSafeList(Arrays.asList(elements), true, false));
    return new DeveloperImpl(this.id, this.name, this.email, newValue);
  }

  /**
   * Copy the current immutable object with elements that replace the content of {@link Developer#roles() roles}.
   * A shallow reference equality check is used to prevent copying of the same value by returning {@code this}.
   * @param elements An iterable of roles elements to set
   * @return A modified copy of {@code this} object
   */
  public final DeveloperImpl roles(Iterable<String> elements) {
    if (this.roles == elements) return this;
    List<String> newValue = createUnmodifiableList(false, createSafeList(elements, true, false));
    return new DeveloperImpl(this.id, this.name, this.email, newValue);
  }

  /**
   * This instance is equal to all instances of {@code DeveloperImpl} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof DeveloperImpl
        && equalTo(0, (DeveloperImpl) another);
  }

  private boolean equalTo(int synthetic, DeveloperImpl another) {
    return id.equals(another.id)
        && name.equals(another.name)
        && email.equals(another.email)
        && roles.equals(another.roles);
  }

  /**
   * Computes a hash code from attributes: {@code id}, {@code name}, {@code email}, {@code roles}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + id.hashCode();
    h += (h << 5) + name.hashCode();
    h += (h << 5) + email.hashCode();
    h += (h << 5) + roles.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code Developer} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "Developer{"
        + "id=" + id
        + ", name=" + name
        + ", email=" + email
        + ", roles=" + roles
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link Developer} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable Developer instance
   */
  public static DeveloperImpl copyOf(Developer instance) {
    if (instance instanceof DeveloperImpl) {
      return (DeveloperImpl) instance;
    }
    return new DeveloperImpl.BuilderImpl()
        .from(instance)
        .build();
  }

  /**
   * Builds instances of type {@link DeveloperImpl DeveloperImpl}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code BuilderImpl} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "Developer", generator = "Immutables")
  @NotThreadSafe
  static final class BuilderImpl implements Developer.Builder {
    private static final long INIT_BIT_ID = 0x1L;
    private static final long INIT_BIT_NAME = 0x2L;
    private static final long INIT_BIT_EMAIL = 0x4L;
    private long initBits = 0x7L;

    private @Nullable String id;
    private @Nullable String name;
    private @Nullable String email;
    private List<String> roles = null;

    /**
     * Creates a builder for {@link DeveloperImpl DeveloperImpl} instances.
     * <pre>
     * new DeveloperImpl.BuilderImpl()
     *    .id(String) // required {@link Developer#id() id}
     *    .name(String) // required {@link Developer#name() name}
     *    .email(String) // required {@link Developer#email() email}
     *    .roles|addAllRoles(String) // {@link Developer#roles() roles} elements
     *    .build();
     * </pre>
     */
    BuilderImpl() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code Developer} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * Collection elements and entries will be added, not replaced.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl from(Developer instance) {
      Objects.requireNonNull(instance, "instance");
      id(instance.id());
      name(instance.name());
      email(instance.email());
      addAllRoles(instance.roles());
      return this;
    }

    /**
     * Initializes the value for the {@link Developer#id() id} attribute.
     * @param id The value for id 
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl id(String id) {
      this.id = Objects.requireNonNull(id, "id");
      initBits &= ~INIT_BIT_ID;
      return this;
    }

    /**
     * Initializes the value for the {@link Developer#name() name} attribute.
     * @param name The value for name 
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl name(String name) {
      this.name = Objects.requireNonNull(name, "name");
      initBits &= ~INIT_BIT_NAME;
      return this;
    }

    /**
     * Initializes the value for the {@link Developer#email() email} attribute.
     * @param email The value for email 
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl email(String email) {
      this.email = Objects.requireNonNull(email, "email");
      initBits &= ~INIT_BIT_EMAIL;
      return this;
    }

    /**
     * Adds one element to {@link Developer#roles() roles} list.
     * @param element A roles element
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl roles(String element) {
      if (this.roles == null) {
        this.roles = new ArrayList<String>();
      }
      this.roles.add(Objects.requireNonNull(element, "roles element"));
      return this;
    }

    /**
     * Adds elements to {@link Developer#roles() roles} list.
     * @param elements An array of roles elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl roles(String... elements) {
      if (this.roles == null) {
        this.roles = new ArrayList<String>();
      }
      for (String element : elements) {
        this.roles.add(Objects.requireNonNull(element, "roles element"));
      }
      return this;
    }


    /**
     * Sets or replaces all elements for {@link Developer#roles() roles} list.
     * @param elements An iterable of roles elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl roles(Iterable<String> elements) {
      this.roles = new ArrayList<String>();
      return addAllRoles(elements);
    }

    /**
     * Adds elements to {@link Developer#roles() roles} list.
     * @param elements An iterable of roles elements
     * @return {@code this} builder for use in a chained invocation
     */
    public final BuilderImpl addAllRoles(Iterable<String> elements) {
      Objects.requireNonNull(elements, "roles element");
      if (this.roles == null) {
        this.roles = new ArrayList<String>();
      }
      for (String element : elements) {
        this.roles.add(Objects.requireNonNull(element, "roles element"));
      }
      return this;
    }

    /**
     * Builds a new {@link DeveloperImpl DeveloperImpl}.
     * @return An immutable instance of Developer
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public DeveloperImpl build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new DeveloperImpl(
          id,
          name,
          email,
          roles == null ? Collections.<String>emptyList() : createUnmodifiableList(true, roles));
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_ID) != 0) attributes.add("id");
      if ((initBits & INIT_BIT_NAME) != 0) attributes.add("name");
      if ((initBits & INIT_BIT_EMAIL) != 0) attributes.add("email");
      return "Cannot build Developer, some of required attributes are not set " + attributes;
    }
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
