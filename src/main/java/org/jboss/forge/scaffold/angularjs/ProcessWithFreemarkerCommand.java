package org.jboss.forge.scaffold.angularjs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    
    public List<Resource<?>> execute(List<ScaffoldResource> templates, Map<String,Object> root, boolean overwrite) {
        List<Resource<?>> resources = new ArrayList<Resource<?>>();
        for (ScaffoldResource projectGlobalTemplate : templates) {
            String output = freemarkerClient.processFTL(root, projectGlobalTemplate.getSource());
            resources.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(projectGlobalTemplate.getDestination()), output, overwrite));
        }
        return resources;
    }
    
}
