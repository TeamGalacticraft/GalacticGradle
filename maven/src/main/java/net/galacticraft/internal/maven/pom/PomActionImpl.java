package net.galacticraft.internal.maven.pom;

import java.util.List;
import java.util.stream.Collectors;

import org.gradle.api.Action;
import org.gradle.api.publish.maven.MavenPom;
import org.gradle.api.publish.maven.MavenPomCiManagement;
import org.gradle.api.publish.maven.MavenPomContributor;
import org.gradle.api.publish.maven.MavenPomContributorSpec;
import org.gradle.api.publish.maven.MavenPomDeveloper;
import org.gradle.api.publish.maven.MavenPomDeveloperSpec;
import org.gradle.api.publish.maven.MavenPomIssueManagement;
import org.gradle.api.publish.maven.MavenPomLicense;
import org.gradle.api.publish.maven.MavenPomLicenseSpec;
import org.gradle.api.publish.maven.MavenPomOrganization;
import org.gradle.api.publish.maven.MavenPomScm;

import net.galacticraft.internal.maven.pom.PomJsonSpec.CiManagement;
import net.galacticraft.internal.maven.pom.PomJsonSpec.Contributor;
import net.galacticraft.internal.maven.pom.PomJsonSpec.Developer;
import net.galacticraft.internal.maven.pom.PomJsonSpec.IssueManagement;
import net.galacticraft.internal.maven.pom.PomJsonSpec.License;
import net.galacticraft.internal.maven.pom.PomJsonSpec.Organization;
import net.galacticraft.internal.maven.pom.PomJsonSpec.Role;
import net.galacticraft.internal.maven.pom.PomJsonSpec.Scm;

public final class PomActionImpl
{

	public static Action<MavenPom> getPomAction(PomIO io)
	{
    	Integer j1 = Integer.valueOf(PomJsonSpec.empty().hashCode());
    	Integer j2 = Integer.valueOf(io.getJsonSpec().get().hashCode());
    	
    	if(!j1.equals(j2))
    	{
    		return spec -> {
    			
    			PomJsonSpec json = io.getJsonSpec().get();
    			
    			spec.getName().set(json.getName());
    			spec.getDescription().set(json.getDescription());
    			spec.getUrl().set(json.getUrl());
    			spec.getInceptionYear().set(json.getInceptionYear());
    			spec.organization(organization(json.getOrganization()));
    			spec.licenses(licenses(json.getLicenses()));
    			
    			spec.developers(developers(json.getDevelopers()));
    			spec.contributors(contributors(json.getContributors()));
    			
    			spec.scm(scm(json.getScm()));
    			spec.issueManagement(issueManagement(json.getIssueManagement()));
    			spec.ciManagement(ciManagement(json.getCiManagement()));
    		};
    	}
		
    	return spec -> {};
	}
	
	private static Action<MavenPomScm> scm(Scm scm)
	{
		return spec -> {
			spec.getConnection().set(scm.getConnection());
			spec.getDeveloperConnection().set(scm.getDeveloperConnection());
			spec.getTag().set(scm.getTag());
			spec.getUrl().set(scm.getUrl());
		};
	}
	
	private static Action<MavenPomCiManagement> ciManagement(CiManagement ci)
	{
		return spec -> {
			spec.getSystem().set(ci.getSystem());
			spec.getUrl().set(ci.getUrl());
		};
	}
	
	private static Action<MavenPomIssueManagement> issueManagement(IssueManagement issueManagement)
	{
		return spec -> {
			spec.getSystem().set(issueManagement.getSystem());
			spec.getUrl().set(issueManagement.getUrl());
		};
	}
	
	private static Action<MavenPomOrganization> organization(Organization organization)
	{
		return spec -> {
			spec.getName().set(organization.getName());
			spec.getUrl().set(organization.getUrl());
		};
	}
	
	private static Action<MavenPomLicense> license(License license)
	{
		return spec -> {
			spec.getName().set(license.getName());
			spec.getUrl().set(license.getUrl());
			spec.getDistribution().set(license.getDistribution());
			spec.getComments().set(license.getComments());
		};
	}
	
	private static Action<MavenPomDeveloper> developer(Developer developer)
	{
		return spec -> {	
			spec.getId().set(developer.getId());
			spec.getName().set(developer.getName());
			spec.getEmail().set(developer.getEmail());
			spec.getUrl().set(developer.getUrl());
			spec.getOrganization().set(developer.getOrganization());
			spec.getOrganizationUrl().set(developer.getOrganizationurl());
			spec.getRoles().addAll(developer.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
			spec.getTimezone().set(developer.getTimezone());
		};
	}
	
	private static Action<MavenPomContributor> contributor(Contributor contributor)
	{
		return spec -> {	
			spec.getName().set(contributor.getName());
			spec.getEmail().set(contributor.getEmail());
			spec.getUrl().set(contributor.getUrl());
			spec.getOrganization().set(contributor.getOrganization());
			spec.getOrganizationUrl().set(contributor.getOrganizationurl());
			spec.getRoles().addAll(contributor.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
			spec.getTimezone().set(contributor.getTimezone());
		};
	}
	
	private static Action<MavenPomDeveloperSpec> developers(List<Developer> developers)
	{
		return spec -> {
			for(Developer dev : developers)
			{
				spec.developer(developer(dev));
			}
		};
	}
	
	private static Action<MavenPomContributorSpec> contributors(List<Contributor> contributors)
	{
		return spec -> {
			for(Contributor contributor : contributors)
			{
				spec.contributor(contributor(contributor));
			}
		};
	}
	
	private static Action<MavenPomLicenseSpec> licenses(List<License> licenses)
	{
		return spec -> {
			for(License license : licenses)
			{
				spec.license(license(license));
			}
		};
	}
}
