package net.galacticraft.plugins.modrinth;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.plugins.AppliedPlugin;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.modrinth.minotaur.compat.FabricLoomCompatibility;
import com.modrinth.minotaur.dependencies.Dependency;
import com.modrinth.minotaur.dependencies.VersionDependency;
import com.modrinth.minotaur.request.VersionData;
import com.modrinth.minotaur.responses.ResponseError;
import com.modrinth.minotaur.responses.ResponseUpload;

import net.galacticraft.plugins.modrinth.model.dependency.DependencyContainer;

/**
 * Slightly modified version
 * @see 
 * 	<a href="https://github.com/modrinth/minotaur/blob/master/src/main/java/com/modrinth/minotaur/TaskModrinthUpload.java">TaskModrinthUpload</a>
 */
public class ModrinthUploadTask extends DefaultTask {

    private static final Gson GSON = new Gson();
    private static final Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
    private final ModrinthUploadExtension extension = getProject().getExtensions().getByType(ModrinthUploadExtension.class);

    @Nullable
    public ResponseUpload uploadInfo = null;

    @Nullable
    public ResponseError errorInfo = null;

    @TaskAction
    public void apply() {
        try {
            // Add version name if it's null
            if (extension.getVersionName().getOrNull() == null) {
                extension.getVersionName().set(extension.getVersionNumber().get());
            }

            // Attempt to automatically resolve the game version if one wasn't specified.
            if (extension.getGameVersions().get().isEmpty()) {
                this.detectGameVersionForge();
                this.detectGameVersionFabric();
            }

            if (extension.getGameVersions().get().isEmpty()) {
                throw new GradleException("Cannot upload to Modrinth: no game versions specified!");
            }
            if(extension.getLoaders().get().isEmpty()) {
                this.addLoaderForPlugin("net.minecraftforge.gradle", "forge");
                this.addLoaderForPlugin("fabric-loom", "fabric");
                this.addLoaderForPlugin("org.quiltmc.loom", "quilt");	
            }

            if (extension.getLoaders().get().isEmpty()) {
                throw new GradleException("Cannot upload to Modrinth: no loaders specified!");
            }

            List<File> filesToUpload = new ArrayList<>();

            final Object primaryFile = extension.getMainFile().get();
            final File file = resolveFile(this.getProject(), primaryFile);

            // Ensure the file actually exists before trying to upload it.
            if (file == null || !file.exists()) {
                this.getProject().getLogger().error("The upload file is missing or null. {}", primaryFile);
                throw new GradleException("The upload file is missing or null. " + primaryFile);
            }

            filesToUpload.add(file);

            try {
                this.upload(filesToUpload);
            } catch (final IOException e) {
                this.getProject().getLogger().error("Failed to upload the file!", e);
                throw new GradleException("Failed to upload the file!", e);
            }
        } catch (final Exception e) {
            this.getLogger().info("Failed to upload to Modrinth. Check logs for more info.");
            this.getLogger().error("Modrinth upload failed silently.", e);
        }
    }

