dependencies {
    api(project(":galacticgradle-addon-development"))
    api("net.kyori:indra-common:2.0.6")
    api("gradle.plugin.org.cadixdev.gradle:licenser:0.6.1")
    api("com.google.code.gson:gson:2.8.9")
}

indraPluginPublishing {
    plugin(
        "gradle.dev",
        "net.galacticraft.gradle.convention.ConventionPlugin",
        "TeamGalacticraft Convention",
        "Gradle conventions for TeamGalacticraft organization projects",
        listOf("galacticraft", "convention")
    )
}