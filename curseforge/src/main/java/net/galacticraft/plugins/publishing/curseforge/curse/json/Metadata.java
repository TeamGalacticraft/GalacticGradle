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

package net.galacticraft.plugins.publishing.curseforge.curse.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NonNull;
import net.galacticraft.plugins.publishing.curseforge.curse.ChangelogType;
import net.galacticraft.plugins.publishing.curseforge.curse.ReleaseType;

@Getter
public class Metadata {

    @SerializedName("changelog")
    @Expose
    public String changelog;
    
    @SerializedName("changelogType")
    @Expose
    public ChangelogType changelogType;
    
    @SerializedName("displayName")
    @Expose
	public String displayName;
    
    @SerializedName("parentFileID")
    @Expose
    public Integer parentFileID = null;

    @SerializedName("gameVersions")
    @Expose
    public List<Integer> gameVersions = new ArrayList<Integer>();
    
    @SerializedName("releaseType")
    @Expose
    public ReleaseType releaseType;
    
    @SerializedName("relations")
    @Expose
    public Relations relations = null;

    @Nullable
    public Metadata changelog(String changelog) {
        this.changelog = changelog;
        return this;
    }

    @Nullable
    public Metadata changelogType(ChangelogType changelogType) {
        this.changelogType = changelogType;
        return this;
    }
    
    @Nullable
    public Metadata displayName(String displayName) {
    	this.displayName = displayName;
    	return this;
    }
    
    @Nullable
    public Metadata parentFileID(int parentFileID) {
    	this.parentFileID = parentFileID;
    	return this;
    }

    @NonNull
    public Metadata gameVersions(Integer[] gameVersions) {
    	this.gameVersions.addAll(Arrays.asList(gameVersions));
        return this;
    }

    public Metadata clearGameVersions() {
    	this.gameVersions = null;
    	return this;
    }

    @NonNull
    public Metadata releaseType(ReleaseType releaseType) {
        this.releaseType = releaseType;
        return this;
    }

    @Nullable
    public Metadata relations(Relations relations) {
        this.relations = relations;
        return this;
    }
}
