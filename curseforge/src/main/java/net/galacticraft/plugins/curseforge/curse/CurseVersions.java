package net.galacticraft.plugins.curseforge.curse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import lombok.Getter;
import net.galacticraft.plugins.curseforge.util.Util;

public class CurseVersions {
	private static final TObjectIntMap<String> gameVersions = new TObjectIntHashMap<String>();
	private static final Map<Integer, String> gameVersionsReversed = new HashMap<>();

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
				gameVersionsReversed.put(version.getId(), version.getName());
			}
		}
	}
	
	public static String[] checkVersionsToAdd(String[] strings) {
		Set<String> versions = new HashSet<>();
		for (String string : strings) {
			if(gameVersions.keySet().contains(string)) {
				versions.add(string);
			}
		}
		return Arrays.stream(versions.toArray()).toArray(String[]::new);
	}
	
	public static String[] resolvedIdsToStrings(final Iterable<Integer> ids) {
		Set<String> versions = new HashSet<>();
		for (Integer id : ids) {
			String name = gameVersionsReversed.get(id);
			versions.add(name);
		}
		return Arrays.stream(versions.toArray()).toArray(String[]::new);
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