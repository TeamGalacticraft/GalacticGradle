package dev.galacticraft.gradle;

import static dev.galacticraft.gradle.internal.Constants.LATEST_VERSION_PATH;
import static dev.galacticraft.gradle.internal.Constants.NEW_GROUP;
import static dev.galacticraft.gradle.internal.Constants.NEW_ID;
import static dev.galacticraft.gradle.internal.Constants.OLD_GROUP;
import static dev.galacticraft.gradle.internal.Constants.OLD_ID;
import static dev.galacticraft.gradle.internal.Constants.OLD_LATEST_VERSION_PATH;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;

import dev.galacticraft.gradle.internal.ArtifactData;
import dev.galacticraft.gradle.internal.Constants;
import dev.galacticraft.gradle.internal.Version;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.gradle.userdev.DependencyManagementExtension;

public class DevBasePlugin implements Plugin<Project> {

	@Getter
	private AddonExtension addonExtension;
	private MavenArtifactRepository repo;
	private Project project;
	@Getter
	@Setter
	private String gcDependency;
	
	@Override
	public void apply(Project project) {
		this.project = project;

		addonExtension = project.getExtensions().create("galacticraft", AddonExtension.class);
		project.afterEvaluate(configure -> applyGalacticraftDependencyRepository());
		
		Version gradleVersion = new Version(project.getGradle().getGradleVersion());
		project.afterEvaluate(set -> getGalacticraftVersion());
		
		if (gradleVersion.isLowerThanOrEqualTo("4.10.3")) {
			project.afterEvaluate(set -> applyFG2_3Dependency());
		} else if (gradleVersion.isGreaterThan("4.10.3")) {
			project.afterEvaluate(set -> applyFG5Dependency());
		}
	}
	
	private void getGalacticraftVersion() {
		ArtifactData artifactData;
		if (addonExtension.getVersion().equals("latest")) {
			artifactData = determineArtifactData();
		} else {
			Version version;
			if (addonExtension.isSnapshots()) {
				version = new Version(addonExtension.getVersion().split("-")[0]);
			} else {
				version = new Version(addonExtension.getVersion());
			}
			artifactData = determineArtifactData(version);
		}
		project.getLogger().lifecycle(artifactData.toString());
		setGcDependency(artifactData.artifactString());
	}
	
	private void applyFG2_3Dependency() {
		project.getDependencies().add("compile", getGcDependency() + ":deobf");
	}
	
	private void applyFG5Dependency() {
		Dependency dep = project.getExtensions().getByType(DependencyManagementExtension.class)
				.deobf(project.getDependencies().create(getGcDependency()));
		project.getDependencies().add("implementation", dep);
	}

	private void applyGalacticraftDependencyRepository() {
		if (addonExtension.isSnapshots()) {
			repo = project.getRepositories().maven(r -> {
				r.setName("galacticraft");
				r.setUrl(Constants.SNAPSHOTS_URL);
				r.metadataSources(m -> {
					m.gradleMetadata();
					m.mavenPom();
					m.artifact();
				});
			});
		} else {
			repo = project.getRepositories().maven(r -> {
				r.setName("galacticraft");
				r.setUrl(Constants.RELEASES_URL);
				r.metadataSources(m -> {
					m.gradleMetadata();
					m.mavenPom();
					m.artifact();
				});
			});
		}
	}

	public ArtifactData determineArtifactData(Version version) {
		if (version.isEqualTo("latest")) {
			return determineArtifactData();
		} else {
			if (version.isLowerThanOrEqualTo("4.0.2.284")) {
				return ArtifactData.builder().group(OLD_GROUP).id(OLD_ID).version(version.getValue()).build();
			} else if (version.isGreaterThan("4.0.2.284")) {
				return ArtifactData.builder().group(NEW_GROUP).id(NEW_ID).version(version.getValue()).build();
			} else {
				return ArtifactData.NULL;
			}
		}
	}

	public ArtifactData determineArtifactData() {
		String latest = getLatestString(url(LATEST_VERSION_PATH));
		if (latest.length() > 0) {
			return ArtifactData.builder().group(NEW_GROUP).id(NEW_ID).version(latest).build();
		}
		String next = getLatestString(url(OLD_LATEST_VERSION_PATH));
		if (next.length() > 0) {
			return ArtifactData.builder().group(OLD_GROUP).id(OLD_ID).version(next).build();
		}
		return ArtifactData.NULL;
	}

	private URL url(String path) {
		try {
			URL url = new URL(repo.getUrl().toString() + path);
			return url;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private String getLatestString(URL url) {
		String version = "";
		try {
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
			httpcon.addRequestProperty("User-Agent", "Chrome");
			String value;
			BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
			while ((value = br.readLine()) != null) {
				version = value;
			}
			br.close();
		} catch (IOException e) {
		}
		return version;
	}
}