    public void upload(List<File> files) throws IOException {
        final HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
        final HttpPost post = new HttpPost(this.getUploadEndpoint());

        post.addHeader("Authorization", extension.getToken().get());

        final MultipartEntityBuilder form = MultipartEntityBuilder.create();

        List<String> fileParts = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {
            fileParts.add(String.valueOf(i));
        }

        final VersionData data = new VersionData();
        data.setProjectId(extension.getProjectId().get());
        data.setVersionNumber(extension.getVersionNumber().get());
        data.setVersionTitle(extension.getVersionName().get());
        data.setChangelog(extension.getChangelog().get());
        data.setVersionType(extension.getVersionType().get().toLowerCase(Locale.ROOT));
        data.setGameVersions(extension.getGameVersions().get());
        data.setLoaders(extension.getLoaders().get());
        if(extension.getDependenciesContainer().getDependencies().size() > 0) {
            List<Dependency> depednencies = new ArrayList<>();
            for(DependencyContainer dep : extension.getDependenciesContainer().getDependencies()) {
            	depednencies.add(new VersionDependency(dep.getName(), dep.getType().get()));
            }
            data.setDependencies(depednencies);
        }
        data.setFileParts(fileParts);
        data.setPrimaryFile("0"); // The primary file will always be of the first index in the list

        if (extension.getDebug().get()) {
            this.getProject().getLogger().lifecycle("Full data to be sent for upload:");
            this.getProject().getLogger().lifecycle("{}", prettyGson.toJson(data));
            return;
        }

        form.addTextBody("data", GSON.toJson(data), ContentType.APPLICATION_JSON);

        for (int i = 0; i < files.size(); i++) {
            this.getProject().getLogger().debug("Uploading {} to {}.", files.get(i).getPath(), this.getUploadEndpoint());
            form.addBinaryBody(String.valueOf(i), files.get(i));
        }

        post.setEntity(form.build());

        try {
            final HttpResponse response = client.execute(post);
            final int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                this.uploadInfo = GSON.fromJson(EntityUtils.toString(response.getEntity()), ResponseUpload.class);
                this.getProject().getLogger().lifecycle("Successfully uploaded version {} to {} as version ID {}.", this.uploadInfo.getVersionNumber(), extension.getProjectId().get(), this.uploadInfo.getId());
            } else {
                this.errorInfo = GSON.fromJson(EntityUtils.toString(response.getEntity()), ResponseError.class);
                this.getProject().getLogger().error("Upload failed! Status: {} Error: {} Reason: {}", status, this.errorInfo.getError(), this.errorInfo.getDescription());
                throw new GradleException("Upload failed! Status: " + status + " Reason: " + this.errorInfo.getDescription());
            }
        } catch (final IOException e) {
            this.getProject().getLogger().error("Failure to upload files!", e);
            throw e;
        }
    }

    private String getUploadEndpoint() {
        String apiUrl = "https://api.modrinth.com/v2";
        return apiUrl + (apiUrl.endsWith("/") ? "" : "/") + "version";
    }

    @Nullable
    private static File resolveFile(Project project, Object in) {
        // If input or project is null we can't really do anything...
        if (in == null || project == null) {
            return null;
        }

        // If the file is a Java file handle no additional handling is needed.
        else if (in instanceof File) {
            return (File) in;
        }

        // Grabs the file from an archive task. Allows build scripts to do things like the jar task directly.
        else if (in instanceof AbstractArchiveTask) {
            return ((AbstractArchiveTask) in).getArchiveFile().get().getAsFile();
        }

        // Grabs the file from an archive task wrapped in a provider. Allows Kotlin DSL buildscripts to also specify
        // the jar task directly, rather than having to call #get() before running.
        else if (in instanceof TaskProvider<?>) {
            Object provided = ((TaskProvider<?>) in).get();

            // Check to see if the task provided is actually an AbstractArchiveTask.
            if (provided instanceof AbstractArchiveTask) {
                return ((AbstractArchiveTask) provided).getArchiveFile().get().getAsFile();
            }
        }

        // Fallback to Gradle's built-in file resolution mechanics.
        return project.file(in);
    }

    private void detectGameVersionForge() {
        Project project = this.getProject();
        ModrinthUploadExtension extension = project.getExtensions().getByType(ModrinthUploadExtension.class);

        try {
            final ExtraPropertiesExtension extraProps = project.getExtensions().getExtraProperties();
            if (extraProps.has("MC_VERSION")) {
                final String forgeGameVersion = extraProps.get("MC_VERSION").toString();

                if (forgeGameVersion != null && !forgeGameVersion.isEmpty()) {
                    project.getLogger().debug("Detected fallback game version {} from ForgeGradle.", forgeGameVersion);
                    if (extension.getGameVersions().get().isEmpty()) {
                        project.getLogger().debug("Adding game version {} because the game versions list is empty.", forgeGameVersion);
                        extension.getGameVersions().add(forgeGameVersion);
                    }
                }
            }
        } catch (final Exception e) {
            project.getLogger().debug("Failed to detect ForgeGradle game version.", e);
        }
    }

    private void detectGameVersionFabric() {
        Project project = this.getProject();
        ModrinthUploadExtension extension = this.getProject().getExtensions().getByType(ModrinthUploadExtension.class);

        if (project.getPluginManager().findPlugin("fabric-loom") != null ||
            project.getPluginManager().findPlugin("org.quiltmc.loom") != null) {
            try {
                String loomGameVersion = FabricLoomCompatibility.detectGameVersion(project);
                if (extension.getGameVersions().get().isEmpty()) {
                    project.getLogger().debug("Detected fallback game version {} from Loom.", loomGameVersion);
                    extension.getGameVersions().add(loomGameVersion);
                } else {
                    project.getLogger().debug("Detected fallback game version {} from Loom, but did not apply because game versions list is not empty.", loomGameVersion);
                }
            } catch (final Exception e) {
                project.getLogger().debug("Failed to detect Loom game version.", e);
            }
        } else {
            project.getLogger().debug("Loom is not present; no game versions were added.");
        }
    }

    private void addLoaderForPlugin(String pluginName, String loaderName) {
        try {
            final AppliedPlugin plugin = this.getProject().getPluginManager().findPlugin(pluginName);

            if (plugin != null) {
                extension.getLoaders().add(loaderName);
                this.getLogger().debug("Applying loader {} because plugin {} was found.", loaderName, pluginName);
            } else {
                this.getLogger().debug("Could not automatically apply loader {} because plugin {} has not been applied.", loaderName, pluginName);
            }
        } catch (final Exception e) {
            this.getLogger().debug("Failed to detect plugin {}.", pluginName, e);
        }
    }
}

