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

package net.galacticraft.common;

import java.util.Objects;

public class Version implements Comparable<Version> {

	private final String originalValue;
	private final String value;
	private final Integer major;
	private final Integer minor;
	private final Integer patch;
	private final String[] suffixTokens;
	private final String build;
	
	public static Version of(String version) {
		return new Version(version);
	}

	public Version(Object obj) {
		if(!(obj instanceof String)) {
			throw new VersionException("Version argument must be a String value");
		}
		String value = obj.toString();
		
		this.originalValue = value;
		value = value.trim();

		this.value = value;
		String[] tokens;

		if (hasPreRelease(value)) {
			tokens = value.split("-", 2);
		} else {
			tokens = new String[] { value };
		}

		String build = null;
		Integer minor = null;
		Integer patch = null;
		try {
			String[] mainTokens;
			if (tokens.length == 1) {
				// The build version may be in the main tokens
				if (tokens[0].endsWith("+")) {
					throw new VersionException("The build cannot be empty.");
				}
				String[] tmp = tokens[0].split("\\+");
				mainTokens = tmp[0].split("\\.");
				if (tmp.length == 2) {
					build = tmp[1];
				}
			} else {
				mainTokens = tokens[0].split("\\.");
			}

			try {
				this.major = Integer.valueOf(mainTokens[0]);
			} catch (NumberFormatException e) {
				throw new VersionException("Invalid version (no major version): " + value);
			} catch (IndexOutOfBoundsException e) {
				throw new VersionException("Invalid version (no major version): " + value);
			}
		} catch (NumberFormatException e) {
			throw new VersionException("The version is invalid: " + value);
		} catch (IndexOutOfBoundsException e) {
			throw new VersionException("The version is invalid: " + value);
		}
		this.minor = minor;
		this.patch = patch;

		String[] suffix = new String[0];
		try {
			// The build version may be in the suffix tokens
			if (tokens[1].endsWith("+")) {
				throw new VersionException("The build cannot be empty.");
			}
			String[] tmp = tokens[1].split("\\+");
			if (tmp.length == 2) {
				suffix = tmp[0].split("\\.");
				build = tmp[1];
			} else {
				suffix = tokens[1].split("\\.");
			}
		} catch (IndexOutOfBoundsException ignored) {
		}
		this.suffixTokens = suffix;

		this.build = build;

	}
	
	private boolean hasPreRelease(String version) {

		int firstIndexOfPlus = value.indexOf("+");
		int firstIndexOfHyphen = value.indexOf("-");

		if (firstIndexOfHyphen == -1) {
			return false;
		}

		return firstIndexOfPlus == -1 || firstIndexOfHyphen < firstIndexOfPlus;
	}

	/**
	 * @see #isGreaterThan(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is greater than the provided version
	 */
	public boolean isGreaterThan(String version) {
		return this.isGreaterThan(new Version(version));
	}

	/**
	 * Checks if the version is greater than another version
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is greater than the provided version
	 */
	public boolean isGreaterThan(Version version) {
		// Compare the main part
		if (this.getMajor() > version.getMajor())
			return true;
		else if (this.getMajor() < version.getMajor())
			return false;

		int otherMinor = version.getMinor() != null ? version.getMinor() : 0;
		if (this.getMinor() != null && this.getMinor() > otherMinor)
			return true;
		else if (this.getMinor() != null && this.getMinor() < otherMinor)
			return false;

		int otherPatch = version.getPatch() != null ? version.getPatch() : 0;
		if (this.getPatch() != null && this.getPatch() > otherPatch)
			return true;
		else if (this.getPatch() != null && this.getPatch() < otherPatch)
			return false;

		// Let's take a look at the suffix
		String[] tokens1 = this.getSuffixTokens();
		String[] tokens2 = version.getSuffixTokens();

		// If one of the versions has no suffix, it's greater!
		if (tokens1.length == 0 && tokens2.length > 0)
			return true;
		if (tokens2.length == 0 && tokens1.length > 0)
			return false;

		// Let's see if one of suffixes is greater than the other
		int i = 0;
		while (i < tokens1.length && i < tokens2.length) {
			int cmp;
			try {
				// Trying to resolve the suffix part with an integer
				int t1 = Integer.valueOf(tokens1[i]);
				int t2 = Integer.valueOf(tokens2[i]);
				cmp = t1 - t2;
			} catch (NumberFormatException e) {
				// Else, do a string comparison
				cmp = tokens1[i].compareToIgnoreCase(tokens2[i]);
			}
			if (cmp < 0)
				return false;
			else if (cmp > 0)
				return true;
			i++;
		}

		// If one of the versions has some remaining suffixes, it's greater
		return tokens1.length > tokens2.length;
	}

	/**
	 * @see #isGreaterThanOrEqualTo(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is greater than or equal to the provided
	 *         version
	 */
	public boolean isGreaterThanOrEqualTo(String version) {
		return this.isGreaterThanOrEqualTo(new Version(version));
	}

