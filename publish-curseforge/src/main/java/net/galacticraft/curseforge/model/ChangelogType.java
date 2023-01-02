package net.galacticraft.curseforge.model;

import java.util.EnumSet;
import java.util.List;

import com.google.common.collect.Lists;

public enum ChangelogType
{
    HTML,
    MARKDOWN,
    TEXT;

    @Override
    public String toString()
    {
        return super.toString().toLowerCase();
    }

    public static final EnumSet<ChangelogType> VALID_TYPES()
    {
        return EnumSet.allOf(ChangelogType.class);
    }

    public static ChangelogType fromValue(String value)
    {
        if (value == null) {
            return null;
        }
        List<String> markdownExtensions = Lists.newArrayList("md", "markdown", "mdown", "mkdn", "mkd", "mdwn");
        List<String> htmlExtensions = Lists.newArrayList("html", "htm");

        final String lwr = value.toLowerCase();
        if (markdownExtensions.contains(lwr)) {
            return MARKDOWN;
        } else if (htmlExtensions.contains(lwr)) {
            return HTML;
        }

        return TEXT;
    }
}
