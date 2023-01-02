package net.galacticraft.changelog.markdown.impl;

public class CommitListItem extends CommitText
{
    public CommitListItem(String value) {
        super(value);
    }

    @Override
    public String getPredecessor() {
        return "- ";
    }

    @Override
    public String getSuccessor() {
        return "";
    }
}