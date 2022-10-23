
package net.galacticraft.gradle.core.xml.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@SuppressWarnings("all")
public class Versioning implements Cloneable
{

	private String latest;

	private String release;

	private List<String> versions;

	private String lastUpdated;

	private Snapshot snapshot;

	private List<SnapshotVersion> snapshotVersions;

	public void addSnapshotVersion(SnapshotVersion snapshotVersion)
	{
		getSnapshotVersions().add(snapshotVersion);
	}

	public void addVersion(String string)
	{
		getVersions().add(string);
	}

	public Versioning clone()
	{
		try
		{
			Versioning copy = (Versioning) super.clone();

			if (this.versions != null)
			{
				copy.versions = new ArrayList<String>();
				copy.versions.addAll(this.versions);
			}

			if (this.snapshot != null)
			{
				copy.snapshot = (Snapshot) this.snapshot.clone();
			}

			if (this.snapshotVersions != null)
			{
				copy.snapshotVersions = new ArrayList<SnapshotVersion>();
				for (SnapshotVersion item : this.snapshotVersions)
				{
					copy.snapshotVersions.add(((SnapshotVersion) item).clone());
				}
			}

			return copy;
		} catch (java.lang.Exception ex)
		{
			throw (java.lang.RuntimeException) new java.lang.UnsupportedOperationException(getClass().getName() + " does not support clone()").initCause(ex);
		}
	}

	public String getLastUpdated()
	{
		return this.lastUpdated;
	}

	public String getLatest()
	{
		return this.latest;
	}

	public String getRelease()
	{
		return this.release;
	}

	public Snapshot getSnapshot()
	{
		return this.snapshot;
	}

	public List<SnapshotVersion> getSnapshotVersions()
	{
		if (this.snapshotVersions == null)
		{
			this.snapshotVersions = new ArrayList<SnapshotVersion>();
		}

		return this.snapshotVersions;
	}

	public List<String> getVersions()
	{
		if (this.versions == null)
		{
			this.versions = new ArrayList<String>();
		}

		return this.versions;
	}

	public void removeSnapshotVersion(SnapshotVersion snapshotVersion)
	{
		getSnapshotVersions().remove(snapshotVersion);
	}

	public void removeVersion(String string)
	{
		getVersions().remove(string);
	}

	public void setLastUpdated(String lastUpdated)
	{
		this.lastUpdated = lastUpdated;
	}

	public void setLatest(String latest)
	{
		this.latest = latest;
	}

	public void setRelease(String release)
	{
		this.release = release;
	}

	public void setSnapshot(Snapshot snapshot)
	{
		this.snapshot = snapshot;
	}

	public void setSnapshotVersions(List<SnapshotVersion> snapshotVersions)
	{
		this.snapshotVersions = snapshotVersions;
	}

	public void setVersions(List<String> versions)
	{
		this.versions = versions;
	}

	public void updateTimestamp()
	{
		setLastUpdatedTimestamp(new Date());
	}

	public void setLastUpdatedTimestamp(Date date)
	{
		TimeZone				timezone	= TimeZone.getTimeZone("UTC");
		java.text.DateFormat	fmt			= new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		fmt.setTimeZone(timezone);
		setLastUpdated(fmt.format(date));
	}

}
