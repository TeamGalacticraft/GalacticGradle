dependencies {
	compileOnly("com.google.code.gson:gson:2.8.9")
	compileOnly("org.apache.maven:apache-maven:3.8.5")
	compileOnly("net.minecraftforge.gradle.legacy:ForgeGradle:2.3")
	compileOnly("net.minecraftforge.gradle:ForgeGradle:5.1.31") {
        isTransitive = false
    }
}

indraPluginPublishing {
    plugin(
        "gradle.addon",
        "net.galacticraft.gradle.addon.AddonPlugin",
        "TeamGalacticraft AddonPlugin",
        "Gradle Plugin for developing Galacticraft Addons",
        listOf("galacticraft", "addon")
    )
}