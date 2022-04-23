package net.galacticraft.plugins.curseforge.internal.json;

public class VersionType {

    /**
     * The unique ID
     */
    private int id;
    /**
     * The user friendly name
     */
    private String name;
    /**
     * The unique slug
     */
    private String slug;
	
    @Override
    public String toString() {
        return "VersionType{" + "id=" + id + ", name='" + name + "\'" + ", slug='" + slug + "\'" + "}";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
