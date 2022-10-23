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

package net.galacticraft.addon.util;

public class StringBuild
{

	public static StringBuild start()
	{
		return new StringBuild();
	}

	public static StringBuild start(final int capacity)
	{
		return new StringBuild(capacity);
	}

	public static StringBuild from(final String str)
	{
		return new StringBuild(str);
	}

	public static StringBuild from(final CharSequence seq)
	{
		return new StringBuild(seq);
	}

	public final StringBuilder s;

	private StringBuild()
	{
		s = new StringBuilder();
	}

	private StringBuild(final int capacity)
	{
		s = new StringBuilder(capacity);
	}

	private StringBuild(final String str)
	{
		s = new StringBuilder(str);
	}

	private StringBuild(final CharSequence seq)
	{
		s = new StringBuilder(seq);
	}

	public StringBuilder append(final Object obj)
	{
		return this.s.append(obj);
	}

	public StringBuilder append(final String str)
	{
		return this.s.append(str);
	}

	public StringBuilder append(final StringBuffer sb)
	{
		return this.s.append(sb);
	}

	public StringBuilder append(final CharSequence charSequence)
	{
		return this.s.append(s);
	}

	public StringBuilder append(final CharSequence s, final int start, final int end)
	{
		return this.s.append(s, start, end);
	}

	public StringBuilder append(final char[] str)
	{
		return this.s.append(str);
	}

	public StringBuilder append(final char[] str, final int offset, final int len)
	{
		return this.s.append(str, offset, len);
	}

	public StringBuilder append(final boolean b)
	{
		return this.s.append(b);
	}

	public StringBuilder append(final char c)
	{
		return this.s.append(c);
	}

	public StringBuilder append(final int i)
	{
		return this.s.append(i);
	}

	public StringBuilder append(final long lng)
	{
		return this.s.append(lng);
	}

	public StringBuilder append(final float f)
	{
		return this.s.append(f);
	}

	public StringBuilder append(final double d)
	{
		return this.s.append(d);
	}

	public StringBuilder appendCodePoint(final int codePoint)
	{
		return this.s.appendCodePoint(codePoint);
	}

	public StringBuilder delete(final int start, final int end)
	{
		return this.s.delete(start, end);
	}

	public StringBuilder deleteCharAt(final int index)
	{
		return this.s.deleteCharAt(index);
	}

	public StringBuilder replace(final int start, final int end, final String str)
	{
		return this.s.replace(start, end, str);
	}

	public StringBuilder insert(final int index, final char[] str, final int offset, final int len)
	{
		return this.s.insert(index, str, offset, len);
	}

	public StringBuilder insert(final int offset, final Object obj)
	{
		return this.s.insert(offset, obj);
	}

	public StringBuilder insert(final int offset, final String str)
	{
		return this.s.insert(offset, str);
	}

	public StringBuilder insert(final int offset, final char[] str)
	{
		return this.s.insert(offset, str);
	}

	public StringBuilder insert(final int dstOffset, final CharSequence s)
	{
		return this.s.insert(dstOffset, s);
	}

	public StringBuilder insert(final int dstOffset, final CharSequence s, final int start, final int end)
	{
		return this.s.insert(dstOffset, s, start, end);
	}

	public StringBuilder insert(final int offset, final boolean b)
	{
		return this.s.insert(offset, b);
	}

	public StringBuilder insert(final int offset, final char c)
	{
		return this.s.insert(offset, c);
	}

	public StringBuilder insert(final int offset, final int i)
	{
		return this.s.insert(offset, i);
	}

	public StringBuilder insert(final int offset, final long l)
	{
		return this.s.insert(offset, l);
	}

	public StringBuilder insert(final int offset, final float f)
	{
		return this.s.insert(offset, f);
	}

	public StringBuilder insert(final int offset, final double d)
	{
		return this.s.insert(offset, d);
	}

	public int indexOf(final String str)
	{
		return this.s.indexOf(str);
	}

	public int indexOf(final String str, final int fromIndex)
	{
		return this.s.indexOf(str, fromIndex);
	}

	public int lastIndexOf(final String str)
	{
		return this.s.lastIndexOf(str);
	}

	public int lastIndexOf(final String str, final int fromIndex)
	{
		return this.s.lastIndexOf(str, fromIndex);
	}

	public StringBuilder reverse()
	{
		return this.s.reverse();
	}

	@Override
	public String toString()
	{
		return this.s.toString();
	}

	public StringBuild ln()
	{
		this.s.append("\n");
		return this;
	}

	public StringBuild appendln(final String stringToBeAppended)
	{
		this.append(stringToBeAppended);
		this.ln();
		return this;
	}

	public StringBuild lnAppend(final String stringToBeAppended)
	{
		this.ln();
		this.append(stringToBeAppended);
		return this;
	}
}
