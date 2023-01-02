package net.galacticraft.token;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryTree;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.util.PatternSet;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import groovy.lang.Closure;
import net.galacticraft.gradle.core.plugin.GradlePlugin.ConditionalLog;

public class TokenizeTask extends DefaultTask
{

    @InputFiles
    SourceDirectorySet source;

    HashMap<Object, Object> replacements = new HashMap<Object, Object>();

    ArrayList<String> includes = new ArrayList<String>();

    String beginToken;

    String endToken;

    ConditionalLog conditionalLogger;

    @OutputDirectory
    Object output;

    @SuppressWarnings("unchecked")
    @TaskAction
    public void doTask() throws IOException
    {
        PatternSet patterns = new PatternSet();
        patterns.setIncludes(source.getIncludes());
        patterns.setExcludes(source.getExcludes());

        File out = getOutput();
        if (out.exists())
            deleteDir(out);

        out.mkdirs();
        out = out.getCanonicalFile();

        HashMap<String, String> repl = new HashMap<String, String>(replacements.size());
        for (Entry<Object, Object> e : replacements.entrySet())
        {
            if (e.getKey() == null || e.getValue() == null)
                continue;

            Object val;
            if (e.getValue() instanceof Property<?>)
            {
                Property<?> prop = (Property<?>) e.getValue();
                val = prop.get();
            } else
            {
                val = e.getValue();
            }
            while (val instanceof Closure)
                val = ((Closure<Object>) val).call();

            repl.put(Pattern.quote(beginToken + e.getKey() + endToken), val.toString());
        }

        conditionalLogger.lifecycle("REPLACE >> " + repl);

        for (DirectoryTree dirTree : source.getSrcDirTrees())
        {
            File dir = dirTree.getDir();
            conditionalLogger.lifecycle("PARSING DIR >> " + dir);

            if (!dir.exists() || !dir.isDirectory())
                continue;
            else
                dir = dir.getCanonicalFile();

            FileTree tree = getProject().fileTree(dir).matching(source.getFilter()).matching(patterns);

            for (File file : tree)
            {
                File dest = getDest(file, dir, out);
                dest.getParentFile().mkdirs();
                dest.createNewFile();

                if (isIncluded(file))
                {
                    conditionalLogger.lifecycle("PARSING FILE IN >> " + file);
                    String text = Files.asCharSource(file, Charsets.UTF_8).read();

                    for (Entry<String, String> entry : repl.entrySet())
                        text = text.replaceAll(entry.getKey(), entry.getValue());

                    conditionalLogger.lifecycle("PARSING FILE OUT >> " + dest);
                    Files.asCharSink(dest, Charsets.UTF_8).write(text);
                } else
                {
                    Files.copy(file, dest);
                }
            }
        }
    }

    private File getDest(File in, File base, File baseOut) throws IOException
    {
        String relative = in.getCanonicalPath().replace(base.getCanonicalPath(), "");
        return new File(baseOut, relative);
    }

    private boolean isIncluded(File file) throws IOException
    {
        if (includes.isEmpty())
            return true;

        String path = file.getCanonicalPath().replace('\\', '/');
        for (String include : includes)
        {
            if (path.endsWith(include.replace('\\', '/')))
                return true;
        }

        return false;
    }

    private boolean deleteDir(File dir)
    {
        if (dir.exists())
        {
            File[] files = dir.listFiles();
            if (null != files)
            {
                for (int i = 0; i < files.length; i++)
                {
                    if (files[i].isDirectory())
                    {
                        deleteDir(files[i]);
                    } else
                    {
                        files[i].delete();
                    }
                }
            }
        }
        return (dir.delete());
    }

    public void setConditionalLog(ConditionalLog conditionalLog)
    {
        this.conditionalLogger = conditionalLog;
    }

    public void setBeginToken(String beginToken)
    {
        this.beginToken = beginToken;
    }

    public void setEndToken(String endToken)
    {
        this.endToken = endToken;
    }

    public File getOutput()
    {
        return getProject().file(output);
    }

    public void setOutput(Object output)
    {
        this.output = output;
    }

    public void setSource(SourceDirectorySet source)
    {
        this.source = source;
    }

    public FileCollection getSource()
    {
        return source;
    }

    public void replace(String key, Object val)
    {
        replacements.put(key, val);
    }

    public void replace(MapProperty<Object, Object> map)
    {
        replacements.putAll(map.get());
    }

    @Input
    public HashMap<Object, Object> getReplacements()
    {
        return replacements;
    }

    public void include(String str)
    {
        includes.add(str);
    }

    public void include(ListProperty<String> strs)
    {
        includes.addAll(strs.get());
    }

    @Input
    public ArrayList<String> getIncudes()
    {
        return includes;
    }
}
