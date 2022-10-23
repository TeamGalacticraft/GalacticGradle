# `net.galacticraft.addon`

![Gradle Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/net.galacticraft.addon?style=plastic)

### Note
 - This plugin is only for-use on Addons targeting Galacticraft-Legacy at this time

### Features
- Plugin can be applied to both `build.gradle` and `settings.gradle`
  - If applied to `settings.gradle`, any required `pluginManagement.repositories` will be applied
- Adds the Galacticraft-Legacy dependency version provided
- Dynamically imports the correct jar depending on Addons mapping channel and version
- Can apply ForgeGradle5 to project, cleaning up more of the buildscript
- Applies the FancyGradle plugin and patches, required for 1.12.2 mods running against ForgeGradle5

### Extension

- `galacticraft`

### Special Property

| Property                  | Description                                                                  							            | Default       |
|---------------------------|-------------------------------------------------------------------------------------------------------|---------------|
| galactic.addon.debug      | if set to true, prints out debug information to the projects lifecycle log for debug purposes         | false         |
| galactic.fg5              | if set to false, ForgeGradle is not applied to the project                                            | true          |
| galactic.fancygradle      | if set to false, FancyGradle is not applied and no patches are configured                             | true          |
| galactic.dependencies     | if set to false, No compile dependencies for GC-Legacy are applied (may cause issues)                 | true          |

### Properties

| Property         | Required | Description                                                                                   |
|------------------|----------|-----------------------------------------------------------------------------------------------|
| version     		 |	`false`	| the version of Galacticraft-Legacy to import and apply to dependencies                        |

### Methods

| Property               | Description                                                                        |
|------------------------|------------------------------------------------------------------------------------|
| version(String)			   | Sets the `Galacticraft-Legacy` version Property     															  |
| latestSnapshot()       | Finds the latest Snapshot version and sets this as the `version` Property  		    |
| latestRelease()        | Finds the latest Release version and sets this as the `version` Property  		      |
| useMavenLocal()		 | (OPTIONAL) Pulls the given galacticraft-legacy dependency from MavenLocal		  |

### Information
```
The plguin is designed to not fail if no version is set but will print out an error to the console. At most, one
of the above methods should be used to set a version to search and import.
```

### Full Example

```gradle
galacticraft {
  // Optional
  useMavenLocal()
  
  // One must be used, each one is check in the order
  // listed below until the version property is set
  version('4.0.3')
  //latestSnapshot()
  //latestRelease()
}
```