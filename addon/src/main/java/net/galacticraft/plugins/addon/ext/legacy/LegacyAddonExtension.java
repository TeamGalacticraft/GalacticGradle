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

package net.galacticraft.plugins.addon.ext.legacy;

import net.galacticraft.common.Constants;
import net.galacticraft.plugins.addon.ext.AddonExtension;

public class LegacyAddonExtension implements AddonExtension<LegacyAddonExtension> {
	private String gcVersion;
	private Boolean useLatestRelease = false;
	private Boolean useLatestSnapshot = false;
	
    public void gcVersion(final String version) {
    	gcVersion = version;
    }
    
    public void useLatestRelease() {
    	this.useLatestRelease = true;
    }
    
    public void useLatestSnapshot() {
    	this.useLatestSnapshot = true;
    }
    
    @Override
    public String getGalacticraftVersion() {
    	return this.gcVersion;
    }
    
    public boolean isGcVersionSet() {
    	return this.gcVersion != null;
    }

	public Boolean getUseLatestRelease() {
		return this.useLatestRelease;
	}

	public Boolean getUseLatestSnapshot() {
		return this.useLatestSnapshot;
	}

	@Override
	public LegacyAddonExtension get() {
		return this;
	}

	@Override
	public String getDependency() {
		return Constants.Dependencies.GC_LEGACY + ":" + getGalacticraftVersion();
	}
}
