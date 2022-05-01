package net.galacticraft.plugins.curseforge.curse;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

public enum ChangelogType {
	
    @SerializedName("text")
    TEXT("text"),
    
    @SerializedName("html")
    HTML("html"),
    
    @SerializedName("markdown")
    MARKDOWN("markdown");
    
    private final String value;
    private final static Map<String, ChangelogType> CONSTANTS = new HashMap<String, ChangelogType>();

    static {
        for (ChangelogType c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    ChangelogType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static ChangelogType fromValue(String value) {
    	if(value == null) {
    		return null;
    	}
    	String checkedValue = checkForAlternativeExtensionNames(value.toLowerCase());
        ChangelogType constant = CONSTANTS.get(checkedValue);
        if (constant == null) {
            return ChangelogType.TEXT;
        } else {
            return constant;
        }
    }
    
    private static String checkForAlternativeExtensionNames(String value) {
    	if(value.equalsIgnoreCase("txt")) {
    		return "text";
    	} 
    	else if (value.equalsIgnoreCase("htm")) {
			return "html";
		}
    	else if (value.equalsIgnoreCase("md")) {
			return "markdown";
		}
    	return value;
    }
}
