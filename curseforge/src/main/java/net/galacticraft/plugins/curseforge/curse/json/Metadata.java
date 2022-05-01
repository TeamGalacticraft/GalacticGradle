
package net.galacticraft.plugins.curseforge.curse.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NonNull;
import net.galacticraft.plugins.curseforge.curse.ChangelogType;
import net.galacticraft.plugins.curseforge.curse.ReleaseType;

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
