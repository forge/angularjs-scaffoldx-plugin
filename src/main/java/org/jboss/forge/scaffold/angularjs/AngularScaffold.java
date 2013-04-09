package org.jboss.forge.scaffold.angularjs;

import static org.jboss.forge.scaffold.angularjs.ResourceProvider.getEntityTemplates;
import static org.jboss.forge.scaffold.angularjs.ResourceProvider.getGlobalTemplates;
import static org.jboss.forge.scaffold.angularjs.ResourceProvider.getStatics;

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
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.Entity;

import org.jboss.forge.env.Configuration;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.PackagingFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffoldx.metawidget.MetawidgetInspectorFacade;
import org.jboss.forge.scaffoldx.AccessStrategy;
import org.jboss.forge.scaffoldx.ScaffoldProvider;
import org.jboss.forge.scaffoldx.ScaffoldQualifier;
import org.jboss.forge.scaffoldx.TemplateStrategy;
import org.jboss.forge.scaffoldx.facets.ScaffoldTemplateFacet;
import org.jboss.forge.scaffoldx.freemarker.TemplateLoaderConfig;
import org.jboss.forge.shell.ShellMessages;
import org.jboss.forge.shell.ShellPrintWriter;
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
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.metawidget.util.simple.StringUtils;

/**
 *
 */
@Alias("angularjs")
@Help("AngularJS scaffolding")
@RequiresFacet({ WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class, EJBFacet.class, CDIFacet.class,
        RestFacet.class })
public class AngularScaffold extends BaseFacet implements ScaffoldProvider {

    public static final String SCAFFOLD_DIR = "/scaffold";

    protected ShellPrompt prompt;

    protected ShellPrintWriter writer;

    protected MetawidgetInspectorFacade metawidgetInspectorFacade;

    protected InspectionResultProcessor angularResultEnhancer;

    protected Configuration configuration;

    private Event<InstallFacets> installTemplatesEvent;

    @Inject
    private MetadataFacet metadata;

    @Inject
    private Event<CopyWebResourcesEvent> copyResourcesEvent;
    
    @Inject
    private Event<ProcessWithFreemarkerEvent> processWithFreemarkerEvent;
    
    @Inject
    private ResourceRegistry resourceRegistry;
    
    @Inject
    public AngularScaffold(final ShellPrompt prompt, final ShellPrintWriter writer,
            final MetawidgetInspectorFacade metawidgetInspectorFacade, final InspectionResultProcessor angularResultEnhancer,
            final Configuration configuration, final Event<InstallFacets> installTemplatesEvent) {
        this.prompt = prompt;
        this.writer = writer;
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

        // Copy templates
        if (installTemplates) {
            installTemplates();
        }

        // Setup static resources.
        copyResourcesEvent.fire(new CopyWebResourcesEvent(getStatics(targetDir), overwrite));
        result.addAll(resourceRegistry.getCreatedResources());
        return result;
    }

    public List<Resource<?>> generateIndex(List<JavaClass> filteredClasses, String targetDir, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();
        List<String> entityNames = new ArrayList<String>();
        for (JavaClass klass : filteredClasses) {
            entityNames.add(klass.getName());
        }

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        root.put("projectId", StringUtils.camelCase(metadata.getProjectName()));
        root.put("projectTitle", StringUtils.uncamelCase(metadata.getProjectName()));
        root.put("targetDir", targetDir);

        processWithFreemarkerEvent.fire(new ProcessWithFreemarkerEvent(getGlobalTemplates(targetDir), root, overwrite));
        result.addAll(resourceRegistry.getCreatedResources());
        configureWelcomeFile();
        return result;
    }

