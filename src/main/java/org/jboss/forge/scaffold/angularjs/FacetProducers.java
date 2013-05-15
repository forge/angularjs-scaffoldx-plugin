/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs;

import javax.enterprise.inject.Produces;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.shell.project.ProjectScoped;

/**
 * A utlity class containing CDI producers for Forge project facets.
 */
public class FacetProducers {

    @Produces
    @ProjectScoped
    public MetadataFacet producesMetaData(Project project) {
        return project.getFacet(MetadataFacet.class);
    }
    
    @Produces
    @ProjectScoped
    public WebResourceFacet producesWebResource(Project project) {
        return project.getFacet(WebResourceFacet.class);
    }

}
