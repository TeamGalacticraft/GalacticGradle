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

package net.galacticraft.changelog.git;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import net.galacticraft.gradle.core.util.Checks;
import net.steppschuh.markdowngenerator.list.UnorderedList;

@Getter
public class CatagorizedCommits
{
    private ParentCatagory breakingChangesCommits;

    private List<String> featureCommits;

    private List<String> fixCommits;

    private ParentCatagory miscCommits;

    public CatagorizedCommits()
    {
        this.breakingChangesCommits = new ParentCatagory();
        this.featureCommits = new ArrayList<>();
        this.miscCommits = new ParentCatagory();
        this.fixCommits = new ArrayList<>();
    }

    public void addCommit(Commit commit)
    {
        Checks.notNull(commit);
        switch (commit.getType()) {
            case BREAKING_CHANGES:
                breakingChangesCommits.add(commit);
                break;
            case FEATURES:
                featureCommits.add(commit.getMessage());
                break;
            case FIXES:
                fixCommits.add(commit.getMessage());
                break;
            case MISC:
                miscCommits.add(commit);
                break;
        }
    }

    public class ParentCatagory
    {
        @Getter
        private Map<String, UnorderedList<String>> map = new HashMap<>();

        public void createMap(List<String> list)
        {

        }

        public void add(Commit commit)
        {
            Checks.notNull(commit);
            if (this.map.containsKey(commit.getActualType())) {
                this.map.get(commit.getActualType()).getItems().add(commit.getMessage());
            } else {
                this.map.put(commit.getActualType(), new UnorderedList<>());
                this.map.get(commit.getActualType()).getItems().add(commit.getMessage());
            }
        }
    }
}
