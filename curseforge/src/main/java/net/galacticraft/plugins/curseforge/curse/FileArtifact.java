package net.galacticraft.plugins.curseforge.curse;

import java.io.File;

import javax.annotation.Nullable;

import net.galacticraft.plugins.curseforge.util.Util;

public class FileArtifact {

	private final Object file;
	private File actualFile;
	private String fileName;
	private @Nullable String displayName;

	public FileArtifact(Object file, @Nullable String displayName) {
		this.file = file;
		this.actualFile = Util.resolveFile(file);
		this.fileName = actualFile.getName();
		this.displayName = displayName;
	}
	
	public FileArtifact(Object object) {
		this.file = object;
		this.actualFile = Util.resolveFile(file);
		this.fileName = actualFile.getName();
	}

	public File getFile() {
		return this.actualFile;
	}
	
	public String getFilename() {
		return this.fileName;
	}
	
	@Nullable
	public String getDisplayString() {
		return this.displayName;
	}
}
