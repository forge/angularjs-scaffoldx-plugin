package org.jboss.forge.scaffold.angularjs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.resources.Resource;
import org.jboss.forge.shell.project.ProjectScoped;

@ProjectScoped
public class ResourceRegistry {

    private List<Resource<?>> resources;

    @Inject
    public ResourceRegistry() {
        this.resources = new ArrayList<Resource<?>>();
    }

    public List<? extends Resource<?>> getCreatedResources() {
        return resources;
    }

    public void add(Resource<?> resource) {
        resources.add(resource);
    }

    public void addAll(Collection<Resource<?>> collection) {
        resources.addAll(collection);
    }

    public void clear() {
        resources.clear();
    }

}
