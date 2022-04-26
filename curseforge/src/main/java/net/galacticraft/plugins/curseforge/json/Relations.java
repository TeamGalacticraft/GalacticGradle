
package net.galacticraft.plugins.curseforge.json;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class Relations {

    @SerializedName("projects")
    @Expose
    public List<CurseProject> projects = new ArrayList<CurseProject>();

    public Relations projects(List<CurseProject> projects) {
        this.projects = projects;
        return this;
    }

}
