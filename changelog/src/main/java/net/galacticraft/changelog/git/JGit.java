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
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.GitCommand;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RemoteConfig;
import org.gradle.api.Project;
import org.gradle.api.logging.Logger;

import com.google.common.base.Preconditions;

// TODO: Auto-generated Javadoc
/**
 * The Class JGit.
 */
public class JGit
{

    /** The jgit. */
    private static JGit jgit;

    /**
     * Git.
     *
     * @param project the project
     * @return the j git
     */
    public static JGit git(final Project project)
    {
        if (JGit.jgit == null)
        {
            JGit.jgit = new JGit(project);
        }

        return JGit.jgit;
    }

    /** The project. */
    private final Project    project;
    
    /** The git. */
    private final Git        git;
    
    /** The repo. */
    private final Repository repo;

    /**
     * Instantiates a new j git.
     *
     * @param project the project
     */
    private JGit(Project project)
    {
        this.project = project;
        File dir = project.getProjectDir();
        try
        {
            this.git = Git.open(dir);
            this.repo = Git.open(dir).getRepository();

            project.getLogger().lifecycle("Repository: \n" + repo.toString());

        } catch (IOException e)
        {
            throw new RuntimeException();
        }
    }
    
    /**
     * Gets the single instance of JGit.
     *
     * @return single instance of JGit
     */
    public Git getInstance()
    {
        return git;
    }

    /**
     * Logger.
     *
     * @return the logger
     */
    public Logger logger()
    {
        return project.getLogger();
    }

