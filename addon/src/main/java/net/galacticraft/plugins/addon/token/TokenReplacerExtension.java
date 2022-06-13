package net.galacticraft.plugins.addon.token;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.MapProperty;

public class TokenReplacerExtension {
	
	private MapProperty<String, Object> replacements;
	private ListProperty<String> includes;
	
	public TokenReplacerExtension(final ObjectFactory factory) {
		this.replacements = factory.mapProperty(String.class, Object.class).empty();
		this.includes = factory.listProperty(String.class).empty();
	}
	
    /**
     * Add a source replacement mapping
     *
     * @param token       The token to replace
     * @param replacement The value to replace with
     */
    public void replace(Object token, Object replacement)
    {
        replacements.put(token.toString(), replacement);
    }

    /**
     * Add a map of source replacement mappings
     *
     * @param map A map of tokens -&gt; replacements
     */
    public void replace(Map<Object, Object> map)
    {
        for (Entry<Object, Object> e : map.entrySet())
        {
            replace(e.getKey(), e.getValue());
        }
    }

    /**
     * Get all of the source replacement tokens and values
     *
     * @return A map of tokens -&gt; replacements
     */
    public Map<String, Object> getReplacements()
    {
        return replacements.get();
    }
    
    /**
     * Get a list of file patterns that will be used to determine included source replacement files.
     *
     * @return A list of classes
     */
    public List<String> getIncludes()
    {
        return includes.get();
    }

    /**
     * Add a file pattern to be used in source replacement. {@code file.getPath().endsWith(pattern)} is used to determine included files.<br>
     * This is an addative operation, so multiple calls are allowed
     *
     * @param pattern The pattern
     */
    public void replaceIn(String pattern)
    {
        includes.add(pattern);
    }
}
