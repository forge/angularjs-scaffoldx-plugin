package org.jboss.forge.scaffold.angularjs;

import java.util.List;
import java.util.Map;

public class ProcessWithFreemarkerEvent {

    private List<ScaffoldResource> resources;
    private Map<String, Object> root;
    private boolean overwrite;

    public ProcessWithFreemarkerEvent(List<ScaffoldResource> resources, Map<String, Object> root, boolean overwrite) {
        this.resources = resources;
        this.root = root;
        this.overwrite = overwrite;
    }

    public List<ScaffoldResource> getResources() {
        return resources;
    }

    public Map<String, Object> getRoot() {
        return root;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

}
