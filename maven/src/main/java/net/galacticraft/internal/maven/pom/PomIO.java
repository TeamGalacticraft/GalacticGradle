package net.galacticraft.internal.maven.pom;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

import org.gradle.api.Project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import net.galacticraft.internal.maven.pom.PomJsonSpec.Role;

public class PomIO
{
	private final static String	POMRC_NAME	= ".pomrc";
	private GsonBuilder			gsonBuilder	= new GsonBuilder().setPrettyPrinting().setLenient();
	private final File			POMRC_FILE;
	private PomJsonSpec			pomJsonSpecObject;

	public PomIO(Project project)
	{
		this.POMRC_FILE = new File(project.getRootDir(), POMRC_NAME);
		if (!this.createRcFileIfNeeded())
		{
			this.readFromFile(POMRC_FILE.getAbsolutePath());
		}
	}

	private boolean createRcFileIfNeeded()
	{
		if (!POMRC_FILE.exists())
		{
			this.writeToFile(POMRC_FILE.getAbsolutePath());
			return true;
		}

		return false;
	}

	private Gson getGsonInstance()
	{
		Adapters.RoleListSerialization	serialize		= new Adapters.RoleListSerialization();
		Type							roleListType	= new TypeToken<List<Role>>()
														{
														}.getType();
		return gsonBuilder.registerTypeAdapter(roleListType, serialize).create();
	}

	public boolean jsonSpecHasArtifactId()
	{
		return !getJsonSpec().get().getArtifactId().isEmpty();
	}

	public boolean isJsonSpecEmpty()
	{
		return Integer.valueOf(PomJsonSpec.empty().hashCode()).equals(Integer.valueOf(getJsonSpec().get().hashCode()));
	}

	public Optional<String> getJsonSpecAsString()
	{
		return Optional.ofNullable(getGsonInstance().toJson(getJsonSpec()));
	}

	public Optional<PomJsonSpec> getJsonSpec()
	{
		return Optional.ofNullable(this.pomJsonSpecObject);
	}

	private void writeToFile(String file)
	{
		try (FileWriter writer = new FileWriter(file))
		{
			this.pomJsonSpecObject = PomJsonSpec.empty();
			getGsonInstance().toJson(PomJsonSpec.empty(), writer);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void readFromFile(String file)
	{
		try (Reader reader = new FileReader(file))
		{
			this.pomJsonSpecObject = getGsonInstance().fromJson(reader, PomJsonSpec.class);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
