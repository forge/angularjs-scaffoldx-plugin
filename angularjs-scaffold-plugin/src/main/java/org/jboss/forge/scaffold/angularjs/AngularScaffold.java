package org.jboss.forge.scaffold.angularjs;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.jboss.forge.env.Configuration;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffoldx.AccessStrategy;
import org.jboss.forge.scaffoldx.ScaffoldProvider;
import org.jboss.forge.scaffoldx.TemplateStrategy;
import org.jboss.forge.scaffoldx.facets.ScaffoldTemplateFacet;
import org.jboss.forge.scaffoldx.util.ScaffoldUtil;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.project.ProjectScoped;
import org.jboss.forge.shell.util.ConstraintInspector;
import org.jboss.forge.spec.javaee.CDIFacet;
import org.jboss.forge.spec.javaee.EJBFacet;
import org.jboss.forge.spec.javaee.PersistenceFacet;
import org.jboss.forge.spec.javaee.RestFacet;
import org.metawidget.util.simple.StringUtils;

/**
 *
 */
@Alias("angularjs")
@Help("AngularJS scaffolding")
@RequiresFacet({ WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class, EJBFacet.class, CDIFacet.class,
        RestFacet.class, ScaffoldTemplateFacet.class })
public class AngularScaffold extends BaseFacet implements ScaffoldProvider {

    protected ShellPrompt prompt;
    
    @Inject
    protected IntrospectorClient introspectorClient;
    
    @Inject
    @ProjectScoped
    Configuration configuration;

    @Inject
    public AngularScaffold(final ShellPrompt prompt) {
        this.prompt = prompt;
    }

    @Override
    public boolean install() {
        // TODO Add Maven artifacts to the project here. Required facet installation is already handled by the class-level
        // @RequiresFacet annotation.
        return true;
    }

