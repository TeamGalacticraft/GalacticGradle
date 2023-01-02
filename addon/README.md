<h1 align="center">GalacticGradle Plugin Suite: Addon</h1>

<a href="https://plugins.gradle.org/plugin/net.galacticraft.addon"><img src="https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.addon?style=plastic" alt="Gradle Plugin Portal"/></a>

```gradle
plugins {
    // Gradle
    id 'net.galacticraft.addon' version '1.0.8'

    // Kotlin
    id("net.galacticraft.addon") version "1.0.8"
}
```
> This plugin is only for-use on Addons targeting Galacticraft-Legacy at this time

### `Features`

- Plugin can be applied to both `build.gradle` and `settings.gradle`
  - If applied to `settings.gradle`, any required `pluginManagement.repositories` will be applied
- Adds the Galacticraft-Legacy dependency version provided
- Dynamically imports the correct jar depending on Addons mapping channel and version
- Can apply ForgeGradle5 to project, cleaning up more of the buildscript
- Applies the FancyGradle plugin and patches, required for 1.12.2 mods running against ForgeGradle5

### `Special Properties`
| Property                  | Description                                                                                                       | Default       |
|---------------------------|-------------------------------------------------------------------------------------------------|---------------|
| galactic.addon.debug      | if set to true, prints out debug information to the projects lifecycle log for debug purposes   | false         |
| galactic.forgegradle      | if set to true, ForgeGradle is applied to the project                                           | false         |
| galactic.fancygradle      | if set to false, FancyGradle is not applied and no patches are configured                       | true          |
| galactic.dependencies     | if set to false, No compile dependencies for GC-Legacy are applied (may cause issues)           | true          |

### `Properties`
| Property         | Required | Description                                                                                   |
|------------------|----------|-----------------------------------------------------------------------------------------------|
| version            |   `false`    | the version of Galacticraft-Legacy to import and apply to dependencies                        |

### `Methods`
| Property               | Description                                                                |
|------------------------|----------------------------------------------------------------------------|
| version(String)              | Sets the `Galacticraft-Legacy` version Property                                                                        |
| latestSnapshot()       | Finds the latest Snapshot version and sets this as the `version` Property  |
| latestRelease()        | Finds the latest Release version and sets this as the `version` Property     |

---

## Tasks

#### `listAvailableVersions`

### Information
```
The plguin is designed to not fail if no version is set but will print out an error to the console. At most, one
of the above methods should be used to set a version to search and import.
```

### Full Example

```gradle
galacticraft {
  // One must be used, each one is check in the order
  // listed below until the version property is set
  version('4.0.3')
  //latestSnapshot()
  //latestRelease()
}
```