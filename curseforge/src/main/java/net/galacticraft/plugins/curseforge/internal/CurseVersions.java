package net.galacticraft.plugins.curseforge.internal;

import java.util.Arrays;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import lombok.Getter;

public class CurseVersions {
	private static final TObjectIntMap<String> gameVersions = new TObjectIntHashMap<String>();

	public static void init() {
		gameVersions.clear();

		final TIntSet validVersionTypes = new TIntHashSet();

		String versionTypesReponse = Util.fromResourceAsString("VersionTypes.json");
		VersionType[] types = Util.getGson().fromJson(versionTypesReponse, VersionType[].class);
		for (VersionType type : types) {
			String slug = type.getSlug();
			if (slug.startsWith("minecraft") || slug.equals("java") || slug.equals("modloader")) {
				validVersionTypes.add(type.getId());
			}
		}

		String GameVersionsResponse = Util.fromResourceAsString("GameVersions.json");
		GameVersion[] versions = Util.getGson().fromJson(GameVersionsResponse, GameVersion[].class);
		for (GameVersion version : versions) {
			if (validVersionTypes.contains(version.getGameVersionTypeID())) {
				gameVersions.put(version.getName(), version.getId());
			}
		}
	}

	public static Integer[] resolveGameVersion(final Iterable<Object> objects) {
		final TIntSet set = new TIntHashSet();
		for (Object obj : objects) {
			final String version = obj.toString();
			int gameVersion = gameVersions.get(version);
			if (gameVersion == 0) {
				throw new IllegalArgumentException(version + " is not a valid game version. Valid versions are: "
						+ String.valueOf(gameVersions.keySet()));
			}

			set.add(gameVersion);
		}
		return Arrays.stream(set.toArray()).boxed().toArray(Integer[]::new);
	}
	
	@Getter
	private static class GameVersion {

		@SerializedName("id")
		@Expose
	    private int id;
		
		@SerializedName("gameVersionTypeID")
		@Expose
	    private int gameVersionTypeID;
		
		@SerializedName("name")
		@Expose
	    private String name;
		
		@SerializedName("slug")
		@Expose
	    private String slug;
	}
	
	@Getter
	private static class VersionType {

		@SerializedName("id")
		@Expose
		private int id;
		
		@SerializedName("name")
		@Expose
		private String name;
		
		@SerializedName("slug")
		@Expose
		private String slug;

	}
}