# GalacticGradle

Plugin Suite providing:
- Default publishing for TeamGalacticraft Projects
- Galacticraft Addon plugin for addon developers
- Optional mod upload plugins for CurseForge and Modrinth

## Plugins
Click the Plugins name to view the plugins README

Replace `x.y.z` in the snippet below with the version you want to use.

### [Conventions Plugin](conventions/README.md)
> 

<details>
<summary>Usage</summary>
<br>
 
*Gradle*
```gradle
plugins {
    id "net.galacticraft.gradle.dev" version "x.y.z"
}
```
*Kotlin*
 ```kotlin
plugins {
    id("net.galacticraft.gradle.dev") version "x.y.z"
}
```
 
</details>

---

### [Addon Plugin](addon/README.md)

<details>
<summary>Usage</summary>
<br>
 
*Gradle*
```gradle
plugins {
    id "net.galacticraft.gradle.addon" version "x.y.z"
}
```
*Kotlin*
 ```kotlin
plugins {
    id("net.galacticraft.gradle.addon") version "x.y.z"
}
```
 
</details>

---

### [Modrinth Publishing Plugin](modrinth/README.md)

<details>
<summary>Usage</summary>
<br>
 
*Gradle*
```gradle
plugins {
    id "net.galacticraft.gradle.publishing.modrinth" version "x.y.z"
}
```
*Kotlin*
 ```kotlin
plugins {
    id("net.galacticraft.gradle.publishing.modrinth") version "x.y.z"
}
```
 
</details>

---

### [Curseforge Publishing Plugin](curseforge/README.md)

<details>
<summary>Usage</summary>
<br>
 
*Gradle*
```gradle
plugins {
    id "net.galacticraft.gradle.publishing.curseforge" version "x.y.z"
}
```
*Kotlin*
 ```kotlin
plugins {
    id("net.galacticraft.gradle.publishing.curseforge") version "x.y.z"
}
```
 
</details>

---

### [Mod Publishing Plugin](publishing/README.md)

<details>
<summary>Usage</summary>
<br>
 
*Gradle*
```gradle
plugins {
    id "net.galacticraft.gradle.publishing" version "x.y.z"
}
```
*Kotlin*
 ```kotlin
plugins {
    id("net.galacticraft.gradle.publishing") version "x.y.z"
}
```
 
</details>
