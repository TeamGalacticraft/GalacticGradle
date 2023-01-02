package net.galacticraft.changelog.markdown.impl;

import java.util.List;

import net.steppschuh.markdowngenerator.MarkdownSerializationException;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.util.StringUtil;

public class CommitList extends UnorderedList<String>
{
    public static CommitList from(UnorderedList<String> list)
    {
        CommitList newList = new CommitList();
        newList.getItems().addAll(list.getItems());
        return newList;
    }
    
    public String trySerialize()
    {
        try {
            return this.serialize();
        } catch (MarkdownSerializationException e) {
            return null;
        }
    }
    
    @Override
    public String serialize() throws MarkdownSerializationException {
        StringBuilder sb = new StringBuilder();
        for (int itemIndex = 0; itemIndex < items.size(); itemIndex++) {
            Object item = items.get(itemIndex);

            if (itemIndex > 0) {
                sb.append(StringUtil.fillUpLeftAligned("", "  ", indentationLevel * 2));
            } else if (indentationLevel > 0) {
                sb.append("  ");
            }

            if (item instanceof CommitListItem) {
                sb.append(item);
            } else if (item instanceof CommitList) {
                CommitList unorderedList = (CommitList) item;
                unorderedList.setIndentationLevel(indentationLevel + 1);
                sb.append(unorderedList);
            } else {
                sb.append(new CommitListItem((String) item));
            }

            if (itemIndex < items.size() - 1) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public List<String> getItems() {
        return items;
    }
    
    public CommitList incrementIndentation()
    {
        super.incrementIndentationLevel();
        return this;
    }
}