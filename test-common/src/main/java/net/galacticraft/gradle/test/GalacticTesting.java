package net.galacticraft.gradle.test;

import static org.junit.jupiter.api.Assertions.fail;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;

public class GalacticTesting {
  public static Project project() {
    return project(null);
  }

  public static Project project(final @Nullable Consumer<ProjectBuilder> consumer) {
    final ProjectBuilder builder = ProjectBuilder.builder();
    if(consumer != null) {
      consumer.accept(builder);
    }
    return builder.build();
  }

  public static String exec(final Path workingDir, final String... cli) throws IOException {
      final StringWriter error = new StringWriter();
      final StringWriter output = new StringWriter();
      final Process process = new ProcessBuilder(cli)
          .directory(workingDir.toFile())
          .redirectError(ProcessBuilder.Redirect.PIPE)
          .redirectOutput(ProcessBuilder.Redirect.PIPE)
          .start();

      try (final InputStreamReader stdout = new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8);
          final InputStreamReader stderr = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8)) {
          int read;
          final char[] data = new char[8192];
          while (process.isAlive()) {
              if ((read = stdout.read(data)) != -1) {
                  output.write(data, 0, read);
              }
              if ((read = stderr.read(data)) != -1) {
                  error.write(data, 0, read);
              }
          }
      }

      if (process.exitValue() != 0) {
          System.err.println("====== Standard error =====");
          System.err.println(error.getBuffer());
          System.err.println("====== Standard output =====");
          System.err.println(output.getBuffer());
          fail("Process " + String.join(" ", cli) + " exited with code " + process.exitValue());
      }

      return output.getBuffer().toString();
  }
}
