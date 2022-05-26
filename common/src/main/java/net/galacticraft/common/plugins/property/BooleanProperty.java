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

package net.galacticraft.common.plugins.property;

import javax.inject.Inject;

import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.provider.Property;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BooleanProperty {

	private boolean propValue;
	private boolean isSet;
	
	public BooleanProperty(Property<Boolean> property) {
		if(!property.isPresent()) {
			throw new GradleException("Boolean Property cannot be null");
		}
		this.propValue = property.get().booleanValue();
	}
	
	@Inject
	public BooleanProperty(Project project, String propertyName) {
		Object prop = project.findProperty(propertyName);
		if(prop != null) {
			this.propValue = Boolean.parseBoolean((String) prop);
			this.isSet = true;
		}
	}
	
	public boolean isSet() {
		return isSet;
	}
	
	public boolean isTrue() {
		return propValue == true;
	}
	
	public boolean isFalse() {
		return propValue == false;
	}
	
	public boolean value() {
		return propValue;
	}
}
