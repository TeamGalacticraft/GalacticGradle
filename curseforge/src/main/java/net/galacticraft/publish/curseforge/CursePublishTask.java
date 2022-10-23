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

package net.galacticraft.publish.curseforge;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.publish.curseforge.curse.ChangelogType;
import net.galacticraft.publish.curseforge.curse.CurseVersions;
import net.galacticraft.publish.curseforge.curse.FileArtifact;
import net.galacticraft.publish.curseforge.curse.RelationType;
import net.galacticraft.publish.curseforge.curse.ReleaseType;
import net.galacticraft.publish.curseforge.curse.json.CurseError;
import net.galacticraft.publish.curseforge.curse.json.CurseReponse;
import net.galacticraft.publish.curseforge.curse.json.Metadata;
import net.galacticraft.publish.curseforge.curse.json.Relations;
import net.galacticraft.publish.curseforge.curse.json.ReturnReponse;
import net.galacticraft.publish.curseforge.http.CFConstants;
import net.galacticraft.publish.curseforge.http.OkHttpUtil;
import net.galacticraft.publish.curseforge.http.PostWrapper;
import net.galacticraft.publish.curseforge.util.ModdingPluginMap;
import net.galacticraft.publish.curseforge.util.Util;
import okhttp3.MultipartBody;

public class CursePublishTask extends DefaultTask {

	private final CurseExtension extension = getProject().getExtensions().getByType(CurseExtension.class);

	private @NotNull ModdingPluginMap pluginMap = ModdingPluginMap.instance;

	@TaskAction
	public void apply() {
		Util.check(extension.getApiKey().getOrNull() != null, "CurseForge Project does not have an apiKey configured");

		this.detectModdingPlugins();
		
		Metadata metadata = new Metadata()
				.releaseType(ReleaseType.fromValue(extension.getReleaseType().get()))
				.changelog(this.extension.getChangelog().getOrElse("No changelog provided"))
				.changelogType(ChangelogType.fromValue(extension.getChangelogType().getOrNull()))
				.gameVersions(CurseVersions.resolveGameVersion(extension.getGameVersions().get()));

		PostWrapper mainFileWrapper = this.buildMainFileWrapper(metadata);
		
		if (this.extension.getDebug().get()) {
			this.runDebugOutput(mainFileWrapper);
			return;
		}
		ReturnReponse returnReponse = this.upload(mainFileWrapper);
		if (returnReponse instanceof CurseReponse) {
			CurseReponse successful = (CurseReponse) returnReponse;
			this.getProject().getLogger().lifecycle("[CurseForge] File upload sucessfull");
			this.getProject().getLogger().lifecycle("[CurseForge] File Name: {}, ID: {}", mainFileWrapper.getFile().getName(), successful.getId());
		} else {
			CurseError error = (CurseError) returnReponse;
			this.getProject().getLogger().lifecycle("Failed to upload file: " + mainFileWrapper.getFile().getName());
			this.getProject().getLogger().lifecycle("[CurseForge] code: {}", error.code());
			this.getProject().getLogger().lifecycle("[CurseForge] message: {}", error.message());
		}
	}
	
	private void runDebugOutput(PostWrapper fileWrappers) {
		this.getProject().getLogger().lifecycle("Full data to be sent for upload:");
		
		fileWrappers.perform(wrapper -> {
			
			lifecycle("Filename: {}", wrapper.getFilename());
			lifecycle("Resolved Verion ID's: [ {} ]", String.join(", ", CurseVersions.resolvedIdsToStrings(wrapper.getMetadata().getGameVersions())));
			lifecycle("{}", wrapper.getMetadataJson());
		});
	}
	
	private void lifecycle(String format, Object... objs) {
		this.getProject().getLogger().lifecycle(format, objs);
	}
	
	private PostWrapper buildMainFileWrapper(Metadata metadata) {
		final FileArtifact mainFileArtifact = this.extension.getMainFile().get();
		metadata.releaseType(ReleaseType.fromValue(extension.getReleaseType().get()))
				.displayName(mainFileArtifact.getDisplayString() != null ? mainFileArtifact.getDisplayString() : null);
		this.addRelations(metadata);
		return new PostWrapper(this.getProject(), mainFileArtifact, metadata);
	}
	
	private void addRelations(Metadata metadata) {
		if(!extension.getRelationsContainer().getDependencies().isEmpty()) {
			Relations relations = new Relations();
			extension.getRelationsContainer().getDependencies().forEach(dep -> {
				relations.addRelation(dep.getName(), RelationType.fromValue(dep.getType().get()));
			});
			metadata.relations = relations;
		}
	}

	private ReturnReponse upload(PostWrapper wrapper) {
		String uploadUrl = String.format(CFConstants.UPLOAD_URL, extension.getProjectId().get());

		MultipartBody.Builder builder = new MultipartBody.Builder()
				.addPart(wrapper.getMetadataPart())
				.addPart(wrapper.getFilePart())
				.setType(MultipartBody.FORM);

		return OkHttpUtil.instance.post(builder, uploadUrl);
	}

	private void detectModdingPlugins() {
		if(this.extension.getGameVersions().get().isEmpty()) {
			this.pluginMap.runCheck(this.getProject(), this.extension);
			final Configuration minecraft = this.getProject().getConfigurations().findByName("minecraft");
			final Configuration modImplementation = this.getProject().getConfigurations().findByName("modImplementation");

			if (minecraft != null && this.extension.getGameVersions().get().contains("Forge")) {
				Util.runForgeCheck(minecraft, this.getProject(), this.extension);
			}

			if (minecraft != null && modImplementation != null
					&& this.extension.getGameVersions().get().contains("Fabric")) {
				Util.runFabricCheck(minecraft, modImplementation, this.getProject(), this.extension);
			}	
		}
	}
}
