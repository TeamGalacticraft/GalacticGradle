pluginManagement {
    repositories {
        maven("https://repo.galacticraft.net/repository/maven-public/") {
            name = "galacticraft"
        }
    }

    plugins {
        val lombokVersion = "6.4.1"
        val indraVersion = "2.0.6"
        id("com.gradle.plugin-publish") version "0.20.0"
        id("net.kyori.indra") version indraVersion
        id("net.kyori.indra.license-header") version indraVersion
        id("net.kyori.indra.publishing.gradle-plugin") version indraVersion
        id("io.freefair.lombok") version lombokVersion
    }
}

rootProject.name = "GalacticGradle"

sequenceOf("test-common", "addon-development", "convention").forEach {
    include(it)
    findProject(":$it")?.name = "${rootProject.name.toLowerCase(java.util.Locale.ROOT)}-$it"
}
