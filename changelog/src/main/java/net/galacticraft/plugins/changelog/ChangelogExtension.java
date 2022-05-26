package net.galacticraft.plugins.changelog;

import javax.inject.Inject;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

import lombok.Getter;
import net.galacticraft.common.util.StringUtil;

@Getter
public class ChangelogExtension {
	
	private final Property<String> lastTag, template;
	private final MapProperty<String, String> types;
	
	@Inject
	public ChangelogExtension(final ObjectFactory factory) {
		this.lastTag = factory.property(String.class);
		this.template = factory.property(String.class).convention("default.mustache");
		this.types = factory.mapProperty(String.class, String.class).empty();
	}
	
	public void lastTag(final String tag) {
		this.lastTag.set(tag);
	}
	
	public void template(final String template) {
		this.template.set(template);
	}
	
	public void addType(final String type, final String fullName) {
		this.getTypes().put(type, fullName);
	}

	public void addType(final String type) {
		this.getTypes().put(type, StringUtil.capitalize(type));
	}
}
