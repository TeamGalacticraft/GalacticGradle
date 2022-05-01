# `net.galacticraft.gradle.publishing.curseforge`

![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.gradle.publishing.curseforge?style=plastic)

```gradle
plugins {
  id "net.galacticraft.gradle.publishing.curseforge" version "1.0.3"
}
```

### Features

- Adds task `publishToCurseforge` to the `galactic-gradle` task group
- Automatically applies certain settings based on project variables (see below)


### Important Notice

Please take note that this plugin does not support uploading child-artifacts of the main upload file. The reason is it is assumed that the conventional additional files that are normally uplaoded to curseforge are files a normal user would need or should have and should only be obtainable via a maven repository. This should help mitigate issues where users report invalid crashes when using said files.

### Requirements

The plugin is not configured to set the ApiKey in your buildscript. This is to help mitigate the possibility of accidently uploading your key to your repository and exposing it.

The API Key for CurseForge can only be set by either 1 of 2 methods and must be named `CURSE_TOKEN`:

`CURSE_TOKEN=<your-token>`

- As a Environment Variable that can be resolved by `System.env("CURSE_TOKEN")`
- As a Project Property that can be resolved by `project.findProperty("CURSE_TOKEN")`

### Extension

- `curseforge`

### Special Property

| Property                    | Description                                                                  							|
|---------------------------- |-----------------------------------------------------------------------------------------------------	|
| debug()                     | Sets the publish Task to output the POST request to the console and not upload the file, for testing 	|

### Properties

| Property                    | Required 	| Description                                                                                          	|
|---------------------------- |------------ |-------------------------------------------------------------------------------------------------------|
| projectId(String/Integer)   |	`TRUE`	 	| Can be a valid project as a String or Integer                                                        	|
| displayName(String)  		  |	`FALSE`	| An optional display name that will visually replace the file name                                     |
| releaseType(String)         |	`FALSE`	| oneOf ['release', 'beta', 'alpha']                                                                   	|
| changelog(String/File)      |	`FALSE`	| Changelog can be an entire string or the relative path of a file within your project                 	|
| changelogType(String)       |	`FALSE`	| oneOf ['text', 'html', 'markdown']                                                                   	|
| gameVersions(String[])      |	`FALSE`	| Array of game versions the uploadFile is compatible with                                             	|
| file(Object)                |	`FALSE`	| Sets the uploadFile to the file or file task provided (With `displayName` as `null`)				|
| file(Object, String)        |	`FALSE`	| Sets the uploadFile to the file or file task provided (With `displayName` as provided)				|
| dependencies 				  |	`FALSE`	| See [Dependencies](#dependencies)                                              						|

### Dependencies

| Property                   	| Description                                                                                          	|
|----------------------------	|------------------------------------------------------------------------------------------------------	|
| required(String) 	          	| Specify the provided project-slug as a required project                                              	|
| embedded(String)    	      	| Specify the provided project-slug as embedded in the file		                                      	|
| optional(String) 	          	| Specify the provided project-slug as a optional project                                              	|
| tool(String)               	| Specify the provided project-slug as a tool                                                          	|
| incompatible(String)       	| Specify the provided project-slug as incompatible			                                 			|

### Properties With Defaults

The following properties will be given default values based on information obtained from the project this plugin is applied too. They can be overridden simply by specifying them in the `curseforge` block. 

| Property      	| Default Value                                                                                                                                               	|
|---------------	|-------------------------------------------------------------------------------------------------------------	|
| file   			| Defaults to the file produced from the `jar` task                                                           	|
| displayName   	| Defaults to `null`                                                                                  			|
| releaseType   	| Defaults to `release` unless project version contains `alpha` or `beta`.                        			|
| changelog     	| If a file named `changelog.<ext>` is in your projects root directory. The contents are used by default    	|
| changelogType     | If a file is provided for `changelog` attempts to use its ext, defaults to `text`    						|
| gameVersions  	| Attempts to determine via environments using ForgeGradle and Fabric Loom. 									|

### Full Example

```gradle
curseforge {
	debug()
	file(customJar) // or file(customJar, 'CustomName')
	projectId('123456')// or projectId(123456)
	releaseType('release')
	gameVersions('1.12', '1.12.2')
	changelog('Changed Some Things') // or changelog(changlogs/CHANGELOG.md)
	changelogType('text')
	dependencies {
		optional('optional-mod')
		required('required-mod')
		embedded('embedded-mod')
		tool('tool-mod')
		incompatible('incompatible-mod')
	}
}
```