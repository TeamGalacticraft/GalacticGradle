package net.galacticraft.curseforge.model;

import java.util.EnumSet;

public enum RelationType
{
    EmbeddedLibrary,
    Incompatible,
    OptionalDependency,
    RequiredDependency,
    Tool;

    @Override
    public String toString()
    {
        final String s = super.toString();
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

    public static final EnumSet<RelationType> VALID_TYPES()
    {
        return EnumSet.allOf(RelationType.class);
    }
}
