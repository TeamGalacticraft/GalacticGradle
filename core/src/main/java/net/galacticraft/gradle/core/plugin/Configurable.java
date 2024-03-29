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

package net.galacticraft.gradle.core.plugin;

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.codehaus.groovy.runtime.GeneratedClosure;
import org.gradle.api.Action;
import org.gradle.internal.metaobject.ConfigureDelegate;
import org.gradle.util.internal.ClosureBackedAction;

import groovy.lang.Closure;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Configurable
{

	/**
	 * Apply a configuration action to an instance and return it.
	 *
	 * @param instance
	 *            the instance to configure
	 * @param configureAction
	 *            the action to configure with
	 * @param <T>
	 *            type being configured
	 * 
	 * @return the provided {@code instance}
	 * 
	 * @since 1.0.0
	 */
	public static <T> @Nonnull T configure(final @Nonnull T instance, final @Nonnull Action<T> configureAction)
	{
		requireNonNull(configureAction, "configureAction").execute(instance);
		return instance;
	}

	/**
	 * Configure the instance if an action is provided, otherwise pass it through.
	 *
	 * @param instance
	 *            the instance to configure
	 * @param configureAction
	 *            the action to configure with
	 * @param <T>
	 *            type being configured
	 * 
	 * @return the provided {@code instance}
	 * 
	 * @since 1.0.0
	 */
	public static <T> @Nonnull T configureIfNonNull(final @Nonnull T instance, final @Nullable Action<T> configureAction)
	{
		if (configureAction != null)
		{
			configureAction.execute(instance);
		}
		return instance;
	}

	/**
	 * Called from an object's {@link Configurable#configure} method.
	 */
	public static <T> T configureSelf(@Nullable Closure<?> configureClosure, T target)
	{
		if (configureClosure == null)
		{
			return target;
		}

		configureTarget(configureClosure, target, new ConfigureDelegate(configureClosure, target));
		return target;
	}

	private static <T> void configureTarget(Closure<?> configureClosure, T target, ConfigureDelegate closureDelegate)
	{
		if (!(configureClosure instanceof GeneratedClosure))
		{
			new ClosureBackedAction<T>(configureClosure, Closure.DELEGATE_FIRST, false).execute(target);
			return;
		}
		Closure<?> withNewOwner = configureClosure.rehydrate(target, closureDelegate, configureClosure.getThisObject());
		new ClosureBackedAction<T>(withNewOwner, Closure.OWNER_ONLY, false).execute(target);
	}
}
