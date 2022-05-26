/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.plugins.convention;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.cadixdev.gradle.licenser.LicenseExtension;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.PasswordCredentials;
import org.gradle.api.java.archives.Attributes;
import org.gradle.api.java.archives.Manifest;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenPublication;
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.compile.JavaCompile;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.logging.TestExceptionFormat;
import org.gradle.api.tasks.testing.logging.TestLoggingContainer;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.gradle.plugins.signing.SigningExtension;
import org.gradle.plugins.signing.SigningPlugin;
import org.gradle.plugins.signing.signatory.pgp.PgpSignatoryProvider;

import net.galacticraft.common.Constants;
import net.galacticraft.common.plugins.GradlePlugin;
import net.galacticraft.common.util.VersionUtil;
import net.galacticraft.plugins.convention.model.Developer;
import net.galacticraft.plugins.convention.task.SignJarTask;
import net.kyori.indra.Indra;
import net.kyori.indra.IndraExtension;
import net.kyori.indra.IndraLicenseHeaderPlugin;
import net.kyori.indra.IndraPlugin;
import net.kyori.indra.api.model.ContinuousIntegration;
import net.kyori.indra.api.model.Issues;
import net.kyori.indra.api.model.SourceCodeManagement;
import net.kyori.indra.git.GitPlugin;
import net.kyori.indra.git.IndraGitExtension;
import net.kyori.indra.repository.RemoteRepository;

public class ConventionPlugin extends GradlePlugin {

	private @MonotonicNonNull Project project;
	private @MonotonicNonNull String NAME = "teamgc";

	@Override
	public void plugin() {
		this.applyPlugins();

		final LicenseExtension license = extensions().find(LicenseExtension.class);
		final IndraExtension indra = Indra.extension(extensions().container());
		final JavaPluginExtension java = extensions().find(JavaPluginExtension.class);
		final ConventionExtension galacticraft = extensions().findOrCreate(NAME, ConventionExtension.class, indra,
				license, java);
		indra.includeJavaSoftwareComponentInPublications(true);

		this.publicationMetadata();
		this.standardJavaTasks();
		if (plugins().hasPlugin(IndraLicenseHeaderPlugin.class)) {
			this.licenseHeaders(license);
		}
		this.configureJarSigning();

		plugins().withType(SigningPlugin.class, $ -> project
				.afterEvaluate(p -> this.configureSigning(p.getExtensions().getByType(SigningExtension.class))));

		final Manifest manifest = galacticraft.combinedManifest();
		tasks().withType(Jar.class).configureEach(task -> task.getManifest().from(manifest));
		project.afterEvaluate(proj -> {
			this.jarTasks(manifest, proj.getExtensions().getByType(IndraGitExtension.class));
		});
	}

	private void jarTasks(final Manifest manifest, final IndraGitExtension git) {
		final Attributes attributes = manifest.getAttributes();
		attributes.putIfAbsent("Specification-Title", project().getName());
		attributes.putIfAbsent("Specification-Vendor", Constants.GITHUB_ORG);
		attributes.putIfAbsent("Specification-Version", project().getVersion());
		attributes.putIfAbsent("Implementation-Title", project().getName());
		attributes.putIfAbsent("Implementation-Vendor", Constants.GITHUB_ORG);
		attributes.putIfAbsent("Implementation-Version", project().getVersion());
		git.applyVcsInformationToManifest(manifest);
	}

	private void standardJavaTasks() {
		tasks().withType(JavaCompile.class).configureEach(compile -> {
			compile.getOptions().getCompilerArgs().addAll(Arrays.asList("-Xmaxerrs", "1000"));
		});

		tasks().withType(Test.class).configureEach(test -> {
			final TestLoggingContainer testLogging = test.getTestLogging();
			testLogging.setExceptionFormat(TestExceptionFormat.FULL);
			testLogging.setShowStandardStreams(true);
			testLogging.setShowStackTraces(true);
		});
	}

	private void applyPlugins() {
		applyPlugin(IndraPlugin.class);
		applyPlugin(MavenPublishPlugin.class);
		if (project().file(ConventionConstants.License.HEADER_FILE).exists()) {
			applyPlugin(IndraLicenseHeaderPlugin.class);
		}
		applyPlugin(GitPlugin.class);
	}

