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

package net.galacticraft.plugins.publishing.modrinth;

import java.io.File;

import org.gradle.api.GradleException;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.modrinth.minotaur.Minotaur;
import com.modrinth.minotaur.ModrinthExtension;
import com.modrinth.minotaur.request.VersionType;

import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.common.plugins.property.BooleanProperty;

public class ModrinthPlugin extends GradlePlugin {

	@Override
	public void plugin() {
		applyPlugin(JavaPlugin.class);
		applyPlugin(Minotaur.class);

		ModrinthPublishExtension extension = extensions().findOrCreate("minotaur", ModrinthPublishExtension.class,
				project().getObjects(), extensions().find(ModrinthExtension.class));

		registerTask("publishToModrinth", task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Publishes the mod artifact to the Modrinth Mod Platform");
			task.finalizedBy(tasks().named("modrinth"));
		});

		BooleanProperty debugProvider = new BooleanProperty(project(), "modrinth.debug");

		project().afterEvaluate(set -> {
			extensions().container().configure(ModrinthExtension.class, modrinthExtension -> {
				configureApiToken(modrinthExtension);
				configureUploadFileIfNeeded(modrinthExtension);
				configureVersionIfNeeded(modrinthExtension);
				configureChangelogIfNeeded(modrinthExtension);
				modrinthExtension.getProjectId().set(extension.getProjectId());
				modrinthExtension.getDetectLoaders().set(true);
				if (extension.getDebug().isPresent()) {
					modrinthExtension.getDebugMode().set(extension.getDebug());
				} else if (debugProvider.isSet()) {
					modrinthExtension.getDebugMode().set(booleanProperty("modrinth.debug"));
				}
			});
		});
	}

	private void configureApiToken(ModrinthExtension extension) {
		if (!extension.getToken().isPresent()) {
			if (System.getenv("MODRINTH_TOKEN") != null) {
				extension.getToken().set(System.getenv("MODRINTH_TOKEN"));
			} else if (System.getProperty("MODRINTH_TOKEN") != null) {
				extension.getToken().set(System.getProperty("MODRINTH_TOKEN"));
			} else if (project().findProperty("MODRINTH_TOKEN") != null) {
				extension.getToken().set((String) project().findProperty("MODRINTH_TOKEN"));
			} else {
				project().getLogger().lifecycle(
						"[Modrinth] Could not set MODRINTH_TOKEN from Environment Variable, System Property or Project Property");
				throw new GradleException(
						"[Modrinth] Could not set MODRINTH_TOKEN from Environment Variable, System Property or Project Property");
			}
		}
	}

	private void configureUploadFileIfNeeded(ModrinthExtension extension) {
		if (!extension.getUploadFile().isPresent()) {
			project().getTasks().withType(AbstractArchiveTask.class, task -> {
				if (task.getName().equals("jar")) {
					extension.getUploadFile().set(task.getArchiveFile());
				}
			});
		}
	}

	private void configureVersionIfNeeded(ModrinthExtension extension) {
		Provider<String> version = project()
				.provider(() -> project().getVersion() == null ? null : String.valueOf(project().getVersion()));
		if (!extension.getVersionNumber().isPresent()) {
			extension.getVersionNumber().set(version);
		}

		if (!extension.getVersionType().isPresent()) {
			for (VersionType val : VersionType.values()) {
				String s = val.name().toLowerCase();
				if (version.get().contains(s)) {
					extension.getVersionType().set(s);
				}
			}
		}
	}

	private void configureChangelogIfNeeded(ModrinthExtension extension) {
		File dir = project().getProjectDir();
		String changelogContent = null;
		if (extension.getChangelog().isPresent()) {
			String changelogFile = extension.getChangelog().get();
			File file = project().file(changelogFile);
			if (file.exists()) {
				changelogContent = this.readFromFile(file);
			} else {
				if (!determineIfChangelogPropertyIsAFileName(changelogFile)) {
					changelogContent = extension.getChangelog().get();
				}
			}
		} else {
			for (File file : dir.listFiles()) {
				if (file.isFile()) {
					String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
					if (filename[0].equalsIgnoreCase("changelog")) {
						changelogContent = this.readFromFile(file);
					}
				}
			}
		}
		if (changelogContent != null) {
			extension.getChangelog().set(changelogContent);
		} else {
			extension.getChangelog().set("No changelog provided");
		}
	}

	private boolean determineIfChangelogPropertyIsAFileName(String changelog) {
		String[] changelogDeclaration = changelog.split("\\.");
		if (changelogDeclaration.length >= 2) {
			String afterPeriod = changelogDeclaration[1];
			if (afterPeriod.startsWith("  ") || afterPeriod.startsWith(" ")) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private String readFromFile(File file) {
		try {
			return Files.asCharSource(file, Charsets.UTF_8).read();
		} catch (Exception e) {
		}
		return null;
	}
}
