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
package net.galacticraft.gradle.common;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.checkerframework.checker.nullness.qual.NonNull;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Checks {

	public static void checkArgument(final boolean b, final String string) {
		if (!b) {
			throw new IllegalStateException(string);
		}
	}

	public static boolean isNullOrEmpty(final String it) {
		return it == null || it.isEmpty();
	}

	public static String nullToEmpty(final String it) {
		if (it == null) {
			return "";
		}
		return it;
	}

	public static String emptyToNull(final String from) {
		if (from != null && !from.trim().isEmpty()) {
			return from;
		}
		return null;
	}

	public static <T> T firstNonNull(final T t1, final T t2) {
		if (t1 == null) {
			return t2;
		}
		return t1;
	}

	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isTrue(boolean expression, Supplier<String> messageSupplier) {
		if (!expression) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static void isNull(@Nullable Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	public static void isNull(@Nullable Object object, Supplier<String> messageSupplier) {
		if (object != null) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	public static <T extends @NonNull Object> T notNull(@Nullable T object, @Nullable Object errorMessage) {
		if (object == null) {
			throw new NullPointerException(String.valueOf(errorMessage));
		}
		return object;
	}

	public static <T extends @NonNull Object> T notNull(@Nullable T object) {
		if (object == null) {
			throw new NullPointerException(object + " must not be null");
		}
		return object;
	}
	
	public static void notNull(@Nullable Object object, Supplier<String> messageSupplier) {
		if (object == null) {
			throw new IllegalArgumentException(nullSafeGet(messageSupplier));
		}
	}

	@Nullable
	private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}
}
