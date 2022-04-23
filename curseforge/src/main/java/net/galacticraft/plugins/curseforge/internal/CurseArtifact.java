package net.galacticraft.plugins.curseforge.internal;

import com.google.gson.annotations.SerializedName;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.Project;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collection;

public class CurseArtifact implements Serializable {
    public void relations(@DelegatesTo(CurseRelation) Closure<CurseRelation> configClosure) {
        CurseRelation relation = new CurseRelation();
        DefaultGroovyMethods.with(relation, configClosure);
        curseRelations = relation;
    }

    /**
     * Validate this artifact
     */
    public void validate(final Object id) {
        Util.check(artifact != null, "Artifact not configured for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}));
        Util.check(changelogType != null, "The changelogType was null for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}));
        Util.check(changelog != null, "The changelog was not set for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}));
        Util.check(releaseType != null, "The releaseType was not set for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}));
        releaseType = Util.resolveString(releaseType);
        Util.check(CurseGradlePlugin.getVALID_RELEASE_TYPES().contains(releaseType), "Invalid release type (" + getReleaseType() + ") for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{id}) + ". Valid options are: " + String.valueOf(CurseGradlePlugin.getVALID_RELEASE_TYPES()));
        DefaultGroovyMethods.each(curseRelations, new Closure<Object>(this, this) {
            public void doCall(CurseRelation it) {
                it.validate(id);
            }

            public void doCall() {
                doCall(null);
            }

        });
    }

    /**
     * Resolve metadata into their final values
     */
    public void resolve(Project project) {
        changelogType = Util.resolveString(changelogType);
        changelog = Util.resolveString(changelog);
        releaseType = Util.resolveString(releaseType);
        artifact = Util.resolveFile(project, artifact);
        if (displayName != null) {
            displayName = Util.resolveString(displayName);
        }

    }

    @Override
    public String toString() {
        return ((String) ("CurseArtifact{" + "artifact=" + artifact + ", changelogType=" + changelogType + ", changelog=" + changelog + ", displayName=" + displayName + ", releaseType='" + releaseType + "\'" + ", gameVersionStrings=" + gameVersionStrings + "}"));
    }

    public Object getArtifact() {
        return artifact;
    }

    public void setArtifact(Object artifact) {
        this.artifact = artifact;
    }

    public Collection<Object> getGameVersionStrings() {
        return gameVersionStrings;
    }

    public void setGameVersionStrings(Collection<Object> gameVersionStrings) {
        this.gameVersionStrings = gameVersionStrings;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public Object getChangelogType() {
        return changelogType;
    }

    public void setChangelogType(Object changelogType) {
        this.changelogType = changelogType;
    }

    public Object getChangelog() {
        return changelog;
    }

    public void setChangelog(Object changelog) {
        this.changelog = changelog;
    }

    public Object getDisplayName() {
        return displayName;
    }

    public void setDisplayName(Object displayName) {
        this.displayName = displayName;
    }

    public Object getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(Object releaseType) {
        this.releaseType = releaseType;
    }

    public Integer[] getGameVersions() {
        return gameVersions;
    }

    public void setGameVersions(Integer[] gameVersions) {
        this.gameVersions = gameVersions;
    }

    public Integer getParentFileID() {
        return parentFileID;
    }

    public void setParentFileID(Integer parentFileID) {
        this.parentFileID = parentFileID;
    }

    public CurseRelation getCurseRelations() {
        return curseRelations;
    }

    public void setCurseRelations(CurseRelation curseRelations) {
        this.curseRelations = curseRelations;
    }

    /**
     * The file that should be uploaded
     */
    private transient Object artifact;
    /**
     * <b>Internal Use Only</b>
     */
    @Nullable
    private transient Collection<Object> gameVersionStrings;
    /**
     * The curseforge file ID of the artifact.
     */
    private transient Integer fileID;
    /**
     * The type of changelog. At the time of writing this is: html and text
     */
    @SerializedName("changelogType")
    private Object changelogType;
    /**
     * The changelog for this artifact. The {@link Object#toString()} method will be called to get the value
     */
    @SerializedName("changelog")
    private Object changelog;
    /**
     * The user-friendly display name for this artifact. The {@link Object#toString()} method will be called to get the value
     */
    @Nullable
    @SerializedName("displayName")
    private Object displayName;
    /**
     * The release type of this artifact. See {@link CurseGradlePlugin#VALID_RELEASE_TYPES} for valid release types
     */
    @SerializedName("releaseType")
    private Object releaseType;
    /**
     * Internal use only. Will be set when game versions are resolved into their numerical representation
     */
    @Nullable
    @SerializedName("gameVersions")
    private Integer[] gameVersions;
    /**
     * Internal use only
     */
    @Nullable
    @SerializedName("parentFileID")
    private Integer parentFileID;
    @SerializedName("relations")
    private CurseRelation curseRelations;

    private static <Value extends Value> Value setFileID(CurseArtifact propOwner, Value fileID) {
        propOwner.setFileID(fileID);
        return fileID;
    }
}
