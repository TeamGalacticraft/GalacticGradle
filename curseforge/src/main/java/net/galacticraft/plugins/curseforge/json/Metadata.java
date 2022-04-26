
package net.galacticraft.plugins.curseforge.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class Metadata {

    @SerializedName("changelog")
    @Expose
    public String changelog;
    
    @SerializedName("changelogType")
    @Expose
    public Metadata.ChangelogType changelogType;

    @SerializedName("gameVersions")
    @Expose
    public List<Object> gameVersions = new ArrayList<Object>();
    
    @SerializedName("releaseType")
    @Expose
    public Metadata.ReleaseType releaseType;
    
    @SerializedName("relations")
    @Expose
    public Relations relations = new Relations();

    @Nullable
    public Metadata changelog(String changelog) {
        this.changelog = changelog;
        return this;
    }

    @Nullable
    public Metadata changelogType(Metadata.ChangelogType changelogType) {
        this.changelogType = changelogType;
        return this;
    }

    @NonNull
    public Metadata gameVersions(Object[] gameVersions) {
    	this.gameVersions.addAll(Arrays.asList(gameVersions));
        return this;
    }

    @NonNull
    public Metadata releaseType(Metadata.ReleaseType releaseType) {
        this.releaseType = releaseType;
        return this;
    }

    @Nullable
    public Metadata relations(Relations relations) {
        this.relations = relations;
        return this;
    }

    public enum ChangelogType {

        @SerializedName("text")
        TEXT("text"),
        @SerializedName("html")
        HTML("html"),
        @SerializedName("markdown")
        MARKDOWN("markdown");
        private final String value;
        private final static Map<String, Metadata.ChangelogType> CONSTANTS = new HashMap<String, Metadata.ChangelogType>();

        static {
            for (Metadata.ChangelogType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ChangelogType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Metadata.ChangelogType fromValue(String value) {
            Metadata.ChangelogType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ReleaseType {

        @SerializedName("alpha")
        ALPHA("alpha"),
        @SerializedName("beta")
        BETA("beta"),
        @SerializedName("release")
        RELEASE("release");
        private final String value;
        private final static Map<String, Metadata.ReleaseType> CONSTANTS = new HashMap<String, Metadata.ReleaseType>();

        static {
            for (Metadata.ReleaseType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ReleaseType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Metadata.ReleaseType fromValue(String value) {
            Metadata.ReleaseType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }
    }
}
