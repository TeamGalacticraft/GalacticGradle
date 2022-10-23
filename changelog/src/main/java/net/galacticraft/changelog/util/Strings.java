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

package net.galacticraft.changelog.util;


public class Strings
{
    /**
     * Pads a CharSequence to a minimum length specified by {@code numberOfChars} by adding the space character
     * to the left as many times as needed.
     *
     * If the String is already the same size or bigger than the target {@code numberOfChars}, then the original String is returned.
     * 
     * @param self          a CharSequence object
     * @param numberOfChars the total minimum number of characters of the resulting CharSequence
     * @return the CharSequence padded to the left as a String
     *
     * @see #padLeft(CharSequence,Number,CharSequence)
     */
    public static String padLeft(final CharSequence self, final Number numberOfChars) {
        return padLeft(self, numberOfChars, " ");
    }

    /**
     * Pads a CharSequence to a minimum length specified by {@code numberOfChars}, adding the supplied
     * padding CharSequence as many times as needed to the left.
     *
     * If the CharSequence is already the same size or bigger than the target {@code numberOfChars}, then the
     * toString() of the original CharSequence is returned.
     *
     * @param self          a CharSequence object
     * @param numberOfChars the total minimum number of characters of the resulting CharSequence
     * @param padding       the characters used for padding
     * @return the CharSequence padded to the left as a String
     */
    public static String padLeft(final CharSequence self, final Number numberOfChars, final CharSequence padding) {
        int numChars = numberOfChars.intValue();
        if (numChars <= self.length()) {
            return self.toString();
        } else {
            return getPadding(padding.toString(), numChars - self.length()) + self;
        }
    }
    
    /**
     * Pads a CharSequence to a minimum length specified by {@code numberOfChars} by adding the space
     * character to the right as many times as needed.
     *
     * If the CharSequence is already the same size or bigger than the target {@code numberOfChars},
     * then the toString() of the original CharSequence is returned.
     *
     * @param self          a CharSequence object
     * @param numberOfChars the total minimum number of characters of the resulting string
     * @return the CharSequence padded to the right as a String
     *
     */
    public static String padRight(final CharSequence self, final Number numberOfChars) {
        return padRight(self, numberOfChars, " ");
    }

    /**
     * Pads a CharSequence to a minimum length specified by {@code numberOfChars}, adding the supplied padding
     * CharSequence as many times as needed to the right.
     *
     * If the CharSequence is already the same size or bigger than the target {@code numberOfChars},
     * then the toString() of the original CharSequence is returned.
     *
     * @param self          a CharSequence object
     * @param numberOfChars the total minimum number of characters of the resulting CharSequence
     * @param padding       the characters used for padding
     * @return the CharSequence padded to the right as a String
     *
     */
    public static String padRight(final CharSequence self, final Number numberOfChars, final CharSequence padding) {
        int numChars = numberOfChars.intValue();
        if (numChars <= self.length()) {
            return self.toString();
        } else {
            return self + getPadding(padding.toString(), numChars - self.length());
        }
    }
    
    private static String getPadding(final CharSequence padding, final int length) {
        if (padding.length() < length) {
            return multiply(padding, length / padding.length() + 1).substring(0, length);
        } else {
            return "" + padding.subSequence(0, length);
        }
    }
    
    /**
     * Repeats a CharSequence a certain number of times.
     *
     * @param self   a CharSequence to be repeated
     * @param factor the number of times the CharSequence should be repeated
     * @return a String composed of a repetition
     * @throws IllegalArgumentException if the number of repetitions is &lt; 0
     */
    public static String multiply(final CharSequence self, final Number factor) {
        int size = factor.intValue();
        if (size == 0)
            return "";
        else if (size < 0) {
            throw new IllegalArgumentException("multiply() should be called with a number of 0 or greater not: " + size);
        }
        StringBuilder answer = new StringBuilder(self);
        for (int i = 1; i < size; i++) {
            answer.append(self);
        }
        return answer.toString();
    }
}
