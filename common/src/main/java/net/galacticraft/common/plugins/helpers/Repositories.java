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

package net.galacticraft.common.plugins.helpers;

import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.RepositoryHandler;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository.MetadataSources;

public class Repositories {
	
	private final RepositoryHandler repositoryHandler;

	public Repositories(Project project) {
		this.repositoryHandler = project.getRepositories();
	}
	
	/**
	 * Adds a new maven Repository to the project
	 *
	 * @param name the name
	 * @param url the url
	 * @return the maven artifact repository
	 */
	public MavenArtifactRepository addMaven(String name, String url) {
		return this.repositoryHandler.maven(maven -> {
			maven.setName(name);
			maven.setUrl(url);
			maven.metadataSources(Metadata.ALL.set());
		});
	}
	
	/**
	 * Adds a new maven Repository to the project
	 *
	 * @param url the url
	 * @return the maven artifact repository
	 */
	public MavenArtifactRepository addMaven(String url) {
		return this.repositoryHandler.maven(maven -> {
			maven.setUrl(url);
			maven.metadataSources(Metadata.ALL.set());
		});
	}
	
	/**
	 * Adds a new maven Repository to the project
	 *
	 * @param name the name
	 * @param url the url
	 * @param sources the sources
	 * @return the maven artifact repository
	 */
	public MavenArtifactRepository addMaven(String name, String url, Metadata... sources) {
		return this.repositoryHandler.maven(maven -> {
			maven.setName(name);
			maven.setUrl(url);
			maven.metadataSources(Metadata.ALL.set());
		});
	}
	
	/**
	 * Adds a new maven Repository to the project
	 *
	 * @param url the url
	 * @param sources the sources
	 * @return the maven artifact repository
	 */
	public MavenArtifactRepository addMaven(String url, Metadata... sources) {
		return this.repositoryHandler.maven(maven -> {
			maven.setUrl(url);
			for(Metadata metadata : sources) {
				maven.metadataSources(metadata.set());
			}
		});
	}

	public static enum Metadata {

		GRADLE {
			@Override
			public Action<MetadataSources> set() {
				return MetadataSources::gradleMetadata;
			}
		},

		POM() {
			@Override
			public Action<MetadataSources> set() {
				return MetadataSources::mavenPom;
			}
		},

		ARTIFACT() {
			@Override
			public Action<MetadataSources> set() {
				return MetadataSources::artifact;
			}
		},

		ALL {
			@Override
			public Action<MetadataSources> set() {
				return new Action<MetadataSources>() {
					@Override
					public void execute(MetadataSources metadataSources) {
						metadataSources.gradleMetadata();
						metadataSources.mavenPom();
						metadataSources.artifact();
					}
				};
			}
		};

		public abstract Action<MetadataSources> set();
	}
}
