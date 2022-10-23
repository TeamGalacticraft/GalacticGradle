# `net.galacticraft.internal.maven`

### Please Note

This plugin is meant for TeamGalacticraft project only.

### Features

- Creates a `MavenPublication` from the name of the project (*can be overriden via extension property*)
- Adds tasks `publishToGalacticMaven`, `publishToLocalMaven`, `testPublish`  to the `galacticgradle` task group
- Automatically adds outputs from any configured tasks withType(Jar.class) to the maven publication

### Extension

- `configureInternal`

### Properties

| **Property**    | **Default Value**       | **Required**    | **Description**                                          |
|-----------------|-------------------------|-----------------|----------------------------------------------------------|
| publicationName | `Project Name`          | No              | The name used for creating the MavenPublication instance |
| nexusUsername   | `teamgc.nexus.user`     | No              | Your NexusRepository user name                           |
| nexusPassword   | `teamgc.nexus.password` | No              | Your NexusRepository password                            |

### Methods

| **Method**              | **Description**                                                                             |
|-------------------------|---------------------------------------------------------------------------------------------|
| publicationName(String) | Will set the publicationName property with the provided String                              |
| nexusUser(String)       | Sets the value to use when searching for the nexusUsername property in your project         |
| nexusPassword(String)   | Sets the value to use when searching for the nexusPassword property in your project         |


### Special Property

| **Property**          | **Default** | **Description**                                                     | 
|-----------------------|-------------|---------------------------------------------------------------------|
| `internal.maven.sign` | `true`      | If the publication should be signed                                 |

### Defaults

- **groupId**
  - The groupId is taken from the group property of your project, this is not changeable by design.
  
- **artifactId**
  - The artifactId is by default derived from the following methods, the first one to return is the artifactId, in order from the top:
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