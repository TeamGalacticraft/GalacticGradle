package net.galacticraft.plugins.curseforge;

import org.gradle.api.DefaultTask;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.plugins.curseforge.curse.ChangelogType;
import net.galacticraft.plugins.curseforge.curse.CurseVersions;
import net.galacticraft.plugins.curseforge.curse.FileArtifact;
import net.galacticraft.plugins.curseforge.curse.RelationType;
import net.galacticraft.plugins.curseforge.curse.ReleaseType;
import net.galacticraft.plugins.curseforge.curse.json.CurseError;
import net.galacticraft.plugins.curseforge.curse.json.CurseReponse;
import net.galacticraft.plugins.curseforge.curse.json.Metadata;
import net.galacticraft.plugins.curseforge.curse.json.Relations;
import net.galacticraft.plugins.curseforge.curse.json.ReturnReponse;
import net.galacticraft.plugins.curseforge.http.CFConstants;
import net.galacticraft.plugins.curseforge.http.OkHttpUtil;
import net.galacticraft.plugins.curseforge.http.PostWrapper;
import net.galacticraft.plugins.curseforge.util.ModdingPluginMap;
import net.galacticraft.plugins.curseforge.util.Util;
import okhttp3.MultipartBody;

public class CurseUploadTask extends DefaultTask {

	private final CurseUploadExtension extension = getProject().getExtensions().getByType(CurseUploadExtension.class);

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
		//List<PostWrapper> additionalFileWrappers = this.buildAdditionalFileWrappers(metadata);
		
		//Integer parentFileID;
		
		if (this.extension.getDebug().get()) {
			//additionalFileWrappers.add(0, mainFileWrapper);
			this.runDebugOutput(mainFileWrapper);
			return;
		}
		ReturnReponse returnReponse = this.upload(mainFileWrapper);
		if (returnReponse instanceof CurseReponse) {
			CurseReponse successful = (CurseReponse) returnReponse;
			this.getProject().getLogger().lifecycle("[CurseForge] File upload sucessfull");
			this.getProject().getLogger().lifecycle("[CurseForge] File Name: {}, ID: {}", mainFileWrapper.getFile().getName(), successful.getId());
			//parentFileID = successful.getId();
		} else {
			CurseError error = (CurseError) returnReponse;
			this.getProject().getLogger().lifecycle("Failed to upload file: " + mainFileWrapper.getFile().getName());
			this.getProject().getLogger().lifecycle("[CurseForge] code: {}", error.code());
			this.getProject().getLogger().lifecycle("[CurseForge] message: {}", error.message());
			//parentFileID = null;
		}
		
//		if(parentFileID != null) {
//			additionalFileWrappers.forEach(wrapper -> {
//				wrapper.getMetadata().clearGameVersions();
//				wrapper.getMetadata().parentFileID = parentFileID;
//				ReturnReponse response = this.upload(wrapper);
//				if (response instanceof CurseReponse) {
//					CurseReponse additionalSuccessful = (CurseReponse) response;
//					this.getProject().getLogger().lifecycle("[CurseForge] File upload sucessfull, Parent ID: {}", wrapper.getMetadata().parentFileID);
//					this.getProject().getLogger().lifecycle("[CurseForge] File Name: {}, ID: {}", wrapper.getFile().getName(), additionalSuccessful.getId());
//				} else {
//					CurseError error = (CurseError) response;
//					this.getProject().getLogger().lifecycle("Failed to upload file: " + wrapper.getFile().getName());
//					this.getProject().getLogger().lifecycle("[CurseForge] code: {}", error.code());
//					this.getProject().getLogger().lifecycle("[CurseForge] message: {}", error.message());
//				}
//			});
//		}
	}
	
	private void runDebugOutput(PostWrapper fileWrappers) {
		this.getProject().getLogger().lifecycle("Full data to be sent for upload:");
		
		fileWrappers.perform(wrapper -> {
			
			lifecycle("Filename: {}", wrapper.getFilename());
			lifecycle("Resolved Verion ID's: [ {} ]", String.join(", ", CurseVersions.resolvedIdsToStrings(wrapper.getMetadata().getGameVersions())));
			lifecycle("{}", wrapper.getMetadataJson());
		});
//		fileWrappers.remove(0);
//		Integer fakeParentId = 999999;
//		fileWrappers.forEach(wrapper -> {
//			lifecycle("Filename: {}", wrapper.getFilename());
//			wrapper.getMetadata().clearGameVersions();
//			wrapper.getMetadata().parentFileID(fakeParentId);
//			lifecycle("{}", wrapper.getMetadataJson());
//		});
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
	
//	private List<PostWrapper> buildAdditionalFileWrappers(Metadata metadata) {
//		List<PostWrapper> wrappers = new ArrayList<>();
//		for(FileArtifact artifact : this.extension.getExtraFiles().get()) {
//					metadata.displayName(artifact.getDisplayString() != null ? artifact.getDisplayString() : null);
//			wrappers.add(new PostWrapper(this.getProject(), artifact, metadata));
//		}
//		return wrappers;
//	}
	
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
