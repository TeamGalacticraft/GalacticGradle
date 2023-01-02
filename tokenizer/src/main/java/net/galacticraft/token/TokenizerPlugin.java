package net.galacticraft.token;

import java.io.File;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.initialization.Settings;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.plugins.PluginManager;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;
import org.jetbrains.annotations.NotNull;

import net.galacticraft.gradle.core.plugin.GradlePlugin;
import net.galacticraft.gradle.core.plugin.property.BooleanProperty;

public class TokenizerPlugin extends GradlePlugin
{

    @Override
    protected void applyToProject(@NotNull Project target, @NotNull PluginManager plugins)
    {
        TokenizerExtension extension = extensions.findOrCreate("tokens", TokenizerExtension.class, target.getObjects());

        JavaPluginExtension javaConv = extensions.findOrCreate("java", JavaPluginExtension.class);

        conditionalLog.setConditional(new BooleanProperty(target, "tokens.debug", false));

        Action<SourceSet> sourceAtion = set ->
        {
            TokenizeTask task;

            String capName    = set.getName().substring(0, 1).toUpperCase() + set.getName().substring(1);
            String taskPrefix = "source" + capName;
            File   dirRoot    = new File(target.getBuildDir(), "sources/" + set.getName());
            {
                File dir = new File(dirRoot, "java");

                task = tasks.container().create(taskPrefix + "Java", TokenizeTask.class);
                task.setSource(set.getJava());
                task.setOutput(dir);
                task.setConditionalLog(conditionalLog);

                JavaCompile compile = (JavaCompile) target.getTasks().getByName(set.getCompileJavaTaskName());
                compile.dependsOn(task);
                compile.setSource(dir);
            }
        };

        for (SourceSet set : javaConv.getSourceSets())
        {
            sourceAtion.execute(set);
        }
        javaConv.getSourceSets().whenObjectAdded(sourceAtion);

        target.afterEvaluate(after ->
        {
            target.getTasks().withType(TokenizeTask.class, new Action<TokenizeTask>()
            {

                @Override
                public void execute(TokenizeTask t)
                {
                    t.replace(extension.getReplacements());
                    t.include(extension.getIncludes());
                    t.setBeginToken(extension.getBeginToken().get());
                    t.setEndToken(extension.getEndToken().get());
                }
            });
        });
    }

    @Override
    protected void applyToSettings(@NotNull Settings target, @NotNull PluginManager plugins)
    {
    }

}
