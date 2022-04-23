package net.galacticraft.plugins.curseforge.internal.json;

public class GameVersion {

    /**
     * The unique ID
     */
    private int id;
    /**
     * Game dependency ID
     */
    private int gameVersionTypeID;
    /**
     * A friendly name
     */
    private String name;
    /**
     * The unique slug
     */
    private String slug;
	
    @Override
    public String toString() {
        return "GameVersion{" + "id=" + id + ", gameVersionTypeID=" + gameVersionTypeID + ", name='" + name + "\'" + ", slug='" + slug + "\'" + "}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameVersionTypeID() {
        return gameVersionTypeID;
    }

    public void setGameVersionTypeID(int gameVersionTypeID) {
        this.gameVersionTypeID = gameVersionTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
