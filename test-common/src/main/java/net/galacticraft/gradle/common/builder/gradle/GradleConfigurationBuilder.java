package net.galacticraft.gradle.common.builder.gradle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.galacticraft.gradle.common.builder.io.FileBuilder;

public class GradleConfigurationBuilder {
	private FileBuilder fileBuilder = null;
	private FileBuilder settingsFileBuilder = null;
	private FileBuilder propertiesBuilder = null;
	private String testProjectName = null;
	private Map<String, String> properties = new HashMap<String, String>();
	private Set<String> plugins = new HashSet<>();
	private Map<String, List<String>> configurations = new HashMap<>();
	private List<String> customConfigurationBlocks = new ArrayList<>();
	private List<String> repositories = new ArrayList<>();

	public GradleConfigurationBuilder(FileBuilder fileBuilder, FileBuilder settingsFileBuilder, FileBuilder propertiesBuilder, String projetName) {
		this.fileBuilder = fileBuilder;
		this.settingsFileBuilder = settingsFileBuilder;
		this.propertiesBuilder = propertiesBuilder;
		this.testProjectName = projetName;
	}

	public GradleConfigurationBuilder applyPlugin(String pluginId) {
		plugins.add(pluginId);
		return this;
	}

	public GradleConfigurationBuilder addRepository(String repository) {
		this.repositories.add(repository);
		return this;
	}
	
	public GradleConfigurationBuilder addProperty(String key, String value) {
		properties.putIfAbsent(key, value);
		return this;
	}

	public GradleConfigurationBuilder addDependency(String configurationName, String dependency) {
		if (!configurations.containsKey(configurationName))
			configurations.put(configurationName, new ArrayList<>());
		configurations.get(configurationName).add(dependency);
		return this;
	}

	public GradleConfigurationBuilder addCustomConfigurationBlock(String block) {
		customConfigurationBlocks.add(block);
		return this;
	}

	public void write() {
		if (fileBuilder == null)
			throw new IllegalArgumentException("No file builder!");
		write(this.fileBuilder);
		writeSettingsFile(this.settingsFileBuilder);
		writePropertiesFile(this.propertiesBuilder);
		
	}
	
	public void writeSettingsFile(FileBuilder builder) {
		builder.append(String.format("rootProject.name = '%s'", this.testProjectName));
		builder.write();
	}
	
	public void writePropertiesFile (FileBuilder builder) {
		if(!this.properties.isEmpty()) {
			for(Map.Entry<String, String> entry : this.properties.entrySet()) {
				builder.appendNewLine()
					.append(entry.getKey()).append("=").append(entry.getValue());
			}
		builder.write();
		}
	}

	public void write(FileBuilder builder) {
		builder.append("plugins {").indent();
		for (String plugin : plugins) {
			builder.appendNewLine().append("id '").append(plugin).append("'");
		}
		builder.unindent().appendNewLine().append("}");
		builder.appendNewLine();

		builder.append("repositories {").indent();
		builder.appendNewLine().append("mavenCentral()").appendNewLine().append("mavenCentral()");

		for (String repository : repositories) {
			builder.appendNewLine().append("maven {").indent().appendNewLine().append("url ").append('"')
					.append(repository).append('"').unindent().appendNewLine().append("}");
		}

		builder.unindent().appendNewLine().append("}").appendNewLine();

		builder.append("dependencies {").indent();

		for (Map.Entry<String, List<String>> stringListEntry : configurations.entrySet()) {
			for (String s : stringListEntry.getValue()) {
				builder.appendNewLine().append(stringListEntry.getKey()).append(" ");
				if (!s.contains(" ") && !s.contains(",") && !s.contains("(") && !s.contains("\""))
					builder.append('"').append(s).append('"');
				else
					builder.append(s);
			}
		}

		builder.unindent().appendNewLine().append("}").appendNewLine();

		for (String customConfigurationBlock : customConfigurationBlocks) {
			builder.append(customConfigurationBlock).appendNewLine();
		}

		builder.write();
	}
}
