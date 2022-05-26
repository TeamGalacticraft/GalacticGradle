# `net.galacticraft.gradle.publishing.nexus`

![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.gradle.publishing.nexus?style=plastic)

### Please Note

This plugin is only meant to be used by certain projects and is not a universal plugin. Please do not add this plugin to your project unless directed.

### Features

- Adds task `publishMod` to the `GalacticGradle` task group
- Automatically applies certain settings based on project variables (see below)

### Extension

- `nexus`

### Special Property

| Property                    | Description                                                                  							|
|---------------------------- |-----------------------------------------------------------------------------------------------------	|
| nexus.debug                 | Adding this to your Projects properties file will cause the publish task to print the outputs to console for testing 	|

### Properties

| Property                    | Required 	| Description                                                                                          	|
|---------------------------- |------------ |-------------------------------------------------------------------------------------------------------|
| nexusUsername			      |	`TRUE`	 	| The username provided to you. Best placed in `gradle.properties` in your `.gradle` folder     |
| nexusPassword  		      |	`TRUE`  	| The username provided to you. Best placed in `gradle.properties` in your `.gradle` folder     |
| group                       |	`FALSE`	| group name used in the published mod (see [Defaults](#defaults))                                      |
| artifactId                  |	`FALSE`	| artifact name used in the published mod (see [Defaults](#defaults))                                   |
| version              		  |	`FALSE`	| version name used in the published mod (see [Defaults](#defaults))                                    |
| archives      			  |	`FALSE`	| files list that are added as artifacts to the publication (see [Defaults](#defaults))                 |
| pom                         |	`FALSE`	| additional information to be added to the generated pom file                                          |


### Methods

| Property                    | Description                                                                                           |
|---------------------------- |-------------------------------------------------------------------------------------------------------|
| username(String)			  | Sets the `nexusUsername` property     															  |
| password(String)  		  | Sets the `nexusPassword` property     															  |
| group(String)               | Sets the `group` property                                        		 					  		  |
| artifactId(String)          | Sets the `artifactId` property                                        	  					  	  |
| version(String)             | Sets the `version` property                                        		  				 		  |
| archives(Object...)      	  | Adds the provided objects to the publication (must be resolvable files or Tasks)                      |
| pom(Closure)      	      | Takes the provided maven Pom closure and is added to the published Pom file                           |

### Defaults

- **group**
  - The default group is `mod.dependency`. This is to attempt to have a more centralized convention for artifacts. You are more than welcome to change this. Please follow java naming convention for your group
  
- **artifactId**
  - The default artifactId uses the projects DisplayName with any special charcters removed
  
- **version**
  - The default version uses the projects version. Unless you really need the artifact to have different version than your project it's recommended to leave as default
  
- **archives**
  - By default the plugin will look for the archiveFile from the `jar` task as well as `sourcesJar` & `javadocJar`. If you do not want to have the additional jars published simply add the method to the `nexusd` block as so ` `archives(jar)`

### Full Example

```gradle
nexus {
	username(System.getenv('nexusUsername')
	password(System.getenv('nexusPassword')
	group('com.example.artifact')
	artifactId('ExampleArtifact')
	version('1.0.0')
	archives(jar, sourcesJar, customJar)
	pom {
		name = 'ExampleProject'
		description = 'This project does things'
		url = 'https://github.com/SomeOrganization/ExampleProject'
		organization {
			name = 'Example Organization'
			url = 'https://github.com/SomeOrganization'
		}
		scm {
			url = 'https://github.com/SomeOrganization/ExampleProject'
			connection = 'scm:git:git://github.com/SomeOrganization/ExampleProject.git'
			developerConnection = 'scm:git:git@SomeOrganization/ExampleProject.git'
		}
		issueManagement {
			system = 'github'
			url = 'https://github.com/SomeOrganization/ExampleProject/issues'
		}
		licenses {
			license {
				name = 'MIT'
				url = 'https://github.com/SomeOrganization/ExampleProject/blob/main/LICENSE'
				distribution = 'repo'
			}
		}
		developers {
			developer {
				id = "developer-person"
				name = "Some Person"
				email = "developer@example.com"
				roles = ['developer']
			}
		}
	}
}
```