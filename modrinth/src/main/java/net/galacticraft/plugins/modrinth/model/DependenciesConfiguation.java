package net.galacticraft.plugins.modrinth.model;

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.NamedDomainObjectContainer;
import org.gradle.api.model.ObjectFactory;

import net.galacticraft.plugins.modrinth.model.api.ModDependency;
import net.galacticraft.plugins.modrinth.model.dependency.DependencyContainer;

public class DependenciesConfiguation  {
	private final NamedDomainObjectContainer<DependencyContainer> dependencies;

    @Inject
    public DependenciesConfiguation(final ObjectFactory objects) {
    	this.dependencies = objects.domainObjectContainer(DependencyContainer.class);
    }
    
    public NamedDomainObjectContainer<DependencyContainer> getDependencies() {
        return this.dependencies;
    }
    
    public void incompatible(final String name, final Action<? super ModDependency> action) {
    	this.dependencies.register(name, action).configure(d -> {
    		d.incompatible();
    	});;
    }
    
    public void required(final String name, final Action<? super ModDependency> action) {
    	this.dependencies.register(name, action).configure(d -> {
    		d.required();
    	});;
    }
    
    public void optional(final String name, final Action<? super ModDependency> action) {
    	this.dependencies.register(name, action).configure(d -> {
    		d.optional();
    	});;
    }
}
