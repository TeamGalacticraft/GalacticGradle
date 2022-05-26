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

package net.galacticraft.plugins.publishing.curseforge.http;

import java.io.File;
import java.util.function.Consumer;

import org.gradle.api.Project;

import net.galacticraft.plugins.publishing.curseforge.curse.FileArtifact;
import net.galacticraft.plugins.publishing.curseforge.curse.json.Metadata;
import net.galacticraft.plugins.publishing.curseforge.util.Util;
import net.galacticraft.plugins.publishing.curseforge.util.Wrapper;
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
