package org.jboss.forge.scaffold.angularjs;

import java.util.List;

public class CopyWebResourcesEvent {

    private List<ScaffoldResource> resources;
    private boolean overwrite;

    public CopyWebResourcesEvent(List<ScaffoldResource> resources, boolean overwrite) {
        this.resources = resources;
        this.overwrite = overwrite;
    }

    public List<ScaffoldResource> getResources() {
        return resources;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

}
