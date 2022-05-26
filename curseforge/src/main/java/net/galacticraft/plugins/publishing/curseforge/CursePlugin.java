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

package net.galacticraft.plugins.publishing.curseforge;

import java.io.File;

import org.gradle.api.GradleException;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.plugins.publishing.curseforge.curse.ChangelogType;
import net.galacticraft.plugins.publishing.curseforge.curse.CurseVersions;
import net.galacticraft.plugins.publishing.curseforge.curse.FileArtifact;
import net.galacticraft.plugins.publishing.curseforge.curse.ReleaseType;
import net.galacticraft.plugins.publishing.curseforge.http.OkHttpUtil;
import net.galacticraft.plugins.publishing.curseforge.util.Util;

public class CursePlugin extends GradlePlugin {

	@Override
	public void plugin() {
		
		applyPlugin(JavaPlugin.class);
		
		Util.project = project();
		OkHttpUtil.setup(project());
		CurseVersions.init();
		
		CurseExtension extension = extensions().findOrCreate("curseforge", CurseExtension.class);
		
		registerTask("publishToCurseforge", CursePublishTask.class, task -> {
			task.setGroup("galactic-gradle");
			task.setDescription("Publishes mod artifact to the CurseForge Platform");
			task.dependsOn(tasks().named("build"));
		});
		
		project().afterEvaluate(configure -> {
			this.configureApiToken(extension);
			this.configureUploadFileIfNeeded(extension);
			this.configureVersionIfNeeded(extension);
			this.configureChangelogIfNeeded(extension);
		});
	}
	
	private void configureApiToken(CurseExtension extension) {
		if(!extension.getApiKey().isPresent()) {
			if(System.getenv("CURSE_TOKEN") != null) {
				extension.getApiKey().set(System.getenv("CURSE_TOKEN"));
			} else if (System.getProperty("CURSE_TOKEN") != null) {
				extension.getApiKey().set(System.getProperty("CURSE_TOKEN"));
			} else if (project().findProperty("CURSE_TOKEN") != null) {
				extension.getApiKey().set((String) project().findProperty("CURSE_TOKEN"));
			} else {
				lifecycle("[CurseForge] Could not set CURSE_TOKEN from Environment Variable, System Property or Project Property");
				throw new GradleException("[CurseForge] Could not set CURSE_TOKEN from Environment Variable, System Property or Project Property");
			}
		}
		OkHttpUtil.instance.addHeader("X-Api-Token", extension.getApiKey().get());
	}
	
	private void configureUploadFileIfNeeded(CurseExtension extension) {
		if(!extension.getMainFile().isPresent()) {
			tasks().withType(AbstractArchiveTask.class, task -> {
				if(task.getName().equals("jar")) {
					extension.getMainFile().set(new FileArtifact(task.getArchiveFile(), extension.getDisplayName().getOrNull()));
				}
			});
		}
	}
	
	private void configureVersionIfNeeded(CurseExtension extension) {
		if(!extension.getReleaseType().isPresent()) {
			Provider<String> version = project().provider(() -> project().getVersion() == null ? null : String.valueOf(project().getVersion()));
	        for(String val : ReleaseType.CONSTANTS.keySet()) {
	        	if(version.get().contains(val)) {
	        		extension.getReleaseType().set(val);
	        	}
	        }
		}
	}
	
	private void configureChangelogIfNeeded(CurseExtension extension) {
        File dir = project().getProjectDir();
        String changelogContent =  null;
        String changelogExt = null;
		if(extension.getChangelog().isPresent()) {
        	String changelogFile = extension.getChangelog().get();
        	File file = project().file(changelogFile);
        	if(file.exists()) {
        		changelogContent = this.readFromFile(file);
        		String fileExt = Files.getFileExtension(file.getName());
        		changelogExt = ChangelogType.fromValue(fileExt).value();
        	} else {
        		changelogContent = extension.getChangelog().get();
				changelogExt = ChangelogType.TEXT.value();
			}
		} else {
            for(File file : dir.listFiles()) {
            	if (file.isFile()) {
            		String[] filename = file.getName().split("\\.(?=[^\\.]+$)");
            		if(filename[0].equalsIgnoreCase("changelog")) {
            			changelogContent = this.readFromFile(file);
    	        		String fileExt = Files.getFileExtension(file.getName());
    	        		changelogExt = ChangelogType.fromValue(fileExt).value();
            		}
            	}
            }
		}
		if(changelogContent != null) {
			extension.getChangelog().set(changelogContent);
			extension.getChangelogType().set(changelogExt);
		} else {
			extension.getChangelog().set("No Changelog Provided");
			extension.getChangelogType().set(ChangelogType.TEXT.value());
		}
	}
	
	private String readFromFile(File file) {
		try {
			return Files.asCharSource(file, Charsets.UTF_8).read();
		} catch (Exception e) {}
		return null;
	}
}
