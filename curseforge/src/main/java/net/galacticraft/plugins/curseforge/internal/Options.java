package net.galacticraft.plugins.curseforge.internal;

/**
 * Various options for CurseGradle. These affect the entire plugin and not just a single curse project.
 */
public class Options {
    public boolean getDebug() {
        return debug;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getJavaVersionAutoDetect() {
        return javaVersionAutoDetect;
    }

    public boolean isJavaVersionAutoDetect() {
        return javaVersionAutoDetect;
    }

    public void setJavaVersionAutoDetect(boolean javaVersionAutoDetect) {
        this.javaVersionAutoDetect = javaVersionAutoDetect;
    }

    public boolean getDetectNewerJava() {
        return detectNewerJava;
    }

    public boolean isDetectNewerJava() {
        return detectNewerJava;
    }

    public void setDetectNewerJava(boolean detectNewerJava) {
        this.detectNewerJava = detectNewerJava;
    }

    public boolean getJavaIntegration() {
        return javaIntegration;
    }

    public boolean isJavaIntegration() {
        return javaIntegration;
    }

    public void setJavaIntegration(boolean javaIntegration) {
        this.javaIntegration = javaIntegration;
    }

    public boolean getForgeGradleIntegration() {
        return forgeGradleIntegration;
    }

    public boolean isForgeGradleIntegration() {
        return forgeGradleIntegration;
    }

    public void setForgeGradleIntegration(boolean forgeGradleIntegration) {
        this.forgeGradleIntegration = forgeGradleIntegration;
    }

    public boolean getFabricIntegration() {
        return fabricIntegration;
    }

    public boolean isFabricIntegration() {
        return fabricIntegration;
    }

    public void setFabricIntegration(boolean fabricIntegration) {
        this.fabricIntegration = fabricIntegration;
    }

    public boolean getDetectFabricApi() {
        return detectFabricApi;
    }

    public boolean isDetectFabricApi() {
        return detectFabricApi;
    }

    public void setDetectFabricApi(boolean detectFabricApi) {
        this.detectFabricApi = detectFabricApi;
    }

    /**
     * Debug mode will stop just short of actually uploading the file to Curse, and instead spit out the JSON
     * to the console. Useful for testing your buildscript.
     */
    private boolean debug = false;
    /**
     * If this is left enabled, CurseGradle will automatically detect the compatible versions of Java for the project
     * and add them to the CurseForge metadata.
     */
    private boolean javaVersionAutoDetect = true;
    /**
     * Enables Java version auto detection for Java 9 and beyond. Only applicable if {@link #javaVersionAutoDetect}
     * is enabled.
     */
    private boolean detectNewerJava = false;
    /**
     * Enable integration with the Gradle Java plugin. This includes setting the default artifact to the jar task.
     */
    private boolean javaIntegration = true;
    /**
     * Enable integration with the ForgeGradle plugin. This includes setting dependencies on the reobfuscation tasks.
     */
    private boolean forgeGradleIntegration = true;
    /**
     * Enable integration with the Fabric. This does not add the fabric-api as a dependency
     */
    private boolean fabricIntegration = true;
    /**
     * Detect if the project uses the Fabric API and add it as a required dependency to the main artifact
     */
    private boolean detectFabricApi = true;
}
