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

package net.galacticraft.gradle.core.util;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

public class DependencyDataSet
{
	final Configuration configuration;
	final List<DependencyData> dependencies;

	public static DependencyDataSet create(Project project, String configuration)
	{
		return create(project, project.getConfigurations().getByName(configuration));
	}

	public static DependencyDataSet create(Project project, Configuration configuration)
	{
		DependencySet dependencies = configuration.getDependencies();

		if (dependencies.isEmpty())
		{
			throw new IllegalArgumentException(String.format("Configuration '%s' has no dependencies", configuration.getName()));
		}

		return new DependencyDataSet(project, dependencies, configuration);
	}

	DependencyDataSet(Project project, DependencySet dependencySet, Configuration configuration)
	{
		this.dependencies = new LinkedList<>();
		this.configuration = configuration;
		for(Dependency dep : dependencySet)
		{
			dependencies.add(new DependencyData(project, dep, configuration));
		}
	}

	public List<Dependency> getDependencies()
	{
		return dependencies.stream().map(DependencyData::getDependency).collect(Collectors.toList());
	}

	public Configuration getSourceConfiguration()
	{
		return configuration;
	}
}
