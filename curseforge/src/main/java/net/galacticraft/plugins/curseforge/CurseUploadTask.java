package net.galacticraft.plugins.curseforge;

import java.io.File;

import javax.annotation.Nullable;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.plugins.AppliedPlugin;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;

import net.galacticraft.plugins.curseforge.curse.RelationContainer;
import net.galacticraft.plugins.curseforge.http.CFConstants;
import net.galacticraft.plugins.curseforge.http.OkHttpUtil;
import net.galacticraft.plugins.curseforge.internal.CurseVersions;
import net.galacticraft.plugins.curseforge.internal.Util;
import net.galacticraft.plugins.curseforge.json.CurseError;
import net.galacticraft.plugins.curseforge.json.CurseProject;
import net.galacticraft.plugins.curseforge.json.CurseProject.Type;
import net.galacticraft.plugins.curseforge.json.CurseReponse;
import net.galacticraft.plugins.curseforge.json.Metadata;
import net.galacticraft.plugins.curseforge.json.Metadata.ChangelogType;
import net.galacticraft.plugins.curseforge.json.Metadata.ReleaseType;
import net.galacticraft.plugins.curseforge.json.ReturnReponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CurseUploadTask extends DefaultTask {

    private final CurseUploadExtension extension = getProject().getExtensions().getByType(CurseUploadExtension.class);

    @Nullable
    public CurseError errorInfo = null;
	
    @TaskAction
    public void apply() {
    	Util.check(extension.getApiKey().getOrNull() != null, "CurseForge Project does not have an apiKey configured");
    	
        this.addLoaderForPlugin("net.minecraftforge.gradle", "Forge");
        this.addLoaderForPlugin("net.minecraftforge.gradle.forge", "Forge");
        this.addLoaderForPlugin("fabric-loom", "Fabric");
    	
    	Integer[] gameVersions = CurseVersions.resolveGameVersion(extension.getGameVersions().get());

    	Metadata metadata = new Metadata()
    			.changelog(this.extension.getChangelog().get())
    			.changelogType(ChangelogType.fromValue(extension.getChangelogType().get()))
    			.releaseType(ReleaseType.fromValue(extension.getReleaseType().get()))
    			.gameVersions(gameVersions);
    	
    	for(RelationContainer relation : extension.getRelations()) {
    		CurseProject project = new CurseProject().slug(relation.getName()).type(Type.fromValue(relation.getType().get()));
    		metadata.getRelations().getProjects().add(project);
    	}
    	
    	final File file = resolveFile(this.getProject(), extension.getUploadFile().get());
    	this.upload(metadata, file);
    }
    
    public void upload(Metadata metadata, File uploadFile) {
    	String uploadUrl = String.format(CFConstants.UPLOAD_URL, extension.getProjectId().get());
    	
    	MediaType stream = MediaType.parse("application/octet-stream");
    	MediaType json = MediaType.parse("application/json");
    	
    	RequestBody fileBody = RequestBody.create(stream, uploadFile);
    	RequestBody dataBody = RequestBody.create(json, Util.getGson().toJson(metadata));

    	MultipartBody.Builder builder = new MultipartBody.Builder()
    			.addFormDataPart("metadata", null, dataBody)
    			.addFormDataPart("file", uploadFile.getName(), fileBody)
    			.setType(MultipartBody.FORM);
    	
    	ReturnReponse response = OkHttpUtil.instance.post(builder, uploadUrl);
    	
    	
    	
    	if(response instanceof CurseReponse) {
    		CurseReponse sucessfull = (CurseReponse) response;
    		this.getProject().getLogger().lifecycle("[CurseForge] File upload sucessfull");
    		this.getProject().getLogger().lifecycle("[CurseForge] File ID: {}", sucessfull.getId());
    	} 
    	if(response instanceof CurseError) {
    		CurseError error = (CurseError) response;
    		this.getProject().getLogger().lifecycle("[CurseForge] Upload Failed");
    		this.getProject().getLogger().lifecycle("[CurseForge] code: {}", error.code());
    		this.getProject().getLogger().lifecycle("[CurseForge] message: {}", error.message());
    	}
    }

    @Nullable
    private static File resolveFile(Project project, Object in) {
        if (in == null || project == null) {
            return null;
        }

        else if (in instanceof File) {
            return (File) in;
        }

        else if (in instanceof AbstractArchiveTask) {
            return ((AbstractArchiveTask) in).getArchiveFile().get().getAsFile();
        }

        else if (in instanceof TaskProvider<?>) {
            Object provided = ((TaskProvider<?>) in).get();

            if (provided instanceof AbstractArchiveTask) {
                return ((AbstractArchiveTask) provided).getArchiveFile().get().getAsFile();
            }
        }

        return project.file(in);
    }
    
    private void addLoaderForPlugin(String pluginName, String loaderName) {
        try {
            final AppliedPlugin plugin = this.getProject().getPluginManager().findPlugin(pluginName);

            if (plugin != null) {
                extension.getGameVersions().add(loaderName);
                this.getLogger().lifecycle("[CurseForge] Applying gameVersion {} because plugin {} was found.", loaderName, pluginName);
            } else {
                this.getLogger().debug("[CurseForge] Could not automatically apply gameVersion {} because plugin {} has not been applied.", loaderName, pluginName);
            }
        } catch (final Exception e) {
            this.getLogger().error("[CurseForge] Failed to detect plugin {}.", pluginName, e);
        }
    }
}
