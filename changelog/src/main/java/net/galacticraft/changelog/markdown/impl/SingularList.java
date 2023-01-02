package net.galacticraft.changelog.markdown.impl;

import net.steppschuh.markdowngenerator.MarkdownSerializationException;
import net.steppschuh.markdowngenerator.list.UnorderedList;

public class SingularList extends UnorderedList<String>
{
    public SingularList(String singleValue)
    {
        super();
        this.items.add(singleValue);
    }
    
    public String trySerialize()
    {
        try {
            return this.serialize();
        } catch (MarkdownSerializationException e) {
            return null;
        }
    }
}