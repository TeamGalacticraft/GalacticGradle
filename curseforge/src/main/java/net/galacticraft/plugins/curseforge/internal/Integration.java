package net.galacticraft.plugins.curseforge.internal;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.JavaVersion;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.plugins.JavaPluginExtension;

public class Integration {
    public static void checkJava(Project project, CurseProject curseProject) {
        try {
            if (project.getPlugins().hasPlugin("java")) {
                log.info("Java plugin detected, adding integration...");
                if (curseProject.getMainArtifact() == null) {
                    Task jarTask = project.getTasks().getByName("jar");
                    log.info("Setting main artifact for CurseForge Project " + DefaultGroovyMethods.invokeMethod(String.class, "valueOf", new Object[]{curseProject.getId()}) + " to the java jar");
                    CurseArtifact artifact = new CurseArtifact();
                    artifact.setArtifact(jarTask);
                    curseProject.setMainArtifact(artifact);
                    curseProject.getUploadTask().dependsOn(jarTask);
                }

            }

        } catch (Throwable t) {
            log.warn("Failed Java integration", t);
        }

    }

    public static void checkJavaVersion(Project project, CurseProject curseProject) {

        try {
            JavaPluginExtension javaConv = (JavaPluginExtension) project.getExtensions().findByName("java");
            JavaVersion javaVersion = JavaVersion.toVersion(javaConv.getTargetCompatibility());

            if (JavaVersion.VERSION_1_6.compareTo(javaVersion) >= 0) {
                curseProject.addGameVersion("Java 6");
            }

            if (JavaVersion.VERSION_1_7.compareTo(javaVersion) >= 0) {
                curseProject.addGameVersion("Java 7");
            }

            if (JavaVersion.VERSION_1_8.compareTo(javaVersion) >= 0) {
                curseProject.addGameVersion("Java 8");
            }

            if (project.getExtensions().getByType(CurseExtension.class).getCurseGradleOptions().getDetectNewerJava()) {
                if (JavaVersion.VERSION_1_9.compareTo(javaVersion) >= 0) {
                    curseProject.addGameVersion("Java 9");
                }

                if (JavaVersion.VERSION_16.compareTo(javaVersion) >= 0) {
                    curseProject.addGameVersion("Java 16");
                }

                if (JavaVersion.VERSION_17.compareTo(javaVersion) >= 0) {
                    curseProject.addGameVersion("Java 17");
                }

            }


        } catch (Throwable t) {
            log.warn("Failed to check Java Version", t);
        }

    }

    public static void checkForgeGradle(final Project project, final CurseProject curseProject) {
        if (project.hasProperty("minecraft")) {
            log.info("ForgeGradle plugin detected, adding integration...");

            // FG 3+ doesn't set MC_VERSION until afterEvaluate
            project.getGradle().getTaskGraph().whenReady(new Closure<Object>(null, null) {
                public void doCall(Object it) {
                    try {
                        if (DefaultGroovyMethods.asBoolean(DefaultGroovyMethods.hasProperty(project.minecraft, "version"))) {
                            log.info("Found Minecraft version in FG < 3");
                            curseProject.addGameVersion(project.minecraft.version);
                            curseProject.addGameVersion("Forge");
                        } else if (project.getExtensions().getExtraProperties().has("MC_VERSION")) {
                            log.info("Found Minecraft version in FG >= 3");
                            curseProject.addGameVersion(project.getExtensions().getExtraProperties().get("MC_VERSION"));
                            curseProject.addGameVersion("Forge");
                        } else {
                            log.warn("Unable to extract Minecraft version from ForgeGradle");
                        }

                    } catch (Throwable t) {
                        log.warn("Failed ForgeGradle integration", t);
                    }

                }

                public void doCall() {
                    doCall(null);
                }

            });
        }

    }

    public static void checkFabric(final Project project, final CurseProject curseProject, final boolean detectApi) {
        if (DefaultGroovyMethods.hasProperty(project.getConfigurations(), "minecraft") && DefaultGroovyMethods.hasProperty(project.getConfigurations(), "mappings")) {
            log.info("Fabric mod detected, adding integration...");
            curseProject.addGameVersion("Fabric");

            project.getGradle().getTaskGraph().whenReady(new Closure<Object>(null, null) {
                public void doCall(Object it) {
                    try {
                        String mcver = DefaultGroovyMethods.getAt(project.getConfigurations().minecraft.getAllDependencies(), 0).getVersion();
                        log.info("Found Minecraft Version: " + mcver);
                        curseProject.addGameVersion(mcver);

                        // Check for Fabric API and add dependency
                        if (DefaultGroovyMethods.hasProperty(project.getConfigurations(), "modImplementation") && detectApi) {
                            if (DefaultGroovyMethods.asBoolean(DefaultGroovyMethods.find(project.getConfigurations().modImplementation.getAllDependencies(), new Closure<Boolean>(null, null) {
                                public Boolean doCall(Dependency it) {
                                    return it.getName().equals("fabric-api");
                                }

                                public Boolean doCall() {
                                    return doCall(null);
                                }

                            }))) {
                                String fabricApiVer = DefaultGroovyMethods.find(project.getConfigurations().modImplementation.getAllDependencies(), new Closure<Boolean>(null, null) {
                                    public Boolean doCall(Dependency it) {
                                        return it.getName().equals("fabric-api");
                                    }

                                    public Boolean doCall() {
                                        return doCall(null);
                                    }

                                }).getVersion();
                                log.info("Found Fabric API Version: " + fabricApiVer);
                                if (curseProject.getMainArtifact().getCurseRelations() != null) {
                                    curseProject.getMainArtifact().getCurseRelations().requiredDependency("fabric-api");
                                } else {
                                    CurseRelation curseRelation = new CurseRelation();
                                    curseRelation.requiredDependency("fabric-api");
                                    curseProject.getMainArtifact().setCurseRelations(curseRelation);
                                }

                            }

                        }

                    } catch (Throwable t) {
                        log.warn("Failed Fabric integration", t);
                    }

                }

                public void doCall() {
                    doCall(null);
                }

            });
        }

    }

    private static final Logger log = Logging.getLogger(Integration.class);

    private static <Value extends Value> Value setFileID(CurseArtifact propOwner, Value fileID) {
        ((CurseArtifact) propOwner).setFileID(fileID);
        return fileID;
    }
}
