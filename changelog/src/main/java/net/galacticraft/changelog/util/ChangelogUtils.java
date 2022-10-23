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

package net.galacticraft.changelog.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.gradle.api.Action;
import org.gradle.api.InvalidUserCodeException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.publish.maven.MavenArtifact;
import org.gradle.api.publish.maven.MavenPublication;

import net.galacticraft.changelog.GenerateChangelogTask;
import net.galacticraft.changelog.git.JGit;

// TODO: Auto-generated Javadoc
/**
 * The Class ChangelogUtils.
 */
public class ChangelogUtils
{

    /** The map. */
    public static Map<String, List<RevCommit>> map   = new HashMap<>();
    public static final String[]               types = {"feat", "fix", "docs", "style", "refactor", "perf", "test", "build", "ci", "chore", "revert"};

    static
    {
        for (String type : types)
        {
            map.put(type, new ArrayList<>());
        }
    }

    /**
     * Generates a changelog string that can be written to a file from a given git directory and repository url. The changelog will be generated from the commit referenced by the given tag to the current HEAD.
     *
     * @param  project       the project
     * @param  repositoryUrl the repository url
     * @param  justText      the just text
     * @param  sourceTag     the source tag
     * 
     * @return               A multiline changelog string.
     */
    public static String generateChangelog(final Project project, final String repositoryUrl, final boolean justText, final String sourceTag)
    {
        JGit git = JGit.git(project);

        //Get the tag to commit map so that the beginning commit can be found.
        Map<String, String> tagMap = git.getTagToCommitMap();

        //Check if it even exists.
        if (!tagMap.containsKey(sourceTag))
            throw new IllegalArgumentException("The tag: " + sourceTag + " does not exist in the repository");
        //Get the commit hash from the tag.
        String commitHash = tagMap.get(sourceTag);

        git.logger().lifecycle("Tag = " + commitHash);

        ObjectId tagCommit = ObjectId.fromString(commitHash);

        //Get the current head commit.
        ObjectId headCommit = git.getHead();

        git.logger().lifecycle("Head = " + headCommit.name());

        return generateChangelogFromTo(git, repositoryUrl, justText, headCommit, tagCommit); //Generate the changelog.
    }

    /**
     * Generates a changelog string that can be written to a file from a given git directory and repository url. The changes will be generated from the given commit to the given commit.
     *
     * @param  git           The JGit instance
     * @param  repositoryUrl The github url of the repository.
     * @param  justText      Indicates if plain text ({@code true}) should be used, or changelog should be used ({@code false}).
     * @param  start         The commit ObjectId of the commit to use as the beginning of the changelog.
     * @param  end           The commit ObjectId of the commit to use as the end of the changelog.
     * 
     * @return               A multiline changelog string.
     */
    public static String generateChangelogFromTo(final JGit git, final String repositoryUrl, final boolean justText, final ObjectId start, ObjectId end)
    {
        String endCommitHash = end.toObjectId().getName(); //Grab the commit hash of the end commit.
        String startCommitHash = start.toObjectId().getName(); //Grab the commit hash of the start commit.

        String changeLogName = git.getFullBranch(); //Generate a changelog name from the current branch.
        if (changeLogName != null)
        {
            changeLogName = changeLogName.replace("refs/heads/", ""); //Replace the heads prefix with nothing to only get the name of the current branch.
        }

        Iterable<RevCommit> log = git.getCommitLogFromTo(end, start); //Get all commits between the start and the end.
        List<RevCommit> logList = JGit.toList(log); //And generate a list from it.

        //Generate the header
        String changelog = String.format("### [%s Changelog](%s/compare/%s...%s)\n", changeLogName, repositoryUrl, startCommitHash, endCommitHash);

        //Some working variables and processing patterns.
        Pattern pullRequestPattern = Pattern.compile("\\(#(?<pullNumber>[0-9]+)\\)"); //A Regex pattern to find PullRequest numbers in commit messages.

        for (final RevCommit commit : logList)
        {
            String msg = commit.getShortMessage();
            if (Arrays.stream(types).parallel().anyMatch(msg::startsWith))
            {
                for(String type : types) {
                    if(msg.startsWith(type)) {
                        map.get(type).add(commit);
                    }
                }
                git.logger().lifecycle(commit.getShortMessage());
            }
        }

        //Loop over all commits and append their message as a changelog.
        //(They are already in order from newest to oldest, so that works out for us.)
        for (final RevCommit commit : logList)
        {

            //Generate the commit message prefix.
            String commitHeader = " - ";

            int commitHeaderLength = commitHeader.length();
            commitHeader += " ";

            //Generate a prefix for each line in the commit message so that it lines up.
            String noneCommitHeaderPrefix = String.join("", Collections.nCopies(commitHeaderLength, " ")) + " ";

            //Get a processed commit message body.
            String subject = git.processCommitBody(commit.getFullMessage().trim());

            //If we generate changelog, then process the pull request numbers.
            if (!justText)
            {
                //Check if we have a pull request.
                Matcher matcher = pullRequestPattern.matcher(subject);
                if (matcher.find())
                {
                    //Grab the number
                    String pullRequestNumber = matcher.group("pullNumber");

                    //Replace the pull request number.
                    subject = String.format("[#%s](%s/pull/%s)", pullRequestNumber, repositoryUrl, pullRequestNumber);
                }
            }

            //Replace each newline in the message with a newline and a prefix so the message lines up.
            subject = subject.replaceAll("\\n", "\n" + noneCommitHeaderPrefix);

            //Append the generated entry with its header (list entry + version number)
            changelog += String.format("%s%s", commitHeader, subject);
            changelog += "\n";
        }

        return changelog;
    }

