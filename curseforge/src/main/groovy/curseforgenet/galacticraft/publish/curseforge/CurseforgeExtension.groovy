package curseforgenet.galacticraft.publish.curseforge

import org.gradle.api.provider.Property

class CurseforgeExtension
{
	final Property<String> apiKey, projectId, changelog, changelogType, releaseType, displayName;
}
