package net.galacticraft.changelog.markdown.impl;

import net.steppschuh.markdowngenerator.MarkdownCascadable;
import net.steppschuh.markdowngenerator.MarkdownElement;
import net.steppschuh.markdowngenerator.MarkdownSerializationException;

public class CommitText extends MarkdownElement implements MarkdownCascadable
{

    protected String value;

    public CommitText(String value) {
        this.value = value;
    }

    @Override
    public String serialize() throws MarkdownSerializationException {
        if (value == null) {
            throw new MarkdownSerializationException("Value is null");
        }
        return getPredecessor() + value + getSuccessor();
    }

    @Override
    public String getPredecessor() {
        return "";
    }

    @Override
    public String getSuccessor() {
        return getPredecessor();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        invalidateSerialized();
    }
    
    @Override
    public String toString()
    {
        return getPredecessor() + value + getSuccessor();
    }
}