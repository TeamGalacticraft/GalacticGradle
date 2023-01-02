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

package net.galacticraft.addon.util;

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
