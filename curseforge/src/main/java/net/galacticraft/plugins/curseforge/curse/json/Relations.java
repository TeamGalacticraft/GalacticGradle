
package net.galacticraft.plugins.curseforge.curse.json;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import net.galacticraft.plugins.curseforge.curse.RelationType;

@Getter
public class Relations {

    @SerializedName("projects")
    @Expose
    private Set<CurseRelation> relations = new HashSet<CurseRelation>();

    public void addRelation(String slug, RelationType type) {

        this.relations.add(new CurseRelation().slug(slug).type(type));
    }

}
