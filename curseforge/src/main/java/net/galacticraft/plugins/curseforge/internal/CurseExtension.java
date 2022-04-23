package net.galacticraft.plugins.curseforge.internal;

import groovy.lang.Closure;
import groovy.lang.DelegatesTo;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.util.ArrayList;
import java.util.Collection;

public class CurseExtension {
    @Deprecated
    public boolean getDebug() {
        return curseGradleOptions.getDebug();
    }

    @Deprecated
    public void setDebug(boolean debug) {
        curseGradleOptions.setDebug(debug);
    }

    /**
     * Define a new CurseForge project for deployment
     *
     * @param configClosure The configuration closure
     */
    public void project(@DelegatesTo(CurseProject) Closure<CurseProject> configClosure) {
        CurseProject curseProject = new CurseProject();
        DefaultGroovyMethods.with(curseProject, configClosure);
        if (curseProject.getApiKey() == null) {
            curseProject.setApiKey(this.apiKey);
        }

        ((ArrayList<CurseProject>) curseProjects).add(curseProject);
    }

    public void options(@DelegatesTo(Options) Closure<Options> configClosure) {
        DefaultGroovyMethods.with(curseGradleOptions, configClosure);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public final ArrayList<CurseProject> getCurseProjects() {
        return curseProjects;
    }

    public Options getCurseGradleOptions() {
        return curseGradleOptions;
    }

    public void setCurseGradleOptions(Options curseGradleOptions) {
        this.curseGradleOptions = curseGradleOptions;
    }

    /**
     * Optional global apiKey. Will be applied to all projects that don't declare one
     */
    private String apiKey = "";
    private final Collection<CurseProject> curseProjects = new ArrayList<CurseProject>();
    private Options curseGradleOptions = new Options();

    private static <Value extends Value> Value setFileID(CurseArtifact propOwner, Value fileID) {
        ((CurseArtifact) propOwner).setFileID(fileID);
        return fileID;
    }
}
