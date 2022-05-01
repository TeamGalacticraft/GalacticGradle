
package net.galacticraft.plugins.curseforge.curse.json;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NonNull;
import net.galacticraft.plugins.curseforge.curse.RelationType;

@Getter
public class CurseRelation {

    @SerializedName("slug")
    @Expose
    private String slug;
    
    @SerializedName("type")
    @Expose
    private RelationType type;

    @NonNull
    public CurseRelation slug(String slug) {
        this.slug = slug;
        return this;
    }

    @NonNull
    public CurseRelation type(RelationType type) {
        this.type = type;
        return this;
    }
}
