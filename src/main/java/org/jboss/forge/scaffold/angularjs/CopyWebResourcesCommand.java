package org.jboss.forge.scaffold.angularjs;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.scaffoldx.util.ScaffoldUtil;
import org.jboss.forge.shell.ShellPrompt;

public class CopyWebResourcesCommand {

    @Inject
    private ShellPrompt prompt;
    
    @Inject
    private WebResourceFacet web;
    
    public List<Resource<?>> execute(List<ScaffoldResource> templates, boolean overwrite) {
        List<Resource<?>> resources = new ArrayList<Resource<?>>();
        for (ScaffoldResource template : templates) {
            resources.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(template.getDestination()), getClass()
                    .getResourceAsStream(template.getSource()), overwrite));
        }
        return resources;
    }

}
