package net.galacticraft.plugins.changelog.template;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.gradle.api.GradleException;

import com.google.common.io.FileWriteMode;

import lombok.Getter;
import net.galacticraft.common.util.FileWriterUtil;
import net.galacticraft.plugins.changelog.template.parts.FooterPart;
import net.galacticraft.plugins.changelog.template.parts.HeaderPart;
import net.galacticraft.plugins.changelog.template.parts.TypePart;

public class TemplateFile {
	
	@Getter
	private final File templateFile;
	private HeaderPart header = new HeaderPart();
	private List<TypePart> types = new ArrayList<>();
	private FooterPart footer = new FooterPart();

	private TemplateFile(Builder builder) {
		this.templateFile = builder.templateFile;
		this.header = builder.header;
		this.types = builder.types;
		this.footer = builder.footer;
		this.finalizeTemplate();
	}
	
	private void finalizeTemplate() {
		FileWriterUtil.write(templateFile, header.getContent(), FileWriteMode.APPEND);
		for(TypePart typePart : this.types) {
			FileWriterUtil.write(templateFile, typePart.getContent(), FileWriteMode.APPEND);
		}
		FileWriterUtil.write(templateFile, footer.getContent(), FileWriteMode.APPEND);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private File templateFile = newTempFile();
		private HeaderPart header = new HeaderPart();
		private List<TypePart> types = new ArrayList<>();
		private FooterPart footer = new FooterPart();

		private Builder() {
		}
		
		private final File newTempFile() {
			try {
				return File.createTempFile("changelog", "template");
			} catch (Exception e) {
				throw new GradleException("Could not create temporary changelog template file");
			}
		}

		public Builder addTypePart(final String key, final String value) {
			this.types.add(new TypePart(key, value));
			return this;
		}

		public TemplateFile build() {
			return new TemplateFile(this);
		}
	}
}
