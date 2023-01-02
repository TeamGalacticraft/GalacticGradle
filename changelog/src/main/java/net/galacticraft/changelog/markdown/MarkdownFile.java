/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.changelog.markdown;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import net.galacticraft.changelog.git.CatagorizedCommits.ParentCatagory;
import net.galacticraft.changelog.git.CommitParser;
import net.galacticraft.changelog.markdown.impl.CommitList;
import net.galacticraft.changelog.markdown.impl.SingularList;
import net.galacticraft.gradle.core.version.Version;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

public class MarkdownFile
{
    private final ListSection features, fixes;

    private final ParentListSection breakingChanges, misc;

    private Heading changelogTitle;

    public MarkdownFile(CommitParser parser)
    {
        this.breakingChanges = buildParent("BREAKING CHANGES", parser.getCatagories().getBreakingChangesCommits());
        this.features = buildSection("Features", parser.getCatagories().getFeatureCommits());
        this.fixes = buildSection("Fixes", parser.getCatagories().getFixCommits());
        this.misc = buildParent("Misc", parser.getCatagories().getMiscCommits());
        this.changelogTitle = new Heading(parser.getGit().getRepositoryName(), 1);
    }

    public MarkdownFile(CommitParser parser, Version version)
    {
        this(parser);
        this.changelogTitle = new Heading(parser.getGit().getRepositoryName() + " | " + version.toString(), 1);
    }

    private ListSection buildSection(String title, List<String> list)
    {
        ListSection section = new ListSection(title);
        List<String> items = section.getList().getItems();
        list.forEach(items::add);
        return section;
    }

    private ParentListSection buildParent(String title, ParentCatagory misc)
    {
        ParentListSection miscSection = new ParentListSection(title);
        miscSection.setSections(misc.getMap());
        return miscSection;
    }

    public String getChangelog()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(changelogTitle.toString()).append("\n\n");

        if (breakingChanges.containsElements())
            breakingChanges.appendln(sb);

        if (features.containsElements())
            features.append(sb);

        if (fixes.containsElements())
            fixes.append(sb);

        if (misc.containsElements())
            misc.append(sb);

        return sb.toString();
    }

    private class ParentListSection
    {
        private final Heading title;

        @Setter
        private Map<String, UnorderedList<String>> sections;

        ParentListSection(final String title)
        {
            this.title = new Heading(title, 2);
        }

        boolean containsElements()
        {
            return this.sections.keySet().size() > 0;
        }

        void append(StringBuilder builder)
        {
            if(!sections.isEmpty())
            {
                builder.append(this.title.toString()).append("\n");
                for (String key : this.sections.keySet()) {
                    UnorderedList<String> unorderedList = this.sections.get(key);
                    BoldText boldTitle = new BoldText("(" + key + ")");
                    if (unorderedList.getItems().size() > 0) {
                        builder.append(new SingularList(boldTitle.toString()).trySerialize()).append("\n");
                        builder.append(CommitList.from(unorderedList).incrementIndentation().toString()).append("\n");
                    }
                }
            }
        }
        
        void appendln(StringBuilder builder)
        {
            if(!sections.isEmpty())
            {
                builder.append(this.title.toString()).append("\n");
                for (String key : this.sections.keySet()) {
                    UnorderedList<String> unorderedList = this.sections.get(key);
                    BoldText boldTitle = new BoldText("(" + key + ")");
                    if (unorderedList.getItems().size() > 0) {
                        builder.append(new SingularList(boldTitle.toString()).trySerialize()).append("\n");
                        builder.append(CommitList.from(unorderedList).incrementIndentation().toString()).append("\n").append("\n");
                    }
                }
            }
        }
    }

    @Getter
    private class ListSection
    {
        private final Heading title;

        private CommitList list;

        ListSection(final String title)
        {
            this.title = new Heading(title, 2);
            this.list = new CommitList();
        }

        boolean containsElements()
        {
            return this.list.getItems().size() > 0;
        }

        void append(StringBuilder builder)
        {
            builder.append(this.title.toString()).append("\n");
            builder.append(this.list.trySerialize()).append("\n").append("\n");
        }
    }
}
