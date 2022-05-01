package net.galacticraft.plugins.modrinth.util;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;

/**
 * @author FabricMC
 * @see 
 * 	<a href="https://github.com/FabricMC/fabric-loom/blob/dev/0.11/src/main/java/net/fabricmc/loom/configuration/DependencyInfo.java">DependencyInfo</a>
 */
public class DependencyData {
	final Project project;
	final Dependency dependency;
	final Configuration sourceConfiguration;

	public static DependencyData create(Project project, String configuration) {
		return create(project, project.getConfigurations().getByName(configuration));
	}

	public static DependencyData create(Project project, Configuration configuration) {
		DependencySet dependencies = configuration.getDependencies();

		if (dependencies.isEmpty()) {
			throw new IllegalArgumentException(String.format("Configuration '%s' has no dependencies", configuration.getName()));
		}

		if (dependencies.size() != 1) {
			throw new IllegalArgumentException(String.format("Configuration '%s' must only have 1 dependency", configuration.getName()));
		}

		return create(project, dependencies.iterator().next(), configuration);
	}

	public static DependencyData create(Project project, Dependency dependency, Configuration sourceConfiguration) {
		return new DependencyData(project, dependency, sourceConfiguration);
	}

	DependencyData(Project project, Dependency dependency, Configuration sourceConfiguration) {
		this.project = project;
		this.dependency = dependency;
		this.sourceConfiguration = sourceConfiguration;
	}

	public Dependency getDependency() {
		return dependency;
	}

	public Configuration getSourceConfiguration() {
		return sourceConfiguration;
	}

	public Set<File> resolve() {
		return sourceConfiguration.files(dependency);
	}

	public Optional<File> resolveFile() {
		Set<File> files = resolve();

		if (files.isEmpty()) {
			return Optional.empty();
		} else if (files.size() > 1) {
			StringBuilder builder = new StringBuilder(this.toString());
			builder.append(" resolves to more than one file:");

			for (File f : files) {
				builder.append("\n\t-").append(f.getAbsolutePath());
			}

			throw new RuntimeException(builder.toString());
		} else {
			return files.stream().findFirst();
		}
	}

	@Override
	public String toString() {
		return getDepString();
	}

	public String getDepString() {
		return dependency.getGroup() + ":" + dependency.getName() + ":" + dependency.getVersion();
	}
}
