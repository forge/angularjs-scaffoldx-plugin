package org.jboss.forge.scaffold.angularjs;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.util.ScaffoldUtil;
import org.jboss.forge.shell.ShellPrompt;

public class ProcessWithFreemarkerCommand {

    @Inject
    private ShellPrompt prompt;

    @Inject
    private WebResourceFacet web;

    @Inject
    private FreemarkerClient freemarkerClient;

    @Inject
    private ResourceRegistry resourceRegistry;

    public void execute(@Observes ProcessWithFreemarkerEvent event) {
        resourceRegistry.clear();
        List<Resource<?>> resources = new ArrayList<Resource<?>>();
        for (ScaffoldResource projectGlobalTemplate : event.getResources()) {
            String output = freemarkerClient.processFTL(event.getRoot(), projectGlobalTemplate.getSource());
            resources.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(projectGlobalTemplate.getDestination()),
                    output, event.isOverwrite()));
        }
        resourceRegistry.addAll(resources);
        return;
    }

}
