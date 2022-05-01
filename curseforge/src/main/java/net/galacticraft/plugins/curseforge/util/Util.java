package net.galacticraft.plugins.curseforge.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.annotation.Nullable;

import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loom.configuration.DependencyProvider.DependencyInfo;
import net.galacticraft.plugins.curseforge.CurseUploadExtension;
import net.galacticraft.plugins.curseforge.curse.CurseVersions;

public class Util {
	
	public static Project project;
	private static final Gson gson = new Gson();
	private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
	
	public static Gson getGson() {
		return gson;
	}
	
	public static Gson getPrettyPrintGson() {
		return prettyGson;
	}
	
    public static String fromResourceAsString(String fileName) {
        ClassLoader classLoader = Util.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        String output = null;
        
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
				output = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
			} catch (IOException e) {
			}
        }
        return output;
    }


	/**
	 * Check if a condition is true, and raise an exception if not
	 *
	 * @param condition The condition
	 * @param message   The message to display
	 */
	public static void check(boolean condition, String message) {
		if (!condition) {
			throw new RuntimeException(message);
		}
	}
	
	@Nullable
	public static File resolveFile(Object in) {
		if (in == null || project == null) {
			return null;
		}

		else if (in instanceof File) {
			return (File) in;
		}

		else if (in instanceof AbstractArchiveTask) {
			return ((AbstractArchiveTask) in).getArchiveFile().get().getAsFile();
		}

		else if (in instanceof TaskProvider<?>) {
			Object provided = ((TaskProvider<?>) in).get();

			if (provided instanceof AbstractArchiveTask) {
				return ((AbstractArchiveTask) provided).getArchiveFile().get().getAsFile();
			}
		}

		return project.file(in);
	}
	
	@Nullable
	public static File resolveFile(Project project, Object in) {
		if (in == null || project == null) {
			return null;
		}

		else if (in instanceof File) {
			return (File) in;
		}

		else if (in instanceof AbstractArchiveTask) {
			return ((AbstractArchiveTask) in).getArchiveFile().get().getAsFile();
		}

		else if (in instanceof TaskProvider<?>) {
			Object provided = ((TaskProvider<?>) in).get();

			if (provided instanceof AbstractArchiveTask) {
				return ((AbstractArchiveTask) provided).getArchiveFile().get().getAsFile();
			}
		}

		return project.file(in);
	}
	
	public static void runForgeCheck(Configuration minecraft, Project project, CurseUploadExtension extension) {
		DependencyData data = DependencyData.create(project, minecraft);
		String[] versionParts = data.getDependency().getVersion().split("-");
		for (String version : CurseVersions.checkVersionsToAdd(versionParts)) {
			extension.getGameVersions().add(version);
		}
	}

	public static void runFabricCheck(Configuration minecraft, Configuration modImplementation, Project project, CurseUploadExtension extension) {
		Dependency d1 = minecraft.getDependencies().iterator().next();
		DependencyInfo minecraftDepInfo = DependencyInfo.create(project, d1, minecraft);
		if (minecraftDepInfo.getDependency().getVersion() != null) {
			extension.getGameVersions().add(minecraftDepInfo.getDependency().getVersion());
		}
		Dependency d2 = minecraft.getDependencies().iterator().next();
		DependencyInfo fabricDepInfo = DependencyInfo.create(project, d2, modImplementation);
		if (fabricDepInfo.getDependency().getVersion() != null) {
			extension.getGameVersions().add(fabricDepInfo.getDependency().getVersion());
		}
	}
}
