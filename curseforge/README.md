# `net.galacticraft.gradle.publishing.curseforge`

![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.gradle.publishing.curseforge?style=plastic)

```gradle
plugins {
  id "net.galacticraft.gradle.publishing.curseforge" version "1.0.2"
}
```

### Features

- Adds task `publishToCurseforge` to the `galactic-gradle` task group
- Automatically applies certain settings based on project variables (see below)


### Requirements

The plugin is not configured to set the ApiKey in your buildscript. This is to help mitigate the possibility of accidently uploading your key to your repository and exposing it.

The API Key for CurseForge can only be set by either 1 of 2 methods and must be named `CURSE_TOKEN`:

`CURSE_TOKEN=<your-token>`

- As a Environment Variable that can be resolved by `System.env("CURSE_TOKEN")`
- As a Project Property that can be resolved by `project.findProperty("CURSE_TOKEN")`


### Properties

| Property                   	| Description                                                                                          	|
|----------------------------	|------------------------------------------------------------------------------------------------------	|
| projectId(String/Integer)  	| Can be a valid project as a String or Integer                                                        	|
| releaseType(String)        	| oneOf ['release', 'beta', 'alpha']                                                                   	|
| changelog(String/File)     	| Changelog can be an entire string or the relative path of a file within your project                 	|
| changelogType(String)      	| oneOf ['text', 'html', 'markdown']                                                                   	|
| gameVersions(String[])     	| Array of game versions the uploadFile is compatible with                                             	|
| debug()                    	| Sets the publish Task to output the POST request to the console and not upload the file, for testing 	|
| dependencies 					      | See [Dependencies](#dependencies)                                              						            |

### Dependencies

| Property                   	| Description                                                                                          	|
|----------------------------	|------------------------------------------------------------------------------------------------------	|
| required(String) 	          | Specify the provided project-slug as a required project                                              	|
| embedded(String)    	      | Specify the provided project-slug as embedded in the uploadFile                                      	|
| optional(String) 	          | Specify the provided project-slug as a optional project                                              	|
| tool(String)               	| Specify the provided project-slug as a tool                                                          	|
| incompatible(String)       	| Specify the provided project-slug as incompatible with the uploadFile                                	|

### Properties With Defaults

The following properties will be given default values based on information obtained from the project this plugin is applied too. They can be overridden simply by specifying them in the `curseforge` block. 

| Property      	| Default Value                                                                                                                                               	|
|---------------	|-------------------------------------------------------------------------------------------------------------------------------------------------------------	|
| releaseType   	| Defaults to `release` unless project version contains `alpha` or `beta`.                                                                                    	|
| changelog     	| If a file named `changelog.<ext>` is in your projects root directory. The contents are used by default                                                      	|
| changelogType 	| Determined by the changelog's file extension                                                                                                                	|
| gameVersions  	| Game versions are determined by the modloaders gradle plugins if they are applied to the project. Modloaders are automatically determined and added as well 	|

### Full Example

```gradle
curseforge {
	debug()
	projectId("123456") // or projectId(123456)
	releaseType("release")
	gameVersions("1.12", "1.12.2")
	changelog("Changed Some Things") // or changelog(changlogs/CHANGELOG.md)
	changelogType("text")
	dependencies {
		optional("optional-mod")
		required("required-mod")
		embedded("embedded-mod")
		tool("tool-mod")
		incompatible("incompatible-mod")
	}
}
```