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

package net.galacticraft.plugins.modrinth.model.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum LoaderType {
	FORGE("forge"), FABRIC("fabric"), QUILT("quilt");

	private final String value;
	private final static Map<String, LoaderType> CONSTANTS = new HashMap<String, LoaderType>();
	public final static List<LoaderType> DEFAULT = new ArrayList<>(Arrays.asList(FORGE));

	static {
		for (LoaderType c : values()) {
			CONSTANTS.put(c.value, c);
		}
	}

	LoaderType(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public String value() {
		return this.value;
	}

	public static LoaderType fromValue(String value) {
		LoaderType constant = CONSTANTS.get(value);
		if (constant == null) {
			throw new IllegalArgumentException(value);
		} else {
			return constant;
		}
	}
	
	public static List<String> toStrings(List<LoaderType> loaderTypes) {
		List<String> strings = new ArrayList<>();
		for(LoaderType types : loaderTypes) {
			strings.add(types.value);
		}
		return strings;
	}
}
