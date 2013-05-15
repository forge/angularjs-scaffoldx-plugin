/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs;

import java.util.List;

import javax.enterprise.event.Event;

/**
 * A CDI {@link Event} that is fired whenever {@link ScaffoldResource}s are to be copied to certain locations, at various points
 * in the scaffold generation lifecycle.
 * 
 */
public class CopyWebResourcesEvent {

    private List<ScaffoldResource> resources;
    private boolean overwrite;

    /**
     * 
     * @param resources The list of static resources that are to be copied, represented as {@link ScaffoldResource}s. The source
     *        and destinations are provided by the {@link ScaffoldResource}s.
     * @param overwrite A flag indicating whether existing resources can be overwritten during copying. 
     */
    public CopyWebResourcesEvent(List<ScaffoldResource> resources, boolean overwrite) {
        this.resources = resources;
        this.overwrite = overwrite;
    }

    /**
     * 
     * @return A list of static resources that are to be copied, represented as {@link ScaffoldResource}s. The source and
     *         destinations are provided by the {@link ScaffoldResource}s.
     */
    public List<ScaffoldResource> getResources() {
        return resources;
    }

    /**
     * 
     * @return A flag indicating whether existing resources can be overwritten during copying.
     */
    public boolean isOverwrite() {
        return overwrite;
    }

}
