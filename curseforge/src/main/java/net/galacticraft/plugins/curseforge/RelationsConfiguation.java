package net.galacticraft.plugins.curseforge;

import javax.inject.Inject;

import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.model.ObjectFactory;

import net.galacticraft.plugins.curseforge.curse.RelationContainer;
import net.galacticraft.plugins.curseforge.curse.RelationType;

public class RelationsConfiguation  {
	private final NamedDomainObjectContainer<RelationContainer> dependencies;

    @Inject
    public RelationsConfiguation(final ObjectFactory objects) {
    	this.dependencies = objects.domainObjectContainer(RelationContainer.class);
    }
    
    public NamedDomainObjectContainer<RelationContainer> getDependencies() {
        return this.dependencies;
    }
    
    public void required(String slugIn) {
    	this.dependencies.register(slugIn, set -> {
        	set.setType(RelationType.REQUIRED);
        });
    }
    
    public void embedded(String slugIn) {
    	this.dependencies.register(slugIn, set -> {
        	set.setType(RelationType.EMBEDEDLIB);
        });
    }

    public void optional(String slugIn) {
    	this.dependencies.register(slugIn, set -> {
        	set.setType(RelationType.OPTIONAL);
        });
    }
    
    public void tool(String slugIn) {
    	this.dependencies.register(slugIn, set -> {
        	set.setType(RelationType.TOOL);
        });
    }
    
    public void incompatible(String slugIn) {
    	this.dependencies.register(slugIn, set -> {
        	set.setType(RelationType.INCOMPATIBLE);
        });
    }
}
