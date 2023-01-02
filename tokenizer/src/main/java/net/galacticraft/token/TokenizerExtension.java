package net.galacticraft.token;

import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;
import org.gradle.api.provider.Property;

import lombok.Getter;

@Getter
public class TokenizerExtension
{
	private Property<String> beginToken, endToken;
	private MapProperty<Object, Object>	replacements;
	private ListProperty<String>		includes;

	@Inject
	public TokenizerExtension(final ObjectFactory factory)
	{
		this.replacements = factory.mapProperty(Object.class, Object.class).empty();
		this.includes = factory.listProperty(String.class).empty();
		this.beginToken = factory.property(String.class).convention("${");
		this.endToken = factory.property(String.class).convention("}");
	}

	public void beginToken(String beginToken)
	{
		this.beginToken.set(beginToken);
	}
	
	public void endToken(String endToken)
	{
		this.endToken.set(endToken);
	}
	
	public void withToken(Object token, Object replacement)
	{
	    this.replacements.put(token, replacement);
	}

	public void withTokens(Map<Object, Object> map)
	{
		for (Entry<Object, Object> e : map.entrySet())
		{
		    withToken(e.getKey(), e.getValue());
		}
	}

	public void replaceIn(String pattern)
	{
	    this.includes.add(pattern);
	}
	
	public void replaceIn(String pattern, Map<Object, Object> action)
	{
	    this.includes.add(pattern);
		this.replacements.putAll(action);
	}
}
