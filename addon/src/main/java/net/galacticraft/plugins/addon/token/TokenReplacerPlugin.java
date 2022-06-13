package net.galacticraft.plugins.addon.token;

import java.io.File;

import org.gradle.api.Action;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.compile.JavaCompile;

import net.galacticraft.common.plugins.GradlePlugin;

public class TokenReplacerPlugin extends GradlePlugin {

	TokenReplacerExtension extension;
	
	@Override
	public void plugin() {
		extension = extensions().findOrCreate("tokens", TokenReplacerExtension.class);
		createSourceCopyTasks();
	}

	@Override
	protected void afterEvaluate() {
		super.afterEvaluate();
        project().getTasks().withType(ReplaceTokensTask.class, new Action<ReplaceTokensTask>() {
            @Override
            public void execute(ReplaceTokensTask t)
            {
                t.replace(extension.getReplacements());
                t.include(extension.getIncludes());
            }
        });
	}

	protected void createSourceCopyTasks() {
		JavaPluginExtension javaConv = extensions().findOrCreate("java", JavaPluginExtension.class);

		Action<SourceSet> action = new Action<SourceSet>() {
			@Override
			public void execute(SourceSet set) {

				ReplaceTokensTask task;

				String capName = set.getName().substring(0, 1).toUpperCase() + set.getName().substring(1);
				String taskPrefix = "source" + capName;
				File dirRoot = new File(project().getBuildDir(), "sources/" + set.getName());

				// java
				{
					File dir = new File(dirRoot, "java");

					task = makeTask(taskPrefix + "Java", ReplaceTokensTask.class);
					task.setSource(set.getJava());
					task.setOutput(dir);

					// must get replacements from extension afterEvaluate()

					JavaCompile compile = (JavaCompile) project().getTasks().getByName(set.getCompileJavaTaskName());
					compile.dependsOn(task);
					compile.setSource(dir);
				}
			}
		};

		// for existing sourceSets
		for (SourceSet set : javaConv.getSourceSets()) {
			action.execute(set);
		}
		// for user-defined ones
		javaConv.getSourceSets().whenObjectAdded(action);
	}
}
