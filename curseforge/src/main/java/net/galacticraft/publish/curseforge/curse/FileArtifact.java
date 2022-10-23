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

package net.galacticraft.publish.curseforge.curse;

import java.io.File;

import javax.annotation.Nullable;

import net.galacticraft.publish.curseforge.util.Util;

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