	/**
	 * Checks if the version is greater than or equal to another version
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is greater than or equal to the provided
	 *         version
	 */
	public boolean isGreaterThanOrEqualTo(Version version) {
		return this.isGreaterThan(version) || this.isEquivalentTo(version);
	}

	/**
	 * @see #isLowerThan(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is lower than the provided version
	 */
	public boolean isLowerThan(String version) {
		return this.isLowerThan(new Version(version));
	}

	/**
	 * Checks if the version is lower than another version
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is lower than the provided version
	 */
	public boolean isLowerThan(Version version) {
		return !this.isGreaterThan(version) && !this.isEquivalentTo(version);
	}

	/**
	 * @see #isLowerThanOrEqualTo(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is lower than or equal to the provided
	 *         version
	 */
	public boolean isLowerThanOrEqualTo(String version) {
		return this.isLowerThanOrEqualTo(new Version(version));
	}

	/**
	 * Checks if the version is lower than or equal to another version
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version is lower than or equal to the provided
	 *         version
	 */
	public boolean isLowerThanOrEqualTo(Version version) {
		return !this.isGreaterThan(version);
	}

	/**
	 * @see #isEquivalentTo(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version equals the provided version (build
	 *         excluded)
	 */
	public boolean isEquivalentTo(String version) {
		return this.isEquivalentTo(new Version(version));
	}

	/**
	 * Checks if the version equals another version, without taking the build into
	 * account.
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version equals the provided version (build
	 *         excluded)
	 */
	public boolean isEquivalentTo(Version version) {
		// Get versions without build
		Version sem1 = this.getBuild() == null ? this : new Version(this.getValue().replace("+" + this.getBuild(), ""));
		Version sem2 = version.getBuild() == null ? version
				: new Version(version.getValue().replace("+" + version.getBuild(), ""));
		// Compare those new versions
		return sem1.isEqualTo(sem2);
	}

	/**
	 * @see #isEqualTo(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version equals the provided version
	 */
	public boolean isEqualTo(String version) {
		return this.isEqualTo(new Version(version));
	}

	/**
	 * Checks if the version equals another version
	 *
	 * @param version the version to compare
	 *
	 * @return true if the current version equals the provided version
	 */
	public boolean isEqualTo(Version version) {
		return this.equals(version);
	}

	/**
	 * Determines if the current version is stable or not. Stable version have a
	 * major version number strictly positive and no suffix tokens.
	 *
	 * @return true if the current version is stable
	 */
	public boolean isStable() {
		return (this.getMajor() != null && this.getMajor() > 0)
				&& (this.getSuffixTokens() == null || this.getSuffixTokens().length == 0)
					&& (!isSnapshot());
	}
	
	public boolean isSnapshot() {
		for(String token : this.getSuffixTokens()) {
			return token.equalsIgnoreCase("snapshot");
		}
		return false;
	}

	/**
	 * @see #diff(Version)
	 *
	 * @param version the version to compare
	 *
	 * @return the greatest difference
	 */
	public GenericVersionDiff diff(String version) {
		return this.diff(new Version(version));
	}

	/**
	 * Returns the greatest difference between 2 versions. For example, if the
	 * current version is "1.2.3" and compared version is "1.3.0", the biggest
	 * difference is the 'MINOR' number.
	 *
	 * @param version the version to compare
	 *
	 * @return the greatest difference
	 */
	public GenericVersionDiff diff(Version version) {
		if (!Objects.equals(this.major, version.getMajor()))
			return GenericVersionDiff.MAJOR;
		if (!Objects.equals(this.minor, version.getMinor()))
			return GenericVersionDiff.MINOR;
		if (!Objects.equals(this.patch, version.getPatch()))
			return GenericVersionDiff.PATCH;
		if (!areSameSuffixes(version.getSuffixTokens()))
			return GenericVersionDiff.SUFFIX;
		if (!Objects.equals(this.build, version.getBuild()))
			return GenericVersionDiff.BUILD;
		return GenericVersionDiff.NONE;
	}

	private boolean areSameSuffixes(String[] suffixTokens) {
		if (this.suffixTokens == null && suffixTokens == null)
			return true;
		else if (this.suffixTokens == null || suffixTokens == null)
			return false;
		else if (this.suffixTokens.length != suffixTokens.length)
			return false;
		for (int i = 0; i < this.suffixTokens.length; i++) {
			if (!this.suffixTokens[i].equals(suffixTokens[i]))
				return false;
		}
		return true;
	}

	public Version withIncMajor() {
		return this.withIncMajor(1);
	}

	public Version withIncMajor(int increment) {
		return this.withInc(increment, 0, 0);
	}

	public Version withIncMinor() {
		return this.withIncMinor(1);
	}

	public Version withIncMinor(int increment) {
		return this.withInc(0, increment, 0);
	}

