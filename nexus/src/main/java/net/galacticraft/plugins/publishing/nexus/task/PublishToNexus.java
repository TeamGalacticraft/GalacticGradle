package net.galacticraft.plugins.publishing.nexus.task;

import java.util.List;
import org.gradle.api.logging.Logger;
import org.gradle.api.provider.Property;
import org.gradle.api.publish.maven.MavenPomIssueManagement;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.internal.publication.MavenPomDistributionManagementInternal;
import org.gradle.api.publish.maven.internal.publication.MavenPomInternal;
import org.gradle.api.publish.maven.internal.publication.MavenPublicationInternal;
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository;
import org.gradle.api.tasks.TaskAction;
import net.galacticraft.common.plugins.property.BooleanProperty;

public abstract class PublishToNexus extends PublishToMavenRepository {
    
    Logger log = this.getLogger();
    
    public Property<Boolean> isDebug;

    @TaskAction
    @Override
    public void publish() {
        BooleanProperty debug = new BooleanProperty(isDebug);
        if(debug.isTrue()) {
                log("Nexus Publish Task is set to Debug");
                log("  ");
                log("Artifacts To Upload");
                log("-------------------");
                MavenPublicationInternal publication = getPublicationInternal();

                publication.getArtifacts().forEach(artifact -> {
                    log(" -> {}", artifact.getFile().getName());
                    log("   - Extension: {}", artifact.getExtension());
                    log("   - Classifer: {}", (artifact.getClassifier() != null) ? artifact.getClassifier() : "none");
                    log("  ");
                });
                
                this.logMavenPom(publication.getPom());
        } else {
            super.publish();
        }
    }
    
    private void logMavenPom(MavenPomInternal pom) {
        log("[POM]");
        log("-----");
        
        log(" - Name", pom.getName());
        log(" - Description", pom.getDescription());
        log(" - InceptionYear", pom.getInceptionYear());
        log(" - Url", pom.getUrl());
        log(" - Packaging", pom.getPackaging());
        
        List<MavenPomLicense> license = pom.getLicenses();
        log("   ");
        if(!license.isEmpty()) {
            log(" - Licenses");
            license.forEach(lic -> {
                log("     Name", lic.getName());
                log("     Url", lic.getUrl());
                log("     Distribution", lic.getDistribution());
                log("     Comments", lic.getComments());
                if(license.size() > 1) {
                    log(" ------ ");
                }
            });
        }

        MavenPomDistributionManagementInternal dist = pom.getDistributionManagement();
        if(dist != null) {
            log("  ");
            log(" - DistributionManagement");
            log("     Url", dist.getDownloadUrl());
        }
        
        MavenPomIssueManagement issue = pom.getIssueManagement();
        if(issue != null) {
            log("  ");
            log(" - IssueManagement");
            log("     System", issue.getSystem());
            log("     Url", issue.getUrl());
        }
        
        log("  ");
        if(!pom.getDevelopers().isEmpty()) {
            log(" - Developers");
            pom.getDevelopers().forEach(dev -> {
                log("     Name", dev.getName());
                log("     Id", dev.getId());
                log("     Email", dev.getEmail());
                log("     Roles", dev.getRoles());
                log("     Timezone", dev.getTimezone());
            });
        }
    }
    
    private void log(String m, Object... args) {
        this.log.lifecycle(m, args);
    }
    
    private void log(String m, String args) {
        if(args != null) {
            this.log.lifecycle(m + ": ", args);
        }
    }
    
    private void log(String m) {
        this.log.lifecycle(m);
    }

    private void log(String name, Property<?> m) {
        if(m.isPresent()) {
            this.log.lifecycle("{}: {}", name,  String.valueOf(m.get()));
        }
    }
}
