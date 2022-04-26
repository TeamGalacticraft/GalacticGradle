
package net.galacticraft.plugins.curseforge.json;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class CurseProject {

    @SerializedName("slug")
    @Expose
    public String slug;
    
    @SerializedName("type")
    @Expose
    public CurseProject.Type type;

    @NonNull
    public CurseProject slug(String slug) {
        this.slug = slug;
        return this;
    }

    @NonNull
    public CurseProject type(CurseProject.Type type) {
        this.type = type;
        return this;
    }

    public enum Type {

        @SerializedName("embeddedLibrary")
        EMBEDDED_LIBRARY("embeddedLibrary"),
        @SerializedName("incompatible")
        INCOMPATIBLE("incompatible"),
        @SerializedName("optionalDependency")
        OPTIONAL_DEPENDENCY("optionalDependency"),
        @SerializedName("requiredDependency")
        REQUIRED_DEPENDENCY("requiredDependency"),
        @SerializedName("tool")
        TOOL("tool");
        
        private final String value;
        private final static Map<String, CurseProject.Type> CONSTANTS = new HashMap<String, CurseProject.Type>();

        static {
            for (CurseProject.Type c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CurseProject.Type fromValue(String value) {
            CurseProject.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
