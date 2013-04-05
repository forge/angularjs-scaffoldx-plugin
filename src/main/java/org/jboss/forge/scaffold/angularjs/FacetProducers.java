package org.jboss.forge.scaffold.angularjs;

import javax.enterprise.inject.Produces;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.shell.project.ProjectScoped;

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
