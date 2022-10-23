package curseforgenet.galacticraft.publish.curseforge.impl

import org.gradle.api.Named
import org.gradle.api.provider.Property

class RelationContainer implements Named
{
	
	private final String name
	private final Property<String> type

	@Override
	public String getName()
	{
		this.name
	}
}
