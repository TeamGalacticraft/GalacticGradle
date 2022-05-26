package net.galacticraft.plugins.changelog.template.parts;

import java.util.Arrays;
import java.util.List;

import net.galacticraft.common.util.ResourceLoader;

public class HeaderPart {
	
	private String content;
	
	public HeaderPart() {
		this.content = ResourceLoader.getResourceOrFile("header.part");
	}
	
	public List<String> getContent() {
		return Arrays.asList(this.content.split(System.getProperty("line.separator")));
	}
}
