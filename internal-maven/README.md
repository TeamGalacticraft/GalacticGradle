# `net.galacticraft.internal.maven`

### Please Note

This plugin is meant for TeamGalacticraft project only.

### Features

- Creates a `MavenPublication` from the name of the project (*can be overriden via extension property*)
- Adds tasks `publishToGalacticMaven`, `publishToLocalMaven`, `testPublish`  to the `galacticgradle` task group
- Automatically adds outputs from any configured tasks withType(Jar.class) to the maven publication

### Extension

- `maven`

### Properties

| **Property**    | **Default Value**       | **Type**                   | **Description**                                          |
|-----------------|-------------------------|----------------------------|----------------------------------------------------------|
| publicationName | `<project name>`        | String                     | Name used for the MavenPublication instance              |
| nexusUsername   | `GC_NEXUS_USERNAME`     | Property<String>           | Your Nexus Repository username                           |
| nexusPassword   | `GC_NEXUS_PASSWORD`     | Property<String>           | Your Nexus Repository password                           |
| artifacts       | `ListProperty.empty()`  | ListProperty<File, String> | The artifacts that are added to the publication          |

### Methods

| **Method**                | **Description**                                                                             |
|---------------------------|---------------------------------------------------------------------------------------------|
| publicationName(String)   | Will set the publicationName property with the provided String                              |
| username(String)          | Sets the value to use when searching for the nexusUsername property in your project         |
| password(String)          | Sets the value to use when searching for the nexusPassword property in your project         |
| artifacts(Object[])       | Adds the provider objects to the publication                                                |
| artifact(Object)          | Adds the provider object to the publication                                                 |
| artifact(Object, String)  | Adds the provider object to the publication with the given classifier                       |


### Special Property

| **Property**          | **Default** | **Description**                                                     |
|-----------------------|-------------|---------------------------------------------------------------------|
| `internal.maven.sign` | `true`      | If the publication should be signed                                 |

### Defaults

- **groupId**
  - The groupId is taken from the group property of your project, this is not changeable by design.

- **artifactId**
  - The artifactId is by default derived from the following methods, the first (in order from the top) to return non-null is the artifactId:
    - the `"artifactId": ""` field in the `.pomrc` file
    - project property `mod.id`
    - project property `mod_id`
    - project property `modid`
    - project name - lowercased

- **version**
  - The version is taken from the version property of your project, this is not changeable by design.

The POM can be customized with the `.pomrc` file. This method was chosen to keep the build script less cluttered. On first run an empty `.pomrc` file is created for you

## .pomrc

```json
{
  "artifactId": "",
  "name": "",
  "url": "",
  "description": "",
  "inceptionYear": "",
  "organization": {
    "name": "",
    "url": ""
  },
  "licenses": [
    {
      "name": "",
      "url": "",
      "distribution": "",
      "comments": ""
    }
  ],
  "issueManagement": {
    "system": "",
    "url": ""
  },
  "scm": {
    "connection": "",
    "developerConnection": "",
    "tag": "",
    "url": ""
  },
  "ciManagement": {
    "system": "",
    "url": ""
  },
  "developers": [
    {
      "id": "",
      "name": "",
      "email": "",
      "url": "",
      "organization": "",
      "organizationurl": "",
      "roles": [
        ""
      ],
      "timezone": ""
    }
  ],
  "contributors": [
    {
      "name": "",
      "email": "",
      "url": "",
      "organization": "",
      "organizationurl": "",
      "roles": [
        ""
      ],
      "timezone": ""
    }
  ]
}
```