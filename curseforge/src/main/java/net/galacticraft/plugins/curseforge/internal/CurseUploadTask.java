package net.galacticraft.plugins.curseforge.internal;

import com.google.common.base.Strings;
import groovy.lang.Closure;
import net.galacticraft.plugins.curseforge.internal.jsonresponse.CurseError;
import net.galacticraft.plugins.curseforge.internal.jsonresponse.UploadResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.DefaultTask;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

public class CurseUploadTask extends DefaultTask {
    @TaskAction
    public Collection<CurseArtifact> run() {

        Util.check(!Strings.isNullOrEmpty(apiKey), "CurseForge Project " + getProjectId() + " does not have an apiKey configured");

        mainArtifact.resolve(getProject());

        CurseVersions.initialize(apiKey);
        mainArtifact.setGameVersions(CurseVersions.resolveGameVersion(mainArtifact.getGameVersionStrings()));

        final String json = Util.getGson().toJson(mainArtifact);
        final int mainID = uploadFile(json, (File) mainArtifact.getArtifact());
        mainArtifact.setFileID(mainID);

        return DefaultGroovyMethods.each(additionalArtifacts, new Closure<Integer>(this, this) {
            public Integer doCall(Object artifact) {
                ((CurseArtifact) artifact).resolve(getProject());
                ((CurseArtifact) artifact).setParentFileID(mainID);
                final String childJson = Util.getGson().toJson(artifact);
                return setFileID(artifact, uploadFile(childJson, (File) ((CurseArtifact) artifact).getArtifact()));
            }

        });
    }

    public int uploadFile(String json, File file) throws IOException, URISyntaxException {

        int fileID;
        final String uploadUrl = String.format(CurseGradlePlugin.getUPLOAD_URL(), projectId);
        log.info("Uploading file: {} to url: {} with json: {}", file, uploadUrl, json);

        HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();

        HttpPost post = new HttpPost(new URI(uploadUrl));

        post.addHeader("X-Api-Token", apiKey);
        post.setEntity(MultipartEntityBuilder.create().addTextBody("metadata", json, ContentType.APPLICATION_JSON).addBinaryBody("file", file).build());

        if (getProject().getExtensions().getByType(CurseExtension.class).getCurseGradleOptions().getDebug()) {
            getLogger().lifecycle("DEBUG: File: " + String.valueOf(file) + "  Json: " + json);
            return 0;
        }


        HttpResponse response = client.execute(post);

        if (response.getStatusLine().getStatusCode() == 200) {
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
            UploadResponse curseResponse = Util.getGson().fromJson(reader, UploadResponse.class);
            reader.close();
            fileID = curseResponse.getId();
        } else {
            if (response.getFirstHeader("content-type").getValue().contains("json")) {
                InputStreamReader reader = new InputStreamReader(response.getEntity().getContent());
                final CurseError error = Util.getGson().fromJson(reader, CurseError.class);
                reader.close();
                throw new RuntimeException("[CurseForge " + getProjectId() + "] Error Code " + String.valueOf(error.getErrorCode()) + ": " + error.getErrorMessage());
            } else {
                throw new RuntimeException("[CurseForge " + getProjectId() + "] HTTP Error Code " + String.valueOf(response.getStatusLine().getStatusCode()) + ": " + response.getStatusLine().getReasonPhrase());
            }

        }


        log.lifecycle("Uploaded {} to CurseForge Project: {}, with ID: {}", file, projectId, fileID);
        return fileID;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public CurseArtifact getMainArtifact() {
        return mainArtifact;
    }

    public void setMainArtifact(CurseArtifact mainArtifact) {
        this.mainArtifact = mainArtifact;
    }

    public Collection<CurseArtifact> getAdditionalArtifacts() {
        return additionalArtifacts;
    }

    public void setAdditionalArtifacts(Collection<CurseArtifact> additionalArtifacts) {
        this.additionalArtifacts = additionalArtifacts;
    }

    private static final Logger log = Logging.getLogger(CurseUploadTask.class);
    @Input
    private String apiKey;
    @Input
    private String projectId;
    @Input
    private CurseArtifact mainArtifact;
    @Input
    private Collection<CurseArtifact> additionalArtifacts;

    private static <Value extends Integer> Value setFileID(CurseArtifact propOwner, Value fileID) {
        propOwner.setFileID(fileID);
        return fileID;
    }
}
