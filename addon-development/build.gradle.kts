import net.kyori.indra.IndraExtension
import net.kyori.indra.gradle.IndraPluginPublishingExtension
import org.cadixdev.gradle.licenser.LicenseExtension

plugins {
    id("java-gradle-plugin")
    id("net.kyori.indra")
    id("net.kyori.indra.license-header")
    id("net.kyori.indra.publishing.gradle-plugin")
    id("net.kyori.indra.git")
}

dependencies {
    "compileOnlyApi"("org.checkerframework:checker-qual:3.17.0")
	compileOnly("com.google.code.gson:gson:2.8.9")
	compileOnly("org.apache.maven:apache-maven:3.8.5")
	compileOnly("net.minecraftforge.gradle.legacy:ForgeGradle:2.3")
	compileOnly("net.minecraftforge.gradle:ForgeGradle:5.1.31") {
        isTransitive = false
    }

    "testImplementation"(project(":galacticgradle-test-common"))
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
    dependsOn(tasks.test)
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
    plugin(
        "gradle.addon",
        "net.galacticraft.gradle.addon.AddonPlugin",
        "TeamGalacticraft AddonPlugin",
        "Gradle Plugin for developing Galacticraft Addons",
        listOf("galacticraft", "addon")
    )
}

