package net.galacticraft.internal.maven.pom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.JsonAdapter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PomJsonSpec
{
	private String	artifactId;
	private String	name;
	private String	url;
	private String	description;
	private String	inceptionYear;

	private Organization		organization;
	private List<License>		licenses;
	private IssueManagement		issueManagement;
	private Scm					scm;
	private CiManagement		ciManagement;
	private List<Developer>		developers;
	private List<Contributor>	contributors;

	@Data
	@Builder
	public static class License
	{
		private String	name;
		private String	url;
		private String	distribution;
		private String	comments;

		public static License empty()
		{
			return License.builder().name("").url("").distribution("").comments("").build();
		}
	}

	@Data
	@Builder
	public static class Organization
	{
		private String	name;
		private String	url;

		public static Organization empty()
		{
			return Organization.builder().name("").url("").build();
		}
	}

	@Data
	@Builder
	public static class IssueManagement
	{
		private String	system;
		private String	url;

		public static IssueManagement empty()
		{
			return IssueManagement.builder().system("").url("").build();
		}
	}

	@Data
	@Builder
	public static class Scm
	{
		private String	connection;
		private String	developerConnection;
		private String	tag;
		private String	url;

		public static Scm empty()
		{
			return Scm.builder().connection("").developerConnection("").tag("").url("").build();
		}
	}

	@Data
	@Builder
	public static class CiManagement
	{
		private String	system;
		private String	url;

		public static CiManagement empty()
		{
			return CiManagement.builder().system("").url("").build();
		}
	}

	@Data
	@Builder
	public static class Role
	{
		String name;
	}

	@Data
	@Builder
	public static class Developer
	{
		private String		id;
		private String		name;
		private String		email;
		private String		url;
		private String		organization;
		private String		organizationurl;
		@JsonAdapter(Adapters.RoleListSerialization.class)
		private List<Role>	roles;
		private String		timezone;

		public static Developer empty()
		{
			return Developer.builder().id("").name("").email("").url("").organization("").organizationurl("").roles(new ArrayList<>(Arrays.asList(new Role("")))).timezone("")
				.build();
		}
	}

	@Data
	@Builder
	public static class Contributor
	{
		private String		name;
		private String		email;
		private String		url;
		private String		organization;
		private String		organizationurl;
		@JsonAdapter(Adapters.RoleListSerialization.class)
		private List<Role>	roles;
		private String		timezone;

		public static Contributor empty()
		{
			return Contributor.builder().name("").email("").url("").organization("").organizationurl("").roles(new ArrayList<>(Arrays.asList(new Role("")))).timezone("")
				.build();
		}
	}

	private static List<License> emptyLicenseList()
	{
		return new ArrayList<>(Arrays.asList(License.empty()));
	}

	private static List<Contributor> emptyContributorList()
	{
		return new ArrayList<>(Arrays.asList(Contributor.empty()));
	}

	private static List<Developer> emptyDeveloperList()
	{
		return new ArrayList<>(Arrays.asList(Developer.empty()));
	}

	public static PomJsonSpec empty()
	{
		return PomJsonSpec.builder().artifactId("").name("").url("").description("").inceptionYear("").organization(Organization.empty()).licenses(emptyLicenseList())
			.issueManagement(IssueManagement.empty()).scm(Scm.empty()).ciManagement(CiManagement.empty()).developers(emptyDeveloperList()).contributors(emptyContributorList())
			.build();
	}
}