    @Override
    public boolean isInstalled() {
        // TODO Looks unnecessary for this scaffold. See comments on install(). We could extract install() and installed() out.
        return true;
    }

    
    public List<Resource<?>> setup(String targetDir, boolean overwrite, boolean installTemplates) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);

        // Copy templates
        if(installTemplates) {
            installTemplates();            
        }
        
        // Setup static resources.
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/styles/bootstrap.css"), getClass()
                .getResourceAsStream("/scaffold/styles/bootstrap.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/styles/main.css"), getClass()
                .getResourceAsStream("/scaffold/styles/main.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/styles/bootstrap-responsive.css"),
                getClass().getResourceAsStream("/scaffold/styles/bootstrap-responsive.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/scripts/vendor/angular.js"), getClass()
                .getResourceAsStream("/scaffold/scripts/vendor/angular.js"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/scripts/vendor/angular-resource.js"),
                getClass().getResourceAsStream("/scaffold/scripts/vendor/angular-resource.js"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/img/forge-logo.png"), getClass()
                .getResourceAsStream("/scaffold/img/forge-logo.png"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/img/glyphicons-halflings.png"), getClass()
                .getResourceAsStream("/scaffold/img/glyphicons-halflings.png"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/img/glyphicons-halflings-white.png"), getClass()
                .getResourceAsStream("/scaffold/img/glyphicons-halflings-white.png"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource("/test/lib/angular/angular-scenario.js"),
                getClass().getResourceAsStream("/scaffold/test/lib/angular/angular-scenario.js"), overwrite));
        return result;
    }

    
    public List<Resource<?>> generateIndex(List<Resource<?>> resources, String targetDir, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        List<String> entityNames = new ArrayList<String>();
        // TODO: Use a better way to provide the list of all entities
        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
        FileResource<?> partialsDirectory = web.getWebResource("views");
        for (Resource<?> resource : partialsDirectory.listResources()) {
            entityNames.add(resource.getName());
        }
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        MetadataFacet metadata = this.project.getFacet(MetadataFacet.class);
        String projectIdentifier = StringUtils.camelCase(metadata.getProjectName());
        String projectTitle = StringUtils.uncamelCase(metadata.getProjectName());
        root.put("projectId", projectIdentifier);
        root.put("projectTitle", projectTitle);

        Map<String, String> projectGlobalTemplates = new HashMap<String, String>();
        projectGlobalTemplates.put("index.html.ftl", "index.html");
        projectGlobalTemplates.put("scripts/app.js.ftl", "scripts/app.js");
        projectGlobalTemplates.put("scripts/services/locationParser.js.ftl", "scripts/services/locationParser.js");
        projectGlobalTemplates.put("scripts/filters/genericSearchFilter.js.ftl", "scripts/filters/genericSearchFilter.js");
        projectGlobalTemplates.put("scripts/filters/startFromFilter.js.ftl", "scripts/filters/startFromFilter.js");
        projectGlobalTemplates.put("test/e2e/runner.html.ftl", "test/e2e/runner.html");
        
        FreemarkerClient freemarkerClient = new FreemarkerClient();
        for (String projectGlobalTemplate : projectGlobalTemplates.keySet()) {
            String output = freemarkerClient.processFTL(root, projectGlobalTemplate);
            String outputPath = projectGlobalTemplates.get(projectGlobalTemplate);
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(outputPath), output, overwrite));
        }
        return result;
    }

    @Override
    public List<Resource<?>> generateFrom(List<Resource<?>> resources, String targetDir, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        for(Resource<?> resource: resources)
        {
            JavaClass entity = getJavaClassFrom(resource);
            System.out.println("Generating artifacts from Entity:" + entity.getQualifiedName());
            WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
            
            List<Map<String, String>> inspectionResults = introspectorClient.inspect(entity);
            Map<String, Object> root = new HashMap<String, Object>();
            // TODO: Provide a 'utility' class for allowing transliteration across language naming schemes
            // We need this to use contextual naming schemes instead of performing toLowerCase etc. in FTLs.
            root.put("entityName", entity.getName());
            root.put("properties", inspectionResults);
            MetadataFacet metadata = this.project.getFacet(MetadataFacet.class);
            String projectIdentifier = StringUtils.camelCase(metadata.getProjectName());
            String projectTitle = StringUtils.uncamelCase(metadata.getProjectName());
            String resourceRootPath = configuration.getString(RestFacet.ROOTPATH);
            if (resourceRootPath == null) {
                throw new RuntimeException(
                        "This project does not have a rootpath for REST resources. You may need to run \"rest setup\" in Forge.");
            }
            if (resourceRootPath.startsWith("/")) {
                resourceRootPath = resourceRootPath.substring(1);
            }
            if (resourceRootPath.endsWith("/")) {
                resourceRootPath = resourceRootPath.substring(0, resourceRootPath.length() - 1);
            }
            root.put("projectId", projectIdentifier);
            root.put("projectTitle", projectTitle);
            root.put("resourceRootPath", resourceRootPath);
    
            // TODO: The list of template files to be processed per-entity (like detail.html.ftl and search.html.ftl) needs to
            // be obtained dynamically. Another list to be processed for all entities (like index.html.ftl) also needs to be
            // maintained. In short, a template should be associated with a processing directive like PER_ENTITY, PER_PROJECT etc.
            Map<String, String> perEntityTemplates = new HashMap<String, String>();
            perEntityTemplates.put("views/detail.html.ftl", "/views/" + entity.getName() + "/detail.html");
            perEntityTemplates.put("views/search.html.ftl", "/views/" + entity.getName() + "/search.html");
            perEntityTemplates.put("scripts/services/entityFactory.js.ftl", "/scripts/services/" + entity.getName() + "Factory.js");
            perEntityTemplates.put("scripts/controllers/newEntityController.js.ftl", "/scripts/controllers/new" + entity.getName() + "Controller.js");
            perEntityTemplates.put("scripts/controllers/searchEntityController.js.ftl", "/scripts/controllers/search" + entity.getName() + "Controller.js");
            perEntityTemplates.put("scripts/controllers/editEntityController.js.ftl", "/scripts/controllers/edit" + entity.getName() + "Controller.js");
            perEntityTemplates.put("test/e2e/scenarios.js.ftl", "test/e2e/" + entity.getName() + "scenarios.js");
            
            FreemarkerClient freemarkerClient = new FreemarkerClient();
            for (String entityTemplate : perEntityTemplates.keySet()) {
                String output = freemarkerClient.processFTL(root, entityTemplate);
                String outputPath = perEntityTemplates.get(entityTemplate);
                result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(outputPath), output, overwrite));
            }
        }

        generateIndex(resources, targetDir, overwrite);
        return result;
    }

    @Override
    public AccessStrategy getAccessStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemplateStrategy getTemplateStrategy() {
        // TODO Auto-generated method stub
        return null;
    }

    private JavaClass getJavaClassFrom(Resource<?> resource) {
        JavaResource javaResource = null;
        if(resource instanceof JavaResource)
        {
            javaResource = (JavaResource) resource;
        }
        else
        {
            return null;
        }
        JavaClass entity;
        try {
            entity = (JavaClass) javaResource.getJavaSource();
        } catch (FileNotFoundException fileEx) {
            throw new RuntimeException(fileEx);
        }
        return entity;
    }

    private void installTemplates() {
        ScaffoldTemplateFacet templates = project.getFacet(ScaffoldTemplateFacet.class);
        //templates.createResource(null, ConstraintInspector.getName(getClass()), null);
    }

}