	private void publicationMetadata() {
		ConventionExtension extension = extensions().find(ConventionExtension.class);
		IndraExtension indra = extensions().find(IndraExtension.class);
		project.getGradle().projectsEvaluated(configure -> {
			project.getExtensions().configure(PublishingExtension.class, publishing -> {
				publishing.publications(publication -> {
					publication.create(project.getName().toLowerCase(), MavenPublication.class, maven -> {
						maven.setArtifactId(project.getName().toLowerCase());
						maven.setGroupId(project.getGroup().toString());
						maven.setVersion(project.getVersion().toString());
						maven.pom(pom -> {
							pom.developers(developers -> {
								for (Developer dev : extension.developers()) {
									developers.developer(developer -> {
										developer.getId().set(dev.id());
										developer.getName().set(dev.name());
										developer.getEmail().set(dev.email());
										developer.getRoles().set(dev.roles());
									});
								}
								developers.developer(developer -> {
									developer.getName().set("TeamGalacticraft");
									developer.getEmail().set("team@galacticraft.net");
								});
							});

							pom.organization(organization -> {
								organization.getName().set(Constants.GITHUB_ORG);
								organization.getUrl().set("https://galacticraft.net/");
							});

							if (indra.issues().isPresent()) {
								pom.issueManagement(issues -> {
									Issues i = indra.issues().get();
									issues.getSystem().set(i.system());
									issues.getUrl().set(i.url());
								});
							}
							if (indra.scm().isPresent()) {
								pom.scm(scm -> {
									SourceCodeManagement s = indra.scm().get();
									scm.getConnection().set(s.connection());
									scm.getDeveloperConnection().set(s.developerConnection());
									scm.getUrl().set(s.connection());
								});
							}
							if (indra.ci().isPresent()) {
								pom.ciManagement(ci -> {
									ContinuousIntegration i = indra.ci().get();
									ci.getSystem().set(i.system());
									ci.getUrl().set(i.url());
								});
							}
						});
					});
				});

				final @Nullable String galacticSnapshotsRepo = (String) project()
						.findProperty(ConventionConstants.PropertyAttributes.GALACTIC_SNAPSHOT_REPO);
				final @Nullable String galacticReleasesRepo = (String) project()
						.findProperty(ConventionConstants.PropertyAttributes.GALACTIC_RELEASE_REPO);

				final Set<RemoteRepository> repositories = new HashSet<RemoteRepository>();

				if (galacticReleasesRepo != null && galacticSnapshotsRepo != null) {
					repositories.add(RemoteRepository.snapshotsOnly("GalacticSnapshots", galacticSnapshotsRepo));
					repositories.add(RemoteRepository.releasesOnly("GalacticReleases", galacticReleasesRepo));
				} else {
					repositories.add(RemoteRepository.snapshotsOnly("GalacticSnapshots",
							ConventionConstants.Repositories.SNAPSHOTS));
					repositories.add(RemoteRepository.releasesOnly("GalacticReleases",
							ConventionConstants.Repositories.RELEASES));
				}

				repositories.forEach(r -> {
					if (this.canPublishTo(project, r)) {
						publishing.getRepositories().maven(repo -> {
							repo.setName(r.name());
							repo.setUrl(r.url());
							repo.credentials(PasswordCredentials.class);
						});
					}
				});
			});
		});
	}

	private boolean canPublishTo(final Project project, final RemoteRepository repository) {
		// as per PasswordCredentials
		final String username = "TeamGCUsername";
		final String password = "TeamGCPassword";

		if (!project.hasProperty(username))
			return false;

		if (!project.hasProperty(password))
			return false;

		if (repository.releases() && VersionUtil.isRelease(project))
			return true;
		if (repository.snapshots() && VersionUtil.isSnapshot(project))
			return true;

		return false;
	}

	private void licenseHeaders(final LicenseExtension licenses) {
		licenses.setHeader(project().getRootProject().file(ConventionConstants.License.HEADER_FILE));
		licenses.properties(ext -> {
			ext.set("name", project().getRootProject().getName());
		});
	}

