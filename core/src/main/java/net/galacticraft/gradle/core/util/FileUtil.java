package net.galacticraft.gradle.core.util;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.jetbrains.annotations.Nullable;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil
{
    @Nullable
    public static File resolveFile(Project project, Object in) {
        if (in == null) {
            return null;
        } else if (in instanceof File) {
            return (File) in;
        } else if (in instanceof AbstractArchiveTask) {
            return ((AbstractArchiveTask) in).getArchiveFile().get().getAsFile();
        } else if (in instanceof TaskProvider<?>) {
            Object provided = ((TaskProvider<?>) in).get();

            if (provided instanceof AbstractArchiveTask) {
                return ((AbstractArchiveTask) provided).getArchiveFile().get().getAsFile();
            }
        }

        return project.file(in);
    }
}
