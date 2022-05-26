package net.galacticraft.plugins.changelog.template.parts;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.galacticraft.common.util.ResourceLoader;

public class TypePart {
	
	private String content;
	
	public TypePart(final String key, final String value) {
		this.content = ResourceLoader.getResourceOrFile("type.part");
		this.replaceContent(key, value);
	}
	
	private void replaceContent(final String key, final String value) {
		this.replaceKey(key);
		this.replaceValue(value);
	}
	
	private void replaceKey(final String key) {
        final Pattern pattern = Pattern.compile("%%K%%", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(this.content);
        this.content = matcher.replaceAll(key);
	}
	
	private void replaceValue(final String value) {
        final Pattern pattern = Pattern.compile("%%V%%", Pattern.MULTILINE);
        final Matcher matcher = pattern.matcher(this.content);
        this.content = matcher.replaceAll(value);
	}
	
	public List<String> getContent() {
		return Arrays.asList(this.content.split(System.getProperty("line.separator")));
	}
}
