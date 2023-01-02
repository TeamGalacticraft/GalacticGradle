package net.galacticraft.curseforge.internal;

import org.gradle.api.Named;
import org.jetbrains.annotations.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.darkhax.curseforgegradle.api.metadata.Relation;
import net.galacticraft.curseforge.model.RelationType;

@AllArgsConstructor
public class NamedRelation implements Named
{
    private final String name;

    @Getter
    private final RelationType relationType;

    @NotNull
    @Override
    public String getName()
    {
        return this.name;
    }

    public Relation getRelation()
    {
        return new RelationImpl(this.name, this.relationType.toString());
    }

    static class RelationImpl extends Relation
    {

        protected RelationImpl(String slug, String type)
        {
            super(slug, type);
        }

    }
}
