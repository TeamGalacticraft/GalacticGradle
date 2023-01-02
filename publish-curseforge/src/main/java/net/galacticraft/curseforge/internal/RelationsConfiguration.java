package net.galacticraft.curseforge.internal;

import javax.inject.Inject;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;

import net.galacticraft.curseforge.model.RelationType;

public class RelationsConfiguration
{
    private ListProperty<NamedRelation> relations;
    
    @Inject
    public RelationsConfiguration(final ObjectFactory factory)
    {
        this.relations = factory.listProperty(NamedRelation.class).empty();
    }
    
    public ListProperty<NamedRelation> getList()
    {
        return this.relations;
    }
    
    public void embedded(final String... slugs)
    {
        for(String i : slugs)
            this.relations.add(new NamedRelation(i, RelationType.EmbeddedLibrary));
    }
    
    public void incompatible(final String... slugs)
    {
        for(String i : slugs)
            this.relations.add(new NamedRelation(i, RelationType.Incompatible));
    }
    
    public void optional(final String... slugs)
    {
        for(String i : slugs)
            this.relations.add(new NamedRelation(i, RelationType.OptionalDependency));
    }
    
    public void required(final String... slugs)
    {
        for(String i : slugs)
            this.relations.add(new NamedRelation(i, RelationType.RequiredDependency));
    }
    
    public void tool(final String... slugs)
    {
        for(String i : slugs)
            this.relations.add(new NamedRelation(i, RelationType.Tool));
    }
}
