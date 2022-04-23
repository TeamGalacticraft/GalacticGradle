package net.galacticraft.plugins.curseforge.internal;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import javax.annotation.Nullable;
import java.util.*;

public class CurseProject {
    /**
     * Set the main artifact to upload
     *
     * @param artifact      The artifact
     * @param configClosure Optional configuration closure
     */
    public void mainArtifact(Object artifact, @DelegatesTo(net.galacticraft.plugins.curseforge.internal.CurseArtifact) Closure<CurseArtifact> configClosure) {
        CurseArtifact curseArtifact = new CurseArtifact();
        if (configClosure != null) {
            DefaultGroovyMethods.with(curseArtifact, configClosure);
        }

        curseArtifact.setArtifact(artifact);
        mainArtifact = curseArtifact;
    }

    /**
     * Set the main artifact to upload
     *
     * @param artifact      The artifact
     * @param configClosure Optional configuration closure
     */
    public void mainArtifact(Object artifact) {
        mainArtifact(artifact, null);
    }

    /**
     * Add an additional artifact to this project
     *
     * @param artifact      The artifact
     * @param configClosure Optional configuration closure
     */
    public void addArtifact(Object artifact, @DelegatesTo(net.galacticraft.plugins.curseforge.internal.CurseArtifact) Closure<CurseArtifact> configClosure) {
        CurseArtifact curseArtifact = new CurseArtifact();
        if (configClosure != null) {
            DefaultGroovyMethods.with(curseArtifact, configClosure);
        }

        curseArtifact.setArtifact(artifact);
        ((ArrayList<CurseArtifact>) additionalArtifacts).add(curseArtifact);
    }

    /**
     * Add an additional artifact to this project
     *
     * @param artifact      The artifact
     * @param configClosure Optional configuration closure
     */
    public void addArtifact(Object artifact) {
        addArtifact(artifact, null);
    }

    /**
     * Add a compatible game version
     *
     * @param version The version
     */
    public void addGameVersion(Object version) {
        gameVersionStrings.add(version);
    }

    /**
     * Configure CurseForge relations. Currently the only relation types are project relations
     *
     * @param configureClosure The configuration closure
     */
    public void relations(@DelegatesTo(net.galacticraft.plugins.curseforge.internal.CurseRelation) Closure<CurseRelation> configureClosure) {
        if (curseRelations == null) {
            curseRelations = new HashSet<Closure<CurseRelation>>();
        }

        curseRelations.add(configureClosure);
    }

    /**
     * Copy the default project configurations to artifacts that are missing configurations
     */
    public void copyConfig() {
        DefaultGroovyMethods.each((List<CurseArtifact>) additionalArtifacts, new Closure<Set<Closure<CurseRelation>>>(this, this) {
            public Set<Closure<CurseRelation>> doCall(final Object artifact) {
                if (((CurseArtifact) artifact).getChangelog() == null && CurseProject.this.getChangelog() != null) {
                    ((CurseArtifact) artifact).setChangelog(CurseProject.this.getChangelog());
                }

                if (((CurseArtifact) artifact).getChangelogType() == null && CurseProject.this.getChangelogType() != null) {
                    ((CurseArtifact) artifact).setChangelogType(CurseProject.this.getChangelogType());
                }

                if (((CurseArtifact) artifact).getReleaseType() == null && CurseProject.this.getReleaseType() != null) {
                    ((CurseArtifact) artifact).setReleaseType(CurseProject.this.getReleaseType());
                }

                if (getCurseRelations() != null) {
                    return DefaultGroovyMethods.each(getCurseRelations(), new Closure<Object>(CurseProject.this, CurseProject.this) {
                        public void doCall(Closure<CurseRelation> it) {
                            ((CurseArtifact) artifact).relations(it);
                        }

                        public void doCall() {
                            doCall(null);
                        }

                    });
                }

            }

        });

        if (mainArtifact != null) {
            if (mainArtifact.getReleaseType() == null && this.releaseType != null) {
                mainArtifact.setReleaseType(this.releaseType);
            }

            if (mainArtifact.getChangelogType() == null && this.changelogType != null) {
                mainArtifact.setChangelogType(this.changelogType);
            }

            if (mainArtifact.getChangelog() == null && this.changelog != null) {
                mainArtifact.setChangelog(this.changelog);
            }

            mainArtifact.setGameVersionStrings(this.gameVersionStrings);
            if (curseRelations != null) {
                DefaultGroovyMethods.each(curseRelations, new Closure<Object>(this, this) {
                    public void doCall(Closure<CurseRelation> it) {
                        getMainArtifact().relations(it);
                    }

                    public void doCall() {
                        doCall(null);
                    }

                });
            }

        }

    }

