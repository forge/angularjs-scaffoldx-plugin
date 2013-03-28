package org.jboss.forge.scaffold.angularjs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.jboss.forge.env.Configuration;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.PackagingFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.metawidget.MetawidgetInspectorFacade;
import org.jboss.forge.scaffoldx.AccessStrategy;
import org.jboss.forge.scaffoldx.ScaffoldProvider;
import org.jboss.forge.scaffoldx.TemplateStrategy;
import org.jboss.forge.scaffoldx.facets.ScaffoldTemplateFacet;
import org.jboss.forge.scaffoldx.util.ScaffoldUtil;
import org.jboss.forge.shell.ShellPrompt;
import org.jboss.forge.shell.plugins.Alias;
import org.jboss.forge.shell.plugins.Help;
import org.jboss.forge.shell.plugins.RequiresFacet;
import org.jboss.forge.shell.util.ConstraintInspector;
import org.jboss.forge.spec.javaee.CDIFacet;
import org.jboss.forge.spec.javaee.EJBFacet;
import org.jboss.forge.spec.javaee.PersistenceFacet;
import org.jboss.forge.spec.javaee.RestFacet;
import org.jboss.forge.spec.javaee.ServletFacet;
import org.metawidget.util.simple.StringUtils;

/**
 *
 */
@Alias("angularjs")
@Help("AngularJS scaffolding")
@RequiresFacet({ WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class, EJBFacet.class, CDIFacet.class,
        RestFacet.class })
public class AngularScaffold extends BaseFacet implements ScaffoldProvider {

    protected ShellPrompt prompt;

    protected MetawidgetInspectorFacade metawidgetInspectorFacade;

    protected AngularResultEnhancer angularResultEnhancer;

    protected Configuration configuration;

    private Event<InstallFacets> installTemplatesEvent;

