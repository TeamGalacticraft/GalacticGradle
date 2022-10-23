package net.galacticraft.internal.maven;

import javax.annotation.Nullable;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.gradle.api.GradleException;

final class Util
{
	@SafeVarargs
	static final <T extends @NonNull Object> boolean allNotNull(@Nullable T... objects)
	{
		for(T obj : objects)
		{
			if(obj == null)
			{
				return false;
			}
		}
		
		return true;
	}

	@SafeVarargs
	static final <T extends @NonNull Object> T[] cantBeNull(@Nullable T... objects)
	{
		for(T obj : objects)
		{
			if (obj == null)
			{
				throw new GradleException(obj + " must not be null");
			}
		}

		return objects;
	}
	
	static boolean containsIgnoreCase(CharSequence str, CharSequence searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        final int len = searchStr.length();
        final int max = str.length() - len;
        for (int i = 0; i <= max; i++) {
            if (regionMatches(str, true, i, searchStr, 0, len)) {
                return true;
            }
        }
        return false;
    }

    static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length)
	{
		if (cs instanceof String && substring instanceof String)
		{
			return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
		}
		int	index1	= thisStart;
		int	index2	= start;
		int	tmpLen	= length;
		final int	srcLen		= cs.length() - thisStart;
		final int	otherLen	= substring.length() - start;
		if (thisStart < 0 || start < 0 || length < 0)
		{
			return false;
		}
		if (srcLen < length || otherLen < length)
		{
			return false;
		}
		while (tmpLen-- > 0)
		{
			final char	c1	= cs.charAt(index1++);
			final char	c2	= substring.charAt(index2++);
			if (c1 == c2)
			{
				continue;
			}
			if (!ignoreCase)
			{
				return false;
			}
			final char	u1	= Character.toUpperCase(c1);
			final char	u2	= Character.toUpperCase(c2);
			if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2))
			{
				return false;
			}
		}
		return true;
	}
}