    /**
     * Validate this project and all of it's artifacts
     */
    public void validate() {
        Util.check(id != null, "Project id not set");
        Util.check(apiKey != null, "apiKey not set for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{getId()}));
        Util.check(mainArtifact != null, "mainArtifact not set for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{getId()}));
        Util.check(!gameVersionStrings.isEmpty(), "No Minecraft version configured for project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{getId()}));
        mainArtifact.validate(id);
        DefaultGroovyMethods.each((List<CurseArtifact>) additionalArtifacts, new Closure<Object>(this, this) {
            public void doCall(Object artifact) {
                ((CurseArtifact) artifact).validate(getId());
            }

        });
    }

    public CurseUploadTask getUploadTask() {
        return uploadTask;
    }

    public void setUploadTask(CurseUploadTask uploadTask) {
        this.uploadTask = uploadTask;
    }

    public CurseArtifact getMainArtifact() {
        return mainArtifact;
    }

    public void setMainArtifact(CurseArtifact mainArtifact) {
        this.mainArtifact = mainArtifact;
    }

    public final ArrayList<CurseArtifact> getAdditionalArtifacts() {
        return additionalArtifacts;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(Object releaseType) {
        this.releaseType = releaseType;
    }

    public String getChangelogType() {
        return changelogType;
    }

    public void setChangelogType(String changelogType) {
        this.changelogType = changelogType;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public Object getApiKey() {
        return apiKey;
    }

    public void setApiKey(Object apiKey) {
        this.apiKey = apiKey;
    }

    public ArrayList<Object> getGameVersionStrings() {
        return gameVersionStrings;
    }

    public void setGameVersionStrings(List<Object> gameVersionStrings) {
        this.gameVersionStrings = gameVersionStrings;
    }

    public Set<Closure<CurseRelation>> getCurseRelations() {
        return curseRelations;
    }

    public void setCurseRelations(Set<Closure<CurseRelation>> curseRelations) {
        this.curseRelations = curseRelations;
    }

    /**
     * The gradle task that will upload this project
     */
    private CurseUploadTask uploadTask;
    /**
     * The main artifact that will be uploaded to CurseForge
     * <p>
     * Valid artifact types include:
     * <ol>
     *     <li>{@link File} objects</li>
     *     <li>A string path to a file</li>
     *     <li>Gradle {@link AbstractArchiveTask archive tasks}</li>
     * </ol>
     * </p>
     */
    private CurseArtifact mainArtifact;
    /**
     * The collection of additional artifacts that should be uploaded
     */
    private final Collection<CurseArtifact> additionalArtifacts = new ArrayList<CurseArtifact>();
    /**
     * The project ID on curseforge
     */
    private Object id;
    /**
     * The default release type for this project's artifacts
     */
    @Nullable
    private Object releaseType;
    /**
     * The type of changelog. At the time of writing this is: html and text
     */
    private String changelogType = "text";
    /**
     * The default changelog for this project's artifacts
     */
    private String changelog = "";
    /**
     * The API key to be used for file uploads
     */
    private Object apiKey;
    private List<Object> gameVersionStrings = new ArrayList<Object>();
    @Nullable
    private Set<Closure<CurseRelation>> curseRelations;

    private static <Value extends Value> Value setFileID(CurseArtifact propOwner, Value fileID) {
        ((CurseArtifact) propOwner).setFileID(fileID);
        return fileID;
    }
}