    /**
     * Gets the latest commit.
     *
     * @return the latest commit
     */
    public RevCommit getLatestCommit()
    {
        try
        {
            return git.log().setMaxCount(1).call().iterator().next();
        } catch (GitAPIException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the head commit of the given git workspace.
     *
     * @return     The head commit.
     */
    public ObjectId getHead()
    {
        try
        {
            return repo.resolve(Constants.HEAD);
        } catch (RevisionSyntaxException | IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Determines the commit that the given ref references.
     *
     * @param other the other
     * @return       The commit referenced by the given reference in the given git workspace.
     */
    public RevCommit getCommitFromRef(final Ref other)
    {

        return getCommitFromId(other.getObjectId());
    }

    /**
     * Returns the first commit in the repository.
     *
     * @return     The first commit.
     */
    public RevCommit getFirstCommitInRepository()
    {
        List<RevCommit> commitList = new ArrayList<RevCommit>();
        try
        {
            Iterable<RevCommit> commits = call(git.log());
            commitList = toList(commits);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        if (commitList.isEmpty())
            return null;

        return commitList.get(commitList.size() - 1);
    }

    /**
     * Gets the full branch.
     *
     * @return the full branch
     */
    public String getFullBranch()
    {
        try
        {
            return repo.getFullBranch();
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets the log.
     *
     * @return the log
     */
    public List<RevCommit> getLog()
    {
        try
        {
            return toList(git.log().call());
        } catch (GitAPIException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Remote list.
     *
     * @return the list
     */
    public List<RemoteConfig> remoteList()
    {
        return call(git.remoteList());
    }

    /**
     * Processes the commit body of a commit stripping out unwanted information.
     *
     * @param body the body
     * @return      The result of the processing.
     */
    public String processCommitBody(final String body)
    {
        final String[] bodyLines = body.split("\n"); //Split on newlines.
        final List<String> resultingLines = new ArrayList<>();
        for (String bodyLine : bodyLines)
        {
            if (bodyLine.startsWith("Signed-off-by: ")) //Remove all the signed of messages.
                continue;

            if (bodyLine.trim().isEmpty()) //Remove empty lines.
                continue;

            resultingLines.add(bodyLine);
        }

        return String.join("\n", resultingLines).trim(); //Join the result again.
    }

    /**
     * Get all available remote branches in the git workspace.
     *
     * @return                 A list of remote branches.
     * @throws GitAPIException the git API exception
     */
    public List<Ref> getAvailableRemoteBranches() throws GitAPIException
    {
        ListBranchCommand command = git.branchList();
        ListMode mode = ListBranchCommand.ListMode.REMOTE;
        setPrivateValue(ListBranchCommand.class, command, mode, "listMode");
        return command.call();
    }

    /**
     * Gets the commit message from the start commit to the end. Returns it in youngest to oldest order (so from end to start).
     *
     * @param start the start
     * @param end the end
     * @return                              The commit log.
     */
    public Iterable<RevCommit> getCommitLogFromTo(final ObjectId start, final ObjectId end)
    {
        LogCommand log = command$addRange(start, end);

        return call(log);
    }

    /**
     * Builds a map of tag name to commit hash.
     *
     * @return                 The tags to commit hash map.
     */
    public Map<String, String> getTagToCommitMap()
    {
        final Map<String, String> versionMap = new HashMap<>();

        try
        {
            for (Ref tag : git.tagList().call())
            {

                ObjectId id;

                ObjectId peeled = git.getRepository().getRefDatabase().peel(tag).getPeeledObjectId();
                if (peeled != null)
                {
                    id = peeled;
                } else
                {
                    id = tag.getObjectId();
                }

                versionMap.put(tag.getName().replace(Constants.R_TAGS, ""), id.name());
            }
        } catch (Exception e)
        {
        }

        return versionMap;
    }

    //@noformat
    /**
     * Parse a git revision string and return an object id.
     * 
     * Combinations of these operators are supported:
     * <ul>
     * <li><b>HEAD</b>, <b>MERGE_HEAD</b>, <b>FETCH_HEAD</b></li>
     * <li><b>SHA-1</b>: a complete or abbreviated SHA-1</li>
     * <li><b>refs/...</b>: a complete reference name</li>
     * <li><b>short-name</b>: a short reference name under {@code refs/heads},
     * {@code refs/tags}, or {@code refs/remotes} namespace</li>
     * <li><b>tag-NN-gABBREV</b>: output from describe, parsed by treating
     * {@code ABBREV} as an abbreviated SHA-1.</li>
     * <li><i>id</i><b>^</b>: first parent of commit <i>id</i>, this is the same
     * as {@code id^1}</li>
     * <li><i>id</i><b>^0</b>: ensure <i>id</i> is a commit</li>
     * <li><i>id</i><b>^n</b>: n-th parent of commit <i>id</i></li>
     * <li><i>id</i><b>~n</b>: n-th historical ancestor of <i>id</i>, by first
     * parent. {@code id~3} is equivalent to {@code id^1^1^1} or {@code id^^^}.</li>
     * <li><i>id</i><b>:path</b>: Lookup path under tree named by <i>id</i></li>
     * <li><i>id</i><b>^{commit}</b>: ensure <i>id</i> is a commit</li>
     * <li><i>id</i><b>^{tree}</b>: ensure <i>id</i> is a tree</li>
     * <li><i>id</i><b>^{tag}</b>: ensure <i>id</i> is a tag</li>
     * <li><i>id</i><b>^{blob}</b>: ensure <i>id</i> is a blob</li>
     * </ul>
     * 
     * <p>
     * The following operators are specified by Git conventions, but are not
     * supported by this method:
     * <ul>
     * <li><b>ref@{n}</b>: n-th version of ref as given by its reflog</li>
     * <li><b>ref@{time}</b>: value of ref at the designated time</li>
     * </ul>
     *
     * @param string the string
     * @return an ObjectId or {@code null} if revstr can't be resolved to any
     *         ObjectId
     */
    //@format
    public ObjectId resolve(final String string)
    {
        try
        {
            return repo.resolve(string);
        } catch (RevisionSyntaxException | IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Command$add range.
     *
     * @param since the since
     * @param until the until
     * @return the log command
     */
    public LogCommand command$addRange(ObjectId since, ObjectId until)
    {
        try
        {
            return git.log().addRange(since, until);
        } catch (MissingObjectException | IncorrectObjectTypeException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Command$add.
     *
     * @param id the id
     * @return the log command
     */
    public LogCommand command$add(AnyObjectId id)
    {
        try
        {
            return git.log().add(id);
        } catch (MissingObjectException | IncorrectObjectTypeException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Command$not.
     *
     * @param id the id
     * @return the log command
     */
    public LogCommand command$not(AnyObjectId id)
    {
        try
        {
            return git.log().not(id);
        } catch (MissingObjectException | IncorrectObjectTypeException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Determines the commit that the given object references.
     *
     * @param other the other
     * @return       The commit referenced by the given object in the given git workspace.
     */
    public RevCommit getCommitFromId(final ObjectId other)
    {
        try (RevWalk revWalk = new RevWalk(repo))
        {
            return revWalk.parseCommit(other);

        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sets the private value.
     *
     * @param <T> the generic type
     * @param <E> the element type
     * @param classToAccess the class to access
     * @param instance the instance
     * @param value the value
     * @param fieldName the field name
     */
    public static <T, E> void setPrivateValue(Class<? super T> classToAccess, @Nullable T instance, @Nullable E value, String fieldName)
    {
        try
        {
            findField(classToAccess, fieldName).set(instance, value);
        } catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    /**
     * Call.
     *
     * @param <T> the generic type
     * @param cmd the cmd
     * @return the t
     */
    public <T extends Iterable<?>> T call(GitCommand<T> cmd)
    {
        try
        {
            return (T) cmd.call();
        } catch (GitAPIException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find field.
     *
     * @param clazz the clazz
     * @param fieldName the field name
     * @return the field
     */
    public static Field findField(@Nonnull Class<?> clazz, @Nonnull String fieldName)
    {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkArgument(isNotEmpty(fieldName), "Field name cannot be empty");

        try
        {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (Exception e)
        {
            e.getStackTrace();
        }
        return null;
    }

    /**
     * Checks if is empty.
     *
     * @param cs the cs
     * @return true, if is empty
     */
    public static boolean isEmpty(final CharSequence cs)
    {
        return cs == null || cs.length() == 0;
    }

    /**
     * Checks if is not empty.
     *
     * @param cs the cs
     * @return true, if is not empty
     */
    public static boolean isNotEmpty(final CharSequence cs)
    {
        return !isEmpty(cs);
    }

    /**
     * To list.
     *
     * @param <T> the generic type
     * @param self the self
     * @return the list
     */
    public static <T> List<T> toList(Iterator<T> self)
    {

        List<T> answer = new ArrayList<>();
        while (self.hasNext())
        {
            answer.add(self.next());
        }
        return answer;
    }

    /**
     * To list.
     *
     * @param <T> the generic type
     * @param self the self
     * @return the list
     */
    public static <T> List<T> toList(Iterable<T> self)
    {
        return toList(self.iterator());
    }

    /**
     * To list.
     *
     * @param <T> the generic type
     * @param self the self
     * @return the list
     */
    public static <T> List<T> toList(Enumeration<T> self)
    {
        List<T> answer = new ArrayList<>();
        while (self.hasMoreElements())
        {
            answer.add(self.nextElement());
        }
        return answer;
    }
}
