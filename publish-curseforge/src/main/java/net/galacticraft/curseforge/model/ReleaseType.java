package net.galacticraft.curseforge.model;

import java.util.EnumSet;

import org.jetbrains.annotations.NotNull;

public enum ReleaseType
{
    ALPHA,
    BETA,
    RELEASE;

    @NotNull
    @Override
    public String toString()
    {
        return super.name().toLowerCase();
    }

    public static final EnumSet<ReleaseType> VALID_TYPES()
    {
        return EnumSet.allOf(ReleaseType.class);
    }
    
    public static ReleaseType parse(final String val)
    {
        for(ReleaseType type : VALID_TYPES())
        {
            if(val.toLowerCase().contains(type.toString()))
            {
                return type;
            }
        }
        
        return ReleaseType.RELEASE;
    }
}
