package net.galacticraft.plugins.publishing.nexus.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

public class ConditionalTaskCollectionBuilder {

    private List<Jar> taskList = new ArrayList<>();

    public ConditionalTaskCollectionBuilder(Project project) {
        if (project == null) {
            throw new GradleException("project cannot be null");
        }
        TaskContainer tasks = project.getTasks();
        Optional<JavaPluginExtension> optionalJavaExt = Optional.ofNullable(project.getExtensions().findByType(JavaPluginExtension.class));

        if (optionalJavaExt.isPresent()) {
            Optional<Jar> javadoc = Optional.ofNullable(tasks.withType(Jar.class).findByName(JavaPlugin.JAVADOC_TASK_NAME));
            Optional<Jar> sources = Optional.ofNullable(tasks.withType(Jar.class).findByName("sourcesJar"));

            if (javadoc.isPresent()) {
                getTasksList().add(javadoc.get());
            }
            if (sources.isPresent()) {
                getTasksList().add(sources.get());
            }

        } else {
            project.getLogger().lifecycle("JavaPluginExtension not found in project");
        }
    }

    public List<Jar> getTasksList() {
        return this.taskList;
    }
}
