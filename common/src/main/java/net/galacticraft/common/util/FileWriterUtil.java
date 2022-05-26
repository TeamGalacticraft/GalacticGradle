package net.galacticraft.common.util;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.gradle.api.GradleException;

import com.google.common.io.CharSink;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileWriterUtil {
	
	public static void write(File file, List<String> linesToWrite, FileWriteMode mode) {
		try {
			CharSink writer = Files.asCharSink(file, Charset.forName("UTF-8"), mode);
			writer.writeLines(linesToWrite);
		} catch (Exception e) {
			file.delete();
			throw new GradleException("Writting failure to file " + file + ": " + e.getMessage());
		}
	}
}
