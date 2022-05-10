package net.galacticraft.plugins.tinyforge.versions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YarnVersion {
    private static final Pattern VERSION_PATTERN = Pattern.compile("^(?<mcVersion>[a-zA-Z\\d\\.-]*)\\+build\\.(?<yarnBuild>\\d+)$");

    private final String minecraftVersion;
    private final String yarnBuild;

    public YarnVersion(String minecraftVersion, String yarnBuild) {
        this.minecraftVersion = minecraftVersion;
        this.yarnBuild = yarnBuild;
    }

    public static YarnVersion of(String version) {
        Matcher matcher = VERSION_PATTERN.matcher(version);

        if(matcher.find()) {
            return new YarnVersion(matcher.group("mcVersion"), matcher.group("yarnBuild"));
        } else {
            throw new IllegalArgumentException("version is not a valid Yarn version.");
        }
    }

    public String getMinecraftVersion() {
        return this.minecraftVersion;
    }

    public String getYarnBuild() {
        return this.yarnBuild;
    }

    public String getVersion() {
        return this.getMinecraftVersion() + "+build." + this.getYarnBuild();
    }

    public String getDependencyNotation() {
        return "net.fabricmc:yarn:" + this.getVersion() + ":v2";
    }

    public String getIntermediaryDependencyNotation() {
        return "net.fabricmc:intermediary:" + this.getMinecraftVersion() + ":v2";
    }
}