    /**
     * Sets up the default merge-base based changelog generation on the current project. Creating the default task, setting it as a dependency of the build task and adding it as a publishing artifact to any maven publication in the project.
     *
     * @param project The project to add changelog generation to.
     * @param task    the task
     */
    public static void setupChangelogGeneration(final Project project, final GenerateChangelogTask task)
    {
        //Setup the task as a dependency of the build task.
        if (project.getTasks().findByName("build") != null)
        {
            project.getTasks().getByName("build").dependsOn(task);
        }
    }

    /**
     * Sets up the tag based changelog generation on the current project. Creating the default task, setting it as a dependency of the build task and adding it as a publishing artifact to any maven publication in the project.
     *
     * @param project The project to add changelog generation to.
     * @param tag     The name of the tag to start the changelog from.
     * @param task    the task
     */
    public static void setupChangelogGenerationFromTag(final Project project, final String tag, final GenerateChangelogTask task)
    {

        //Setup the task as a dependency of the build task.
        if (project.getTasks().findByName("build") != null)
        {
            project.getTasks().getByName("build").dependsOn(task);
        }
    }

    /**
     * Finds the nearest `createChangelog` task in the project tree, searching upwards until the root is found. If no project with the task is found, an error is thrown.
     *
     * @param  project the project
     * 
     * @return         The task.
     */
    private static GenerateChangelogTask findNearestChangelogTask(final Project project)
    {
        if (project != null)
        {
            Task genTask = project.getTasks().findByName("createChangelog");
            if (genTask != null)
            {
                return project.getTasks().withType(GenerateChangelogTask.class).findByName("createChangelog");
            }
            genTask = findNearestChangelogTask(project.getParent());
            if (genTask == null)
            {
                throw new IllegalArgumentException("The project tree does not have a createChangelog task.");
            }

        }
        throw new IllegalArgumentException("The project tree does not have a createChangelog task.");
    }

    /**
     * Sets up the changelog generation on the given maven publication.
     *
     * @param project     The project in question.
     * @param publication The publication in question.
     */
    public static void setupChangelogGenerationForPublishing(final Project project, final MavenPublication publication)
    {
        try
        {
            //After evaluation run the publishing modifier.
            project.afterEvaluate(new Action<Project>()
            {

                @Override
                public void execute(final Project evaluatedProject)
                {
                    setupChangelogGenerationForPublishingAfterEvaluation(project, publication);
                }
            });
        } catch (InvalidUserCodeException ignored)
        {
            //Already in after evaluate.
            setupChangelogGenerationForPublishingAfterEvaluation(project, publication);
        }
    }

    /**
     * Setup changelog generation for publishing after evaluation.
     *
     * @param project     the project
     * @param publication the publication
     */
    private static void setupChangelogGenerationForPublishingAfterEvaluation(final Project project, final MavenPublication publication)
    {
        //Grab the task
        final GenerateChangelogTask task = findNearestChangelogTask(project);

        if (task.getProject().getTasks().findByName("build") != null)
        {
            task.getProject().getTasks().findByName("build").dependsOn(task);
        }

        //Add a new changelog artifact and publish it.
        publication.artifact(task.getOutputFile().get(), new Action<MavenArtifact>()
        {

            @Override
            public void execute(final MavenArtifact mavenArtifact)
            {
                mavenArtifact.builtBy(task);
                mavenArtifact.setClassifier("changelog");
                mavenArtifact.setExtension("txt");
            }
        });
    }
}
