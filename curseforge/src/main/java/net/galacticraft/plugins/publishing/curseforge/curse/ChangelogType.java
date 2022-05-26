/*
 * This file is part of GalacticGradle, licensed under the MIT License (MIT).
 *
 * Copyright (c) TeamGalacticraft <https://galacticraft.net>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.galacticraft.plugins.publishing.curseforge.curse;

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