    @Inject
    public AngularScaffold(final ShellPrompt prompt, final MetawidgetInspectorFacade metawidgetInspectorFacade,
            final AngularResultEnhancer angularResultEnhancer, final Configuration configuration,
            final Event<InstallFacets> installTemplatesEvent) {
        this.prompt = prompt;
        this.metawidgetInspectorFacade = metawidgetInspectorFacade;
        this.angularResultEnhancer = angularResultEnhancer;
        this.configuration = configuration;
        this.installTemplatesEvent = installTemplatesEvent;
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
        if (installTemplates) {
            installTemplates();
        }

        // Setup static resources.
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource(targetDir + "/styles/bootstrap.css"),
                getClass().getResourceAsStream("/scaffold/styles/bootstrap.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource(targetDir + "/styles/main.css"), getClass()
                .getResourceAsStream("/scaffold/styles/main.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt,
                web.getWebResource(targetDir + "/styles/bootstrap-responsive.css"),
                getClass().getResourceAsStream("/scaffold/styles/bootstrap-responsive.css"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource(targetDir + "/scripts/vendor/angular.js"),
                getClass().getResourceAsStream("/scaffold/scripts/vendor/angular.js"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt,
                web.getWebResource(targetDir + "/scripts/vendor/angular-resource.js"),
                getClass().getResourceAsStream("/scaffold/scripts/vendor/angular-resource.js"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource(targetDir + "/img/forge-logo.png"),
                getClass().getResourceAsStream("/scaffold/img/forge-logo.png"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt, web.getWebResource(targetDir + "/img/glyphicons-halflings.png"),
                getClass().getResourceAsStream("/scaffold/img/glyphicons-halflings.png"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt,
                web.getWebResource(targetDir + "/img/glyphicons-halflings-white.png"),
                getClass().getResourceAsStream("/scaffold/img/glyphicons-halflings-white.png"), overwrite));
        result.add(ScaffoldUtil.createOrOverwrite(this.prompt,
                web.getWebResource(targetDir + "/test/lib/angular/angular-scenario.js"),
                getClass().getResourceAsStream("/scaffold/test/lib/angular/angular-scenario.js"), overwrite));
        return result;
    }

    public List<Resource<?>> generateIndex(List<Resource<?>> resources, String targetDir, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        List<String> entityNames = new ArrayList<String>();
        for (Resource<?> resource : resources) {
            JavaClass klass = getJavaClassFrom(resource);
            entityNames.add(klass.getName());
        }

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        MetadataFacet metadata = this.project.getFacet(MetadataFacet.class);
        String projectIdentifier = StringUtils.camelCase(metadata.getProjectName());
        String projectTitle = StringUtils.uncamelCase(metadata.getProjectName());
        root.put("projectId", projectIdentifier);
        root.put("projectTitle", projectTitle);

        Map<String, String> projectGlobalTemplates = new HashMap<String, String>();
        projectGlobalTemplates.put("index.html.ftl", "/index.html");
        projectGlobalTemplates.put("scripts/app.js.ftl", "/scripts/app.js");
        projectGlobalTemplates.put("scripts/services/locationParser.js.ftl", "/scripts/services/locationParser.js");
        projectGlobalTemplates.put("scripts/filters/genericSearchFilter.js.ftl", "/scripts/filters/genericSearchFilter.js");
        projectGlobalTemplates.put("scripts/filters/startFromFilter.js.ftl", "/scripts/filters/startFromFilter.js");
        projectGlobalTemplates.put("test/e2e/runner.html.ftl", "/test/e2e/runner.html");

        FreemarkerClient freemarkerClient = new FreemarkerClient(getTemplateBaseDir(), getClass(), "/scaffold");
        WebResourceFacet web = project.getFacet(WebResourceFacet.class);
        for (String projectGlobalTemplate : projectGlobalTemplates.keySet()) {
            String output = freemarkerClient.processFTL(root, projectGlobalTemplate);
            String outputPath = projectGlobalTemplates.get(projectGlobalTemplate);
            result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(targetDir + outputPath), output, overwrite));
        }
        this.project.getFacet(ServletFacet.class).getConfig().welcomeFile(targetDir + "/index.html");
        return result;
    }

    @Override
    public List<Resource<?>> generateFrom(List<Resource<?>> resources, String targetDir, boolean overwrite) {
        List<Resource<?>> result = new ArrayList<Resource<?>>();
        for (Resource<?> resource : resources) {
            JavaClass klass = getJavaClassFrom(resource);
            System.out.println("Generating artifacts from Class:" + klass.getQualifiedName());
            WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);

            List<Map<String, String>> inspectionResults = metawidgetInspectorFacade.inspect(klass);
            inspectionResults = angularResultEnhancer.enhanceResults(klass, inspectionResults);
            Map<String, Object> root = new HashMap<String, Object>();
            // TODO: Provide a 'utility' class for allowing transliteration across language naming schemes
            // We need this to use contextual naming schemes instead of performing toLowerCase etc. in FTLs.
            root.put("entityName", klass.getName());
            root.put("properties", inspectionResults);
            MetadataFacet metadata = this.project.getFacet(MetadataFacet.class);
            String projectIdentifier = StringUtils.camelCase(metadata.getProjectName());
            String projectTitle = StringUtils.uncamelCase(metadata.getProjectName());
            String contextRoot = project.getFacet(PackagingFacet.class).getFinalName();
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
            root.put("contextRoot", contextRoot);

            Map<String, String> perEntityTemplates = new HashMap<String, String>();
            perEntityTemplates.put("views/detail.html.ftl", "/views/" + klass.getName() + "/detail.html");
            perEntityTemplates.put("views/search.html.ftl", "/views/" + klass.getName() + "/search.html");
            perEntityTemplates.put("scripts/services/entityFactory.js.ftl", "/scripts/services/" + klass.getName()
                    + "Factory.js");
            perEntityTemplates.put("scripts/controllers/newEntityController.js.ftl",
                    "/scripts/controllers/new" + klass.getName() + "Controller.js");
            perEntityTemplates.put("scripts/controllers/searchEntityController.js.ftl",
                    "/scripts/controllers/search" + klass.getName() + "Controller.js");
            perEntityTemplates.put("scripts/controllers/editEntityController.js.ftl",
                    "/scripts/controllers/edit" + klass.getName() + "Controller.js");
            perEntityTemplates.put("test/e2e/scenarios.js.ftl", "/test/e2e/" + klass.getName() + "scenarios.js");

            FreemarkerClient freemarkerClient = new FreemarkerClient(getTemplateBaseDir(), getClass(), "/scaffold");
            for (String entityTemplate : perEntityTemplates.keySet()) {
                String output = freemarkerClient.processFTL(root, entityTemplate);
                String outputPath = perEntityTemplates.get(entityTemplate);
                result.add(ScaffoldUtil.createOrOverwrite(prompt, web.getWebResource(targetDir + outputPath), output, overwrite));
            }
        }

        List<Resource<?>> indexResources = generateIndex(resources, targetDir, overwrite);
        result.addAll(indexResources);
        return result;
    }

    @Override
    public AccessStrategy getAccessStrategy() {
        // Not required for the Angular scaffold generator.
        return null;
    }

    @Override
    public TemplateStrategy getTemplateStrategy() {
        // Not required for the Angular scaffold generator.
        return null;
    }

    private JavaClass getJavaClassFrom(Resource<?> resource) {
        JavaResource javaResource = null;
        if (resource instanceof JavaResource) {
            javaResource = (JavaResource) resource;
        } else {
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
        if (!project.hasFacet(ScaffoldTemplateFacet.class)) {
            installTemplatesEvent.fire(new InstallFacets(ScaffoldTemplateFacet.class));
        }
        ScaffoldTemplateFacet templates = project.getFacet(ScaffoldTemplateFacet.class);
        URL resource = getClass().getClassLoader().getResource("scaffold");
        if (resource != null && resource.getProtocol().equals("jar")) {
            try {
                JarURLConnection connection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = connection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entryName = jarEntry.getName();
                    if (entryName.startsWith("scaffold/") && entryName.endsWith(".ftl")) {
                        String relativeFilename = entryName.substring("scaffold/".length());
                        InputStream is = jarFile.getInputStream(jarEntry);
                        templates.createResource(is, ConstraintInspector.getName(getClass()), relativeFilename);
                    }
                }
            } catch (IOException ioEx) {
                throw new RuntimeException(ioEx);
            }
        }
    }

    private File getTemplateBaseDir() {
        if (project.hasFacet(ScaffoldTemplateFacet.class)) {
            ScaffoldTemplateFacet templateFacet = project.getFacet(ScaffoldTemplateFacet.class);
            String scaffoldProviderName = ConstraintInspector.getName(AngularScaffold.class);
            DirectoryResource templateDirectory = templateFacet.getTemplateDirectory(scaffoldProviderName);
            File templateBaseDir = templateDirectory.getUnderlyingResourceObject();
            return templateBaseDir;
        }
        return null;
    }

}
