# `net.galacticraft.gradle.publishing.modrinth`

![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.gradle.publishing.modrinth?style=plastic)

```gradle
plugins {
  id "net.galacticraft.gradle.publishing.modrinth" version "1.0.3"
}
```

### Features

- Adds task `publishToModrinth` to the `galactic-gradle` task group
- Automatically applies certain settings based on project variables (see below)

### Important Notice

Please take note that this plugin does not support uploading child-artifacts of the main upload file. The reason is it is assumed that the conventional additional files that are normally uplaoded are files a normal user would need or should have and should only be obtainable via a maven repository. This should help mitigate issues where users report invalid crashes when using said files.

### Requirements

The plugin is not configured to set the ApiKey in your buildscript. This is to help mitigate the possibility of accidently uploading your key to your repository and exposing it.

The API Key for Modrinth can only be set by either 1 of 2 methods and must be named `MODRINTH_TOKEN`:

`MODRINTH_TOKEN=<your-token>`

- As a Environment Variable that can be resolved by `System.env("MODRINTH_TOKEN")`
- As a Project Property that can be resolved by `project.findProperty("MODRINTH_TOKEN")`

### Extension

- `modrinth`

### Special Property

| Property                    | Description                                                                  							|
|---------------------------- |-----------------------------------------------------------------------------------------------------	|
| debug()                     | Sets the publish Task to output the POST request to the console and not upload the file, for testing 	|

### Properties

| Property                   	| Description                                                                                          	|
|----------------------------	|------------------------------------------------------------------------------------------------------	|
| projectId(String)  	        | The ID of the project to upload to                                                                 	|
| file(Object)  	        	| The file to upload. Can be an actual file or a file task                                              |
| version(String)		  	    | The version number of the version                                                                 	|
| versionName(String)  	    	| The name of the version                                                                 				|
| versionType(String)        	| oneOf ['release', 'beta', 'alpha']                                                                   	|
| changelog(String/File)     	| Changelog can be an entire string or the relative path of a file within your project                 	|
| gameVersions(String[])     	| Array of game versions the uploadFile is compatible with                                             	|
| loaders(String[])     		| Can be anyOf ['fabic', 'forge', 'quilt']                                                             	|
| dependencies 					| See [Dependencies](#dependencies)                                              						|

### Dependencies

| Property                   	| Description                                                                                          	|
|----------------------------	|------------------------------------------------------------------------------------------------------	|
| required(String) 	          	| Specify the provided project-slug as a required project                                              	|
| optional(String) 	          	| Specify the provided project-slug as a optional project                                              	|
| incompatible(String)       	| Specify the provided project-slug as incompatible with the uploadFile                                	|

### Properties With Defaults

The following properties will be given default values based on information obtained from the project this plugin is applied too. They can be overridden simply by specifying them in the `modrinth` block. 

| Property      	| Default Value                                                                                              	|
|---------------	|-----------------------------------------------------------------------------------------------------			|
| file				| Defaults to the file produced from the `jar` task															|
| version		   	| Defaults to the `version` declaration of your project														|
| versionName   	| Defaults to `versionNumber`																				|
| versionType   	| Defaults to `release` unless project version contains `alpha` or `beta`									|
| changelog     	| If a file named `changelog.<ext>` is in your projects root directory The contents are used by default		|
| gameVersions  	| Game versions are determined by the modloaders gradle plugins if they are applied to the project				|
| loaders  			| the modloader of whatever modloaders gradle plugin is applied to the project									|

### Full Example

```gradle
modrinth {
	debug()
	projectId('AABBCCDD')
	file(customJar)
	changelog('anotherChangelogFile.md') // or changelog('Some Changes Made')
	version('1.0.0')
	versionName('FirstRelease')
	gameVersions('1.12.2')
	loaders('Fabric')
	dependencies {
		optional("AABBCCXX") {
			version("1.0.0")
		}
		incompatible("AABBCCZZ") {
			version("1.0.0")
		}
		required("AABBCCYY") {
			version("1.0.0")
		}
	}
}
```