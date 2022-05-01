package net.galacticraft.plugins.curseforge.http;

import java.io.File;
import java.util.function.Consumer;

import org.gradle.api.Project;

import net.galacticraft.plugins.curseforge.curse.FileArtifact;
import net.galacticraft.plugins.curseforge.curse.json.Metadata;
import net.galacticraft.plugins.curseforge.util.Util;
import net.galacticraft.plugins.curseforge.util.Wrapper;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public class PostWrapper implements Wrapper<PostWrapper> {

	private final MediaType stream = MediaType.parse("application/octet-stream");
	private final MediaType json = MediaType.parse("application/json");

	private final FileArtifact artifact;
	private final Metadata metadata;

	private Part filePart;
	private Part dataPart;

	public PostWrapper(Project project, FileArtifact fileArtifact, Metadata metadata) {
		this.artifact =fileArtifact;
		this.metadata = metadata;
		createParts();
	}

	private void createParts() {
		this.filePart = Part.createFormData("file", this.artifact.getFile().getName(), RequestBody.create(this.stream, this.artifact.getFile()));
		this.dataPart = Part.createFormData("metadata", null, RequestBody.create(json, Util.getGson().toJson(metadata)));
	}
	
	public void addParentID(int parentID) {
		this.metadata.parentFileID(parentID);
	}
	
	public Metadata getMetadata() {
		return this.metadata;
	}
	
	public Part getFilePart() {
		return this.filePart;
	}
	
	public Part getMetadataPart() {
		return this.dataPart;
	}
	
	public String getMetadataJson() {
		return Util.getPrettyPrintGson().toJson(metadata);
	}
	
	public File getFile() {
		return this.artifact.getFile();
	}

	public String getFilename() {
		return this.artifact.getFilename();
	}
	
	@Override
	public void perform(Consumer<PostWrapper> action) {
		action.accept(this);
	}
}
