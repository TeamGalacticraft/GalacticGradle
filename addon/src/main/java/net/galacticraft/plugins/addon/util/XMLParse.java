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

package net.galacticraft.plugins.addon.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;

public class XMLParse {

	public static String readLatestVersion(XMLUrl xml) {
		String latestVersion;
		try {
		    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    Document document = builder.parse(xml.url);

		    XPath xPath = XPathFactory.newInstance().newXPath();
		    String expression = "/metadata/versioning/latest";
		    latestVersion = xPath.compile(expression).evaluate(document);
		} catch (Exception e) {
			latestVersion = null;
		}
	    return latestVersion;
	}
	
	public static String readLatestRelease(XMLUrl xml) {
		String latestRelease;
		try {
		    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    Document document = builder.parse(xml.url);

		    XPath xPath = XPathFactory.newInstance().newXPath();
		    String expression = "/metadata/versioning/release";
		    latestRelease = xPath.compile(expression).evaluate(document);
		} catch (Exception e) {
			latestRelease = null;
		}
	    return latestRelease;
	}
	
	public static String[] readVersions(XMLUrl xml) {
		String latestRelease;
		try {
		    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = builderFactory.newDocumentBuilder();
		    Document document = builder.parse(xml.url);

		    XPath xPath = XPathFactory.newInstance().newXPath();
		    String expression = "/metadata/versioning/versions";
		    latestRelease = xPath.compile(expression).evaluate(document);
		} catch (Exception e) {
			latestRelease = null;
		}
		if(latestRelease != null) {
			return latestRelease.trim().split("\n");
		}
	    return new String[] {};
	}
}