	public Version withIncPatch() {
		return this.withIncPatch(1);
	}

	public Version withIncPatch(int increment) {
		return this.withInc(0, 0, increment);
	}

	private Version withInc(int majorInc, int minorInc, int patchInc) {
		Integer minor = this.minor;
		Integer patch = this.patch;
		if (this.minor != null) {
			minor += minorInc;
		}
		if (this.patch != null) {
			patch += patchInc;
		}
		return with(this.major + majorInc, minor, patch, true, true);
	}

	public Version withClearedSuffix() {
		return with(this.major, this.minor, this.patch, false, true);
	}

	public Version withClearedBuild() {
		return with(this.major, this.minor, this.patch, true, false);
	}

	public Version withClearedSuffixAndBuild() {
		return with(this.major, this.minor, this.patch, false, false);
	}

	public Version withSuffix(String suffix) {
		return with(this.major, this.minor, this.patch, suffix.split("\\."), this.build);
	}

	public Version withBuild(String build) {
		return with(this.major, this.minor, this.patch, this.suffixTokens, build);
	}

	public Version nextMajor() {
		return with(this.major + 1, 0, 0, false, false);
	}

	public Version nextMinor() {
		return with(this.major, this.minor + 1, 0, false, false);
	}

	public Version nextPatch() {
		return with(this.major, this.minor, this.patch + 1, false, false);
	}

	private Version with(int major, Integer minor, Integer patch, boolean suffix, boolean build) {
		minor = this.minor != null ? minor : null;
		patch = this.patch != null ? patch : null;
		String buildStr = build ? this.build : null;
		String[] suffixTokens = suffix ? this.suffixTokens : null;
		return Version.create(major, minor, patch, suffixTokens, buildStr);
	}

	private Version with(int major, Integer minor, Integer patch, String[] suffixTokens, String build) {
		minor = this.minor != null ? minor : null;
		patch = this.patch != null ? patch : null;
		return Version.create(major, minor, patch, suffixTokens, build);
	}

	private static Version create(int major, Integer minor, Integer patch, String[] suffix,
			String build) {
		StringBuilder sb = new StringBuilder().append(major);
		if (minor != null) {
			sb.append(".").append(minor);
		}
		if (patch != null) {
			sb.append(".").append(patch);
		}
		if (suffix != null) {
			boolean first = true;
			for (String suffixToken : suffix) {
				if (first) {
					sb.append("-");
					first = false;
				} else {
					sb.append(".");
				}
				sb.append(suffixToken);
			}
		}
		if (build != null) {
			sb.append("+").append(build);
		}

		return new Version(sb.toString());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Version))
			return false;
		Version version = (Version) o;
		return value.equals(version.value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public int compareTo(Version version) {
		if (this.isGreaterThan(version))
			return 1;
		else if (this.isLowerThan(version))
			return -1;
		return 0;
	}

	public String string() {
		return this.getValue();
	}

	/**
	 * Get the original defaultValue as a string
	 *
	 * @return the original string passed in the constructor
	 */
	public String getOriginalValue() {
		return originalValue;
	}

	/**
	 * Returns the version as a String
	 *
	 * @return the version as a String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the major part of the version. Example: for "1.2.3" = 1
	 *
	 * @return the major part of the version
	 */
	public Integer getMajor() {
		return this.major;
	}

	/**
	 * Returns the minor part of the version. Example: for "1.2.3" = 2
	 *
	 * @return the minor part of the version
	 */
	public Integer getMinor() {
		return this.minor;
	}

	/**
	 * Returns the patch part of the version. Example: for "1.2.3" = 3
	 *
	 * @return the patch part of the version
	 */
	public Integer getPatch() {
		return this.patch;
	}

	/**
	 * Returns the suffix of the version. Example: for "1.2.3-beta.4+sha98450956" =
	 * {"beta", "4"}
	 *
	 * @return the suffix of the version
	 */
	public String[] getSuffixTokens() {
		return suffixTokens;
	}

	/**
	 * Returns the build of the version. Example: for "1.2.3-beta.4+sha98450956" =
	 * "sha98450956"
	 *
	 * @return the build of the version
	 */
	public String getBuild() {
		return build;
	}

	/**
	 * The types of diffs between two versions.
	 */
	public enum GenericVersionDiff {
		NONE, MAJOR, MINOR, PATCH, SUFFIX, BUILD
	}

	public enum McVersionDiff {
		MCVERSION, MAJORMOD, MAJORAPI, MINOR, PATCH
	}
	
	public static class NullVersion extends Version {

		public NullVersion() {
			super("0.0.0");
		}
	}

	public static class VersionException extends RuntimeException {

		private static final long serialVersionUID = 3380584011358917827L;

		public VersionException(String msg) {
			super(msg);
		}

		public VersionException(String msg, Throwable t) {
			super(msg, t);
		}
	}
}
