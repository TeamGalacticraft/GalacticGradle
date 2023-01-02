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

package net.galacticraft.gradle.core.xml.model;

import java.util.ArrayList;
import java.util.List;

public class ReportSet extends ConfigurationContainer implements Cloneable
{
	private String id = "default";

	private List<String> reports;

	public void addReport(String string)
	{
		getReports().add(string);
	}

	public ReportSet clone()
	{
		try
		{
			ReportSet copy = (ReportSet) super.clone();
			if (this.reports != null)
			{
				copy.reports = new ArrayList<>();
				copy.reports.addAll(this.reports);
			}
			return copy;
		} catch (Exception ex)
		{
			throw (RuntimeException) (new UnsupportedOperationException(getClass().getName() + " does not support clone()")).initCause(ex);
		}
	}

	public String getId()
	{
		return this.id;
	}

	public List<String> getReports()
	{
		if (this.reports == null)
			this.reports = new ArrayList<>();
		return this.reports;
	}

	public void removeReport(String string)
	{
		getReports().remove(string);
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setReports(List<String> reports)
	{
		this.reports = reports;
	}

	public String toString()
	{
		return getId();
	}
}
