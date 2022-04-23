package net.galacticraft.plugins.curseforge.internal;

import com.google.common.base.Throwables;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import groovy.lang.Closure;
import net.galacticraft.plugins.curseforge.internal.json.GameVersion;
import net.galacticraft.plugins.curseforge.internal.json.VersionType;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class CurseVersions {
    /**
     * Load the valid game versions from CurseForge
     *
     * @param apiKey The api key to use to connect to CurseForge
     */
    public static void initialize(String apiKey) {

        gameVersions.clear();

        log.info("Initializing CurseForge versions...");

        try {
            final TIntSet validVersionTypes = new TIntHashSet();

            String versionTypesJson = Util.httpGet(apiKey, CurseGradlePlugin.getVERSION_TYPES_URL());
            //noinspection GroovyAssignabilityCheck
            VersionType[] types = Util.getGson().fromJson(versionTypesJson, VersionType[].class);
            
            
            DefaultGroovyMethods.each(types, new Closure<Boolean>(null, null) {
                public Boolean doCall(Object type) {
                    if (((VersionType) type).getSlug().startsWith("minecraft") || ((VersionType) type).getSlug().equals("java") || ((VersionType) type).getSlug().equals("modloader")) {
                        return validVersionTypes.add(((VersionType) type).getId());
                    }

                }

            });

            String gameVersionsJson = Util.httpGet(apiKey, CurseGradlePlugin.getVERSION_URL());
            //noinspection GroovyAssignabilityCheck
            GameVersion[] versions = Util.getGson().fromJson(gameVersionsJson, GameVersion[].class);
            DefaultGroovyMethods.each(versions, new Closure<Integer>(null, null) {
                public Integer doCall(Object version) {
                    if (((TIntHashSet) validVersionTypes).contains(((GameVersion) version).getGameVersionTypeID())) {
                        return gameVersions.put(((GameVersion) version).getName(), ((GameVersion) version).getId());
                    }

                }

            });

            log.info("CurseForge versions initialized");
        } catch (Throwable t) {
            throw Throwables.propagate(t);
        }

    }

    public static Integer[] resolveGameVersion(final Iterable<Object> objects) {
        final TIntSet set = new TIntHashSet();
        DefaultGroovyMethods.each(objects, new Closure<Boolean>(null, null) {
            public Boolean doCall(Object obj) {
                final String version = obj.toString();
                int id = gameVersions.get(version);
                if (id == 0) {
                    throw new IllegalArgumentException(version + " is not a valid game version. Valid versions are: " + String.valueOf(gameVersions.keySet()));
                }

                return set.add(id);
            }

        });

        return set.toArray();
    }

    private static final Logger log = Logging.getLogger(CurseVersions.class);
    private static final TObjectIntMap<String> gameVersions = new TObjectIntHashMap<String>();

    private static <Value extends Value> Value setFileID(CurseArtifact propOwner, Value fileID) {
        ((CurseArtifact) propOwner).setFileID(fileID);
        return fileID;
    }
}