	private void configureSigning(final SigningExtension extension) {
		final String spongeSigningKey = (String) project()
				.findProperty(ConventionConstants.PropertyAttributes.GALACTIC_SIGNING_KEY);
		final String spongeSigningPassword = (String) project()
				.findProperty(ConventionConstants.PropertyAttributes.GALACTIC_SIGNING_PASSWORD);
		if (spongeSigningKey != null && spongeSigningPassword != null) {
			final File keyFile = project().file(spongeSigningKey);
			if (keyFile.exists()) {
				final StringBuilder contents = new StringBuilder();
				try (final BufferedReader reader = new BufferedReader(
						new InputStreamReader(new FileInputStream(keyFile), StandardCharsets.UTF_8))) {
					final char[] buf = new char[2048];
					int read;
					while ((read = reader.read(buf)) != -1) {
						contents.append(buf, 0, read);
					}
				} catch (final IOException ex) {
					throw new GradleException("Failed to read Sponge key file", ex);
				}
				extension.useInMemoryPgpKeys(contents.toString(), spongeSigningPassword);
			} else {
				extension.useInMemoryPgpKeys(spongeSigningKey, spongeSigningPassword);
			}
		} else {
			extension.setSignatories(new PgpSignatoryProvider()); // don't use gpg agent
		}
	}

	private void configureJarSigning() {
		if (!project().hasProperty(ConventionConstants.PropertyAttributes.GALACTIC_KEY_STORE)) {
			return;
		}
		final String[] outgoingConfigurations = { JavaPlugin.API_ELEMENTS_CONFIGURATION_NAME,
				JavaPlugin.RUNTIME_ELEMENTS_CONFIGURATION_NAME };
		final String keyStoreProp = (String) project()
				.property(ConventionConstants.PropertyAttributes.GALACTIC_KEY_STORE);
		final File fileTemp = new File(keyStoreProp);
		final File keyStoreFile;
		if (fileTemp.exists()) {
			keyStoreFile = fileTemp;
		} else {
			final Path dest = project().getLayout().getProjectDirectory().file(".gradle/signing-key").getAsFile()
					.toPath();
			try {
				Files.createDirectories(dest.getParent());
				Files.createFile(dest, PosixFilePermissions.asFileAttribute(
						new HashSet<>(Arrays.asList(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE))));
			} catch (final IOException ignored) {
				// oh well
			}
			final byte[] decoded = Base64.getDecoder().decode(keyStoreProp);
			try (final OutputStream out = Files.newOutputStream(dest)) {
				out.write(decoded);
			} catch (final IOException ex) {
				throw new GradleException("Unable to write key file to disk", ex);
			}
			keyStoreFile = dest.toFile();
			keyStoreFile.deleteOnExit();
		}

		tasks().matching(it -> it.getName().equals("jar") && it instanceof Jar).whenTaskAdded(task -> {
			final Jar jarTask = (Jar) task;
			jarTask.getArchiveClassifier().set("unsigned");
			final TaskProvider<SignJarTask> sign = tasks().register("signJar", SignJarTask.class, config -> {
				config.dependsOn(jarTask);
				config.from(project().zipTree(jarTask.getOutputs().getFiles().getSingleFile()));
				config.setManifest(jarTask.getManifest());
				config.getArchiveClassifier().set("");
				config.getKeyStore().set(keyStoreFile);
				config.getAlias().set(
						(String) project().property(ConventionConstants.PropertyAttributes.GALACTIC_KEY_STORE_ALIAS));
				config.getStorePassword().set((String) project()
						.property(ConventionConstants.PropertyAttributes.GALACTIC_KEY_STORE_PASSWORD));
			});

			for (final String configName : outgoingConfigurations) {
				project().getConfigurations().named(configName, conf -> {
					conf.getOutgoing().artifact(sign);
				});
			}

			tasks().named(LifecycleBasePlugin.ASSEMBLE_TASK_NAME, t -> t.dependsOn(sign));
		});

		project().afterEvaluate(p -> {
			for (final String outgoing : outgoingConfigurations) {
				p.getConfigurations().named(outgoing, conf -> {
					conf.getOutgoing().getArtifacts().removeIf(it -> Objects.equals(it.getClassifier(), "unsigned"));
				});
			}
		});
	}
}