    @Override
    public List<Resource<?>> generateFrom(List<Resource<?>> resources, String targetDir, boolean overwrite) {
        List<Resource<?>> result = new ArrayList<Resource<?>>();
        List<JavaClass> filteredClasses = filterResources(resources);
        for (JavaClass klass : filteredClasses) {
            ShellMessages.info(writer, "Generating artifacts from Class: [" + klass.getQualifiedName() + "]");

            List<Map<String, String>> inspectionResults = metawidgetInspectorFacade.inspect(klass);
            String entityId = angularResultEnhancer.fetchEntityId(klass, inspectionResults);
            inspectionResults = angularResultEnhancer.enhanceResults(klass, inspectionResults);
            // TODO: Provide a 'utility' class for allowing transliteration across language naming schemes
            // We need this to use contextual naming schemes instead of performing toLowerCase etc. in FTLs.
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("entityName", klass.getName());
            root.put("entityId", entityId);
            root.put("properties", inspectionResults);
            root.put("projectId", StringUtils.camelCase(metadata.getProjectName()));
            root.put("projectTitle", StringUtils.uncamelCase(metadata.getProjectName()));
            root.put("resourceRootPath", getRootResourcePath());
            root.put("contextRoot", project.getFacet(PackagingFacet.class).getFinalName());

            processWithFreemarkerEvent.fire(new ProcessWithFreemarkerEvent(getEntityTemplates(targetDir, klass.getName()), root, overwrite));
            result.addAll(resourceRegistry.getCreatedResources());
        }

        List<Resource<?>> indexResources = generateIndex(filteredClasses, targetDir, overwrite);
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

    private void configureWelcomeFile() {
        String indexFileEntry = "/index.html";

        ServletFacet servlet = this.project.getFacet(ServletFacet.class);
        WebAppDescriptor webAppDescriptor = servlet.getConfig();
        if (!webAppDescriptor.getWelcomeFiles().contains(indexFileEntry)) {
            webAppDescriptor = webAppDescriptor.welcomeFile(indexFileEntry);
            servlet.saveConfig(webAppDescriptor);
        }
        return;
    }

    private void displaySkippingNonexistentResourceMsg(final Resource<?> r) {
        ShellMessages.warn(writer, "Skipped non-existent Java resource [" + r.getFullyQualifiedName() + "]");
    }

    private void displaySkippingResourceMsg(final JavaSource<?> entity) {
        ShellMessages.info(writer, "Skipped non-@Entity Java resource [" + entity.getQualifiedName() + "]");
    }

    private List<JavaClass> filterResources(List<Resource<?>> targets) {
        List<JavaClass> results = new ArrayList<JavaClass>();
        if (targets == null) {
            targets = new ArrayList<Resource<?>>();
        }
        for (Resource<?> r : targets) {
            if (r instanceof JavaResource) {
                JavaSource<?> entity;
                try {
                    entity = ((JavaResource) r).getJavaSource();
                } catch (FileNotFoundException fileEx) {
                    displaySkippingNonexistentResourceMsg(r);
                    continue;
                }

                // Filter Java classes that have the JPA @Entity annotation, and skip the rest
                if (entity instanceof JavaClass) {
                    if (entity.hasAnnotation(Entity.class)) {
                        results.add((JavaClass) entity);
                    } else {
                        displaySkippingResourceMsg(entity);
                    }
                } else {
                    displaySkippingResourceMsg(entity);
                }
            }
        }
        return results;
    }

    private String getRootResourcePath() {
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
        return resourceRootPath;
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
    
    @Produces
    @ScaffoldQualifier
    public TemplateLoaderConfig createTemplateLoaderConfig()
    {
        String providerName = ConstraintInspector.getName(AngularScaffold.class);
        File templateBaseDir = getTemplateBaseDir(providerName);
        return new TemplateLoaderConfig(templateBaseDir, getClass(), SCAFFOLD_DIR);
    }
    
    private File getTemplateBaseDir(String scaffoldProviderName)
    {
       if (project != null && project.hasFacet(ScaffoldTemplateFacet.class))
       {
          ScaffoldTemplateFacet templateFacet = project.getFacet(ScaffoldTemplateFacet.class);
          DirectoryResource templateDirectory = templateFacet.getTemplateDirectory(scaffoldProviderName);
          File templateBaseDir = templateDirectory.getUnderlyingResourceObject();
          return templateBaseDir;
       }
       return null;
    }

}
