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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;

import lombok.Getter;
import net.galacticraft.changelog.ChangelogExtension;
import net.galacticraft.gradle.core.util.Checks;
import net.galacticraft.gradle.core.version.Version;

public class CommitParser
{
    // Typically, our changelogs are for Minecraft Mods. Players are
    // not going to care about commits that change our build scripts
    // or ci scripts, or even tests. Therefore, unless overriden via
    // ChangelogExtension, we only want the commits with these types.
    private List<String> types;

    @Getter
    private CatagorizedCommits catagories;

    @Getter
    private JGit git;

    public CommitParser(final File dir, String sourceTag)
    {
        Checks.notNull(dir);
        Checks.notNull(sourceTag);
        this.catagories = new CatagorizedCommits();
        this.types = Arrays.asList("feat", "fix", "perf", "refactor", "revert", "docs", "style");
        this.git = JGit.git(dir);
        this.walkRepository(sourceTag);
    }

    public CommitParser(ChangelogExtension extension)
    {
        this.catagories = new CatagorizedCommits();
        this.git = JGit.git(extension.getProject());
        this.types = extension.getCommitTypes().get();
        this.walkRepository(extension.getTag().get());
    }

    private void walkRepository(String source)
    {
        Version version;
        
        if(source == null)
            version = git.getTagVersionSet(false).latest();
        else
            version = this.git.getTagVersionSet(false).getVersion(source);
        
        Map<Version, ObjectId> tagMap = git.getTagToCommitMap(false);
        
        if (!tagMap.containsKey(version))
            throw new IllegalArgumentException(
                "The tag: " + version.toString() + " does not exist in the repository");
        ObjectId commitHash = tagMap.get(version);
        ObjectId tagCommit = commitHash;
        ObjectId headCommit = git.getHead();

        String changeLogName = git.getFullBranch();
        if (changeLogName != null) {
            changeLogName = changeLogName.replace("refs/heads/", "");
        }

        this.catagorize(git.getCommitLogFromTo(tagCommit, headCommit));
    }

    private void catagorize(Iterable<RevCommit> commitLog)
    {
        List<String> miscTypes = new ArrayList<>(this.types);
        miscTypes.removeAll(Arrays.asList("fix", "feat"));
        this.catagories.getMiscCommits().createMap(miscTypes);
        this.catagories.getBreakingChangesCommits().createMap(types);
        
        RegexHandler handler = new RegexHandler(this.types);
        for (RevCommit commit : commitLog) {
            handler.parseCommit(commit);

            if (handler.isFound()) {
                //@off
                Commit initial = Commit.builder()
                    .gitHash(commit.getId().abbreviate(8).name())
                    .isBreakingChange(handler.isBreakingChange())
                    .actualType(handler.getType())
                    .scope(handler.getScope())
                    .fullCommitText(handler.getFullMatch())
                    .message(this.handleMessage(handler, commit.getId()))
                    .build();
                    
                Commit full = initial.toBuilder()
                    .type(initial.configureType())
                    .build();
                //@on

                this.catagories.addCommit(full);
            }
        }
    }

    private String handleMessage(RegexHandler handler, ObjectId objectId)
    {
        String msg = handler.getMessage();
        Matcher prm = Pattern.compile("\\((#[0-9]+)\\)").matcher(msg);
        if (prm.find()) {
            msg = msg.replace(prm.group(0),
                String.format("([%s](%s/pull/%s))", prm.group(1), this.git.getRepositoryUrl("origin"), prm.group(1).substring(1)));
        } else {
            String commitUrl = String.format("([%s](%s/commit/%s))", objectId.abbreviate(8).name(),
                git.getRepositoryUrl("origin"), objectId.getName());
            msg = String.format("%s %s", msg, commitUrl);
        }

        if (handler.getScope() != null) {
            msg = String.format("(%s) %s", handler.getScope(), msg);
        }

        return msg;
    }
}
