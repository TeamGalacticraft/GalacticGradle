package net.galacticraft.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
	
	public static String capitalize(String s) {
		if (s.length() == 0) {
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}
}
