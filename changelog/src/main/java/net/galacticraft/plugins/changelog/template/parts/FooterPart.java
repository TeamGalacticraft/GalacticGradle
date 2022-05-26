package net.galacticraft.plugins.changelog.template.parts;

import java.util.Arrays;
import java.util.List;

import net.galacticraft.common.util.ResourceLoader;

public class FooterPart {
	
	private String content;
	
	public FooterPart() {
		this.content = ResourceLoader.getResourceOrFile("footer.part");
	}
	
	public List<String> getContent() {
		return Arrays.asList(this.content.split(System.getProperty("line.separator")));
	}
}
