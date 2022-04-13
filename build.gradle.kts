import net.kyori.indra.IndraExtension
import net.kyori.indra.gradle.IndraPluginPublishingExtension
import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("com.gradle.plugin-publish") apply false
    id("net.kyori.indra") apply false
    id("net.kyori.indra.license-header") apply false
    id("net.kyori.indra.publishing.gradle-plugin") apply false
    id("io.freefair.lombok") apply false
}

group = "net.galacticraft"
version = "1.0.0-SNAPSHOT"

subprojects {
    plugins.apply {
        apply(JavaGradlePluginPlugin::class)
        apply("com.gradle.plugin-publish")
        apply("net.kyori.indra")
        apply("net.kyori.indra.license-header")
        apply("net.kyori.indra.publishing.gradle-plugin")
        apply("net.kyori.indra.git")
        apply("io.freefair.lombok")
    }

    repositories {
        maven("https://repo.galacticraft.net/repository/maven-public/") {
            name = "galacticraft"
        }
    }

    dependencies {
        "compileOnlyApi"("org.checkerframework:checker-qual:3.17.0")
        "implementation"("com.google.guava:guava:30.1.1-jre")
    }

    val indraGit = extensions.getByType(net.kyori.indra.git.IndraGitExtension::class)
    tasks.withType(Jar::class).configureEach {
        indraGit.applyVcsInformationToManifest(manifest)
        manifest.attributes(
            "Specification-Title" to project.name,
            "Specification-Vendor" to "TeamGalacticraft",
            "Specification-Version" to project.version,
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "TeamGalacticraft"
        )
    }

    extensions.configure(IndraExtension::class) {
        github("TeamGalacticraft", "GalacticGradle") {
            ci(true)
            publishing(true)
        }
        mitLicense()

        configurePublications {
            pom {
                developers {
                    developer {
                        name.set("TeamGalacticraft")
                        email.set("team@galacticraft.net")
                    }
                }
            }
        }

        val snapshotRepo = project.findProperty("snapshotRepo") as String?
        val releaseRepo = project.findProperty("releaseRepo") as String?
        if (releaseRepo != null && snapshotRepo != null) {
            publishSnapshotsTo("galacticraft", snapshotRepo)
            publishReleasesTo("galacticraft", releaseRepo)
        }
    }

    extensions.configure(LicenseExtension::class) {
        val organization: String by project
        val projectUrl: String by project

        properties {
            this["name"] = "GalacticGradle"
            this["organization"] = organization
            this["url"] = projectUrl
        }
        header(rootProject.file("HEADER.txt"))
    }

    extensions.configure(SigningExtension::class) {
        val signingKey = project.findProperty("signingKey") as String?
        val signingPassword = project.findProperty("signingPassword") as String?
        if (signingKey != null && signingPassword != null) {
            val keyFile = file(signingKey)
            if (keyFile.exists()) {
                useInMemoryPgpKeys(file(signingKey).readText(Charsets.UTF_8), signingPassword)
            } else {
                useInMemoryPgpKeys(signingKey, signingPassword)
            }
        } else {
            signatories = PgpSignatoryProvider() // don't use gpg agent
        }
    }

    class ExamplePlugin : Plugin<Project> {
        override fun apply(target: Project) {
            target.configure<JavaPluginExtension> {
                sourceSets {
                    println(names)
                }
            }
        }
    }

    extensions.findByType(IndraPluginPublishingExtension::class)?.apply {
        pluginIdBase("$group")
        website("https://galacticraft.net/")
    }
}
