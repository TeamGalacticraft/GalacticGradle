package net.galacticraft.plugins.curseforge.internal;

import com.google.common.base.Strings;
import groovy.lang.Closure;
import groovy.lang.GString;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CurseGradlePlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        this.project = project;

        final Task mainTask = project.getTasks().create(TASK_NAME, DefaultTask.class);
        mainTask.setDescription("Uploads all CurseForge projects");
        mainTask.setGroup(TASK_GROUP);

        extension = project.getExtensions().create(EXTENSION_NAME, CurseExtension.class);

        project.afterEvaluate(new Closure<List<CurseProject>>(this, this) {
            public List<CurseProject> doCall(Object it) {
                if (project.getState().getFailure() != null) {
                    project.getLogger().info("Failure detected. Not running afterEvaluate");
                }


                return DefaultGroovyMethods.each((List<CurseProject>) getExtension().getCurseProjects(), new Closure<List<CurseArtifact>>(CurseGradlePlugin.this, CurseGradlePlugin.this) {
                    public List<CurseArtifact> doCall(Object curseProject) {

                        Util.check(!Strings.isNullOrEmpty((String) ((CurseProject) curseProject).getId()), "A CurseForge project was configured without an id");

                        final CurseUploadTask uploadTask = project.getTasks().create("curseforge" + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{((CurseProject) curseProject).getId()}), CurseUploadTask.class);
                        ((CurseProject) curseProject).setUploadTask(uploadTask);
                        uploadTask.setGroup(getTASK_GROUP());
                        uploadTask.setDescription("Uploads CurseForge project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{((CurseProject) curseProject).getId()}));
                        uploadTask.setAdditionalArtifacts(((CurseProject) curseProject).getAdditionalArtifacts());
                        uploadTask.setApiKey((String) ((CurseProject) curseProject).getApiKey());
                        uploadTask.setProjectId((String) ((CurseProject) curseProject).getId());

                        CurseExtension ext = project.getExtensions().getByType(CurseExtension.class);

                        if (ext.getCurseGradleOptions().getJavaVersionAutoDetect()) {
                            Integration.checkJavaVersion(project, (CurseProject) curseProject);
                        }


                        if (ext.getCurseGradleOptions().getJavaIntegration()) {
                            Integration.checkJava(project, (CurseProject) curseProject);
                        }

                        if (ext.getCurseGradleOptions().getForgeGradleIntegration()) {
                            Integration.checkForgeGradle(project, (CurseProject) curseProject);
                        }

                        if (ext.getCurseGradleOptions().getFabricIntegration()) {
                            Integration.checkFabric(project, (CurseProject) curseProject, ext.getCurseGradleOptions().getDetectFabricApi());
                        }


                        ((CurseProject) curseProject).copyConfig();

                        uploadTask.setMainArtifact(((CurseProject) curseProject).getMainArtifact());

                        // At this stage, all artifacts should be in a state ready to upload
                        // ForgeGradle's reobf tasks are dependants of this
                        uploadTask.dependsOn(project.getTasks().getByName("assemble"));

                        // Run after build if it's on the task graph. This is useful because if tests fail,
                        // the artifacts won't be uploaded
                        uploadTask.mustRunAfter(project.getTasks().getByName("build"));

                        mainTask.dependsOn(uploadTask);
                        uploadTask.onlyIf(new Closure<Boolean>(CurseGradlePlugin.this, CurseGradlePlugin.this) {
                            public Boolean doCall(Object it) {
                                return mainTask.getEnabled();
                            }

                            public Boolean doCall() {
                                return doCall(null);
                            }

                        });

                        ((CurseProject) curseProject).validate();

                        if (((CurseProject) curseProject).getMainArtifact().getArtifact() instanceof AbstractArchiveTask) {
                            uploadTask.dependsOn((Object[]) ((CurseProject) curseProject).getMainArtifact().getArtifact());
                        }


                        return DefaultGroovyMethods.each((List<CurseArtifact>) ((CurseProject) curseProject).getAdditionalArtifacts(), new Closure<Task>(CurseGradlePlugin.this, CurseGradlePlugin.this) {
                            public Task doCall(Object artifact) {
                                if (((CurseArtifact) artifact).getArtifact() instanceof AbstractArchiveTask) {
                                    AbstractArchiveTask archiveTask = (AbstractArchiveTask) ((CurseArtifact) artifact).getArtifact();
                                    return uploadTask.dependsOn(archiveTask);
                                }

                            }

                        });
                    }

                });
            }

            public List<CurseProject> doCall() {
                return doCall(null);
            }

        });
    }

    public static String getTASK_NAME() {
        return TASK_NAME;
    }

    public static String getTASK_GROUP() {
        return TASK_GROUP;
    }

    public static String getEXTENSION_NAME() {
        return EXTENSION_NAME;
    }

    public static Set<String> getVALID_RELEASE_TYPES() {
        return VALID_RELEASE_TYPES;
    }

    public static Set<String> getVALID_RELATIONS() {
        return VALID_RELATIONS;
    }

    public static String getAPI_BASE_URL() {
        return API_BASE_URL;
    }

    public static GString getVERSION_TYPES_URL() {
        return VERSION_TYPES_URL;
    }

    public static GString getVERSION_URL() {
        return VERSION_URL;
    }

    public static GString getUPLOAD_URL() {
        return UPLOAD_URL;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public CurseExtension getExtension() {
        return extension;
    }

    public void setExtension(CurseExtension extension) {
        this.extension = extension;
    }

    private static final String TASK_NAME = "curseforge";
    private static final String TASK_GROUP = "upload";
    private static final String EXTENSION_NAME = "curseforge";
    private static final Set<String> VALID_RELEASE_TYPES = new ArrayList<String>(Arrays.asList("alpha", "beta", "release"));
    private static final Set<String> VALID_RELATIONS = new ArrayList<String>(Arrays.asList("requiredDependency", "embeddedLibrary", "optionalDependency", "tool", "incompatible"));
    private static final String API_BASE_URL = "https://minecraft.curseforge.com";
    private static final String VERSION_TYPES_URL = CurseGradlePlugin.getAPI_BASE_URL() + "/api/game/version-types";
    private static final String VERSION_URL = CurseGradlePlugin.getAPI_BASE_URL() + "/api/game/versions";
    private static final String UPLOAD_URL = CurseGradlePlugin.getAPI_BASE_URL() + "/api/projects/%s/upload-file";
    private Project project;
    private CurseExtension extension;
}
