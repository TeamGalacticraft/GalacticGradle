# `net.galacticraft.gradle.publishing`

![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.gradle.publishing?style=plastic)

### Features

- Applies both plugins:
  - `net.galacticraft.gradle.publishing.modrinth`
  - `net.galacticraft.gradle.publishing.curseforge`
- Adds task `publishToAll` in group `galactic-gradle` that will call both `publishToCurseforge` & `publishToModrinth` tasks

### Requirements

Your project should satisfy the requirements of both Modrinth and Curseforge plugins. Refer to their Readme files for these

- [Modrinth Requirements](https://github.com/TeamGalacticraft/GalacticGradle/blob/master/modrinth/README.md#requirements)
- [Curseforge Requirements](https://github.com/TeamGalacticraft/GalacticGradle/tree/master/curseforge/README.md#requirements)

### Extension

- `modpublishing`

### Properties

| Property                    | Description                                                 |
|---------------------------- |------------------------------------------------------------	|
| debug()                     | Sets the debug property for both publishing plugins 		|
| modrinth(Closure)           | Configures the `modrinth` plugin 							|
| curseforge(Closure)         | Configures the `curseforge` plugin 						|

Essentially both of the `modrinth` and `curseforge` blocks from each plugin can be copy/pasted into the `modpublishing` block

### Full Example

```gradle
modpublishing {
	debug() // Debug can be set here or in each plugins closure
	modrinth {
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
	curseforge {
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
}

```