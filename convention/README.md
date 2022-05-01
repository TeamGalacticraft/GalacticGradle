# `net.galacticraft.gradle.dev`

Conventional configurations for TeamGalacticraft projects.

- Applies the `net.kyori.indra` plugin
- Configures publication to Galacticraft repository
- Configures standard location for license headers
- configures code signing to sign artifacts with TeamGalacticraft's key (not fully implemented)
- Adds a 'sign jar' task to distribute signed jars

## Publishing

Publishing contains Properties that have default values applied to them if not defined in the buildscript. And Properties that must be defined and do not have defaults

## Properties w/ Defaults

Property                | Description          | Default
----------------------- | ------------         | -------------
`galacticSnapshotRepo`    | The repository URL to publish snapshots to | `https://repo.galacticraft.net/repository/maven-snapshots/`
`galacticReleaseRepo`     | The repository URL to publish releases to  | `https://repo.galacticraft.net/repository/maven-releases/`