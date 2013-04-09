package org.jboss.forge.scaffold.angularjs;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.event.Observes;
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
    
    @Inject
    private ResourceRegistry resourceRegistry;

    public void execute(@Observes CopyWebResourcesEvent event) {
        resourceRegistry.clear();
        List<Resource<?>> resources = new ArrayList<Resource<?>>();
        for (ScaffoldResource template : event.getResources()) {
            resources.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(template.getDestination()), getClass()
                    .getResourceAsStream(template.getSource()), event.isOverwrite()));
        }
        resourceRegistry.addAll(resources);
        return;
    }

}
