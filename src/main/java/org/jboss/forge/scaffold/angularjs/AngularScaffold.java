/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs;

import static org.jboss.forge.scaffold.angularjs.ResourceProvider.*;
import static org.jboss.forge.scaffoldx.constants.ScaffoldConstants.INSTALLING_SCAFFOLD;

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
import org.jboss.forge.env.ConfigurationFactory;
import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.parser.java.JavaSource;
import org.jboss.forge.parser.java.util.Strings;
import org.jboss.forge.project.Project;
import org.jboss.forge.project.facets.BaseFacet;
import org.jboss.forge.project.facets.DependencyFacet;
import org.jboss.forge.project.facets.JavaSourceFacet;
import org.jboss.forge.project.facets.MetadataFacet;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.project.facets.events.InstallFacets;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.ResourceFilter;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.scaffoldx.metawidget.MetawidgetInspectorFacade;
import org.jboss.forge.scaffoldx.AccessStrategy;
import org.jboss.forge.scaffoldx.ScaffoldProvider;
import org.jboss.forge.scaffoldx.ScaffoldQualifier;
import org.jboss.forge.scaffoldx.TemplateStrategy;
import org.jboss.forge.scaffoldx.facets.ScaffoldTemplateFacet;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.freemarker.TemplateLoaderConfig;
import org.jboss.forge.shell.Shell;
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
 * A {@link ScaffoldProvider} that generates AngularJS scaffolding from JPA entities. The generated scaffold is utilizes the
 * REST resources generated by the Forge rest plugin.
 */
@Alias("angularjs")
@Help("AngularJS scaffolding")
@RequiresFacet({ WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class, EJBFacet.class, CDIFacet.class,
        RestFacet.class })
public class AngularScaffold extends BaseFacet implements ScaffoldProvider {

    public static final String SCAFFOLD_DIR = "/scaffold";

    @Inject
    private ShellPrompt prompt;
    
    @Inject
    private ShellPrintWriter writer;

    @Inject
    private MetawidgetInspectorFacade metawidgetInspectorFacade;

    @Inject
    private InspectionResultProcessor angularResultEnhancer;

    @Inject
    private Event<InstallFacets> installFacets;

    @Inject
    private MetadataFacet metadata;

    @Inject
    private Event<CopyWebResourcesEvent> copyResourcesEvent;

    @Inject
    private Event<ProcessWithFreemarkerEvent> processWithFreemarkerEvent;

    @Inject
    private ResourceRegistry resourceRegistry;

    @Inject
    private ConfigurationFactory configurationFactory;

    // Do not refer this field directly. Use the getProjectConfiguration() method instead.
    private Configuration configuration;
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean install() {
        if (!isInstalled()) {
            this.installFacets.fire(new InstallFacets(WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class,
                    EJBFacet.class, CDIFacet.class, RestFacet.class));
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isInstalled() {
        String targetDir = getProjectConfiguration().getString(getTargetDirConfigKey(this));
		targetDir = targetDir == null ? "" : targetDir;
        if (project.hasAllFacets(WebResourceFacet.class, DependencyFacet.class, PersistenceFacet.class, EJBFacet.class,
                CDIFacet.class, RestFacet.class)) {
            // If the facet installation is in progress due to an InstallFacet event being fired, then the files would not
            // be available since ScaffoldProvider.setup would not have been executed, yet. We relax the facet installed
            // condition in such a case, to include only depdendencies being installed.
            Object isInstallInProgress = this.project.getAttribute(INSTALLING_SCAFFOLD);
            if (isInstallInProgress != null && isInstallInProgress.equals(Boolean.TRUE))
            {
               return true;
            }
            WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
            boolean areResourcesInstalled = web.getWebResource(targetDir + GLYPHICONS_SVG).exists()
                    && web.getWebResource(targetDir + GLYPHICONS_EOT).exists()
                    && web.getWebResource(targetDir + GLYPHICONS_SVG).exists()
                    && web.getWebResource(targetDir + GLYPHICONS_TTF).exists()
                    && web.getWebResource(targetDir + GLYPHICONS_WOFF).exists()
                    && web.getWebResource(targetDir + FORGE_LOGO_PNG).exists()
                    && web.getWebResource(targetDir + ANGULAR_RESOURCE_JS).exists()
                    && web.getWebResource(targetDir + ANGULAR_ROUTE_JS).exists()
                    && web.getWebResource(targetDir + ANGULAR_JS).exists()
                    && web.getWebResource(targetDir + MODERNIZR_JS).exists()
                    && web.getWebResource(targetDir + JQUERY_JS).exists()
                    && web.getWebResource(targetDir + BOOTSTRAP_JS).exists()
                    && web.getWebResource(targetDir + OFFCANVAS_JS).exists()
                    && web.getWebResource(targetDir + MAIN_CSS).exists()
                    && web.getWebResource(targetDir + BOOTSTRAP_CSS).exists()
                    && web.getWebResource(targetDir + BOOTSTRAP_THEME_CSS).exists()
                    && web.getWebResource(targetDir + LANDING_VIEW).exists();
            return areResourcesInstalled;
        }
        return false;
    }

    /**
     * Sets up the AngularJS scaffolding. Templates present in the 'scaffold' resource directory of this provider are copied to
     * src/main/templates if the end-user requests to install them. Static resources whose content do not depend on the JPA
     * entities, are also copied at this point.
     */
    public List<Resource<?>> setup(String targetDir, boolean overwrite, boolean installTemplates) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();

        // Copy templates
        if (installTemplates) {
            installTemplates();
        }

        // Setup static resources.
        copyResourcesEvent.fire(new CopyWebResourcesEvent(getStatics(targetDir), overwrite));
        result.addAll(resourceRegistry.getCreatedResources());
        
        // FORGE-1280. This is done since reads and writes to XMLConfiguration instances are not reflected immediately.
		if (!Strings.isNullOrEmpty(targetDir)) {
			// Busy wait until the configuration is updated (sigh).
			while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					break;
				}
				String configTargetDir = getProjectConfiguration().getString(getTargetDirConfigKey(this));
				if(targetDir.equals("/" + configTargetDir)) {
					break;
				}
			}
		}
        return result;
    }

    /**
     * Generates the application's index aka landing page, among others. All artifacts that are generated once per scaffolding
     * run are generated here.
     *
     * @param filteredClasses The list of {@link JavaClass}es that were scaffolded.
     * @param targetDir The target directory for the generated scaffold artifacts.
     * @param overwrite A flag that indicates whether existing resources should be overwritten or not.
     * @return A list of generated {@link Resource}s
     */
    public List<Resource<?>> generateIndex(List<JavaClass> filteredClasses, String targetDir, boolean overwrite) {
        ArrayList<Resource<?>> result = new ArrayList<Resource<?>>();

        /*
         * TODO: Revert this change at a later date, if necessary. This is currently done to ensure that entities are picked up
         * during invocation of the plugin from the Forge wizard in JBDS.
         */
        ResourceFilter filter = new ResourceFilter() {
            @Override
            public boolean accept(Resource<?> resource) {
                FileResource<?> file = (FileResource<?>) resource;

                if (!file.isDirectory() || file.getName().equals("resources") || file.getName().equals("WEB-INF")
                        || file.getName().equals("META-INF")) {
                    return false;
                }

                return true;
            }
        };

        WebResourceFacet web = this.project.getFacet(WebResourceFacet.class);
        List<Resource<?>> resources = web.getWebResource(targetDir + "/views/").listResources(filter);
        List<String> entityNames = new ArrayList<String>();
        for (Resource<?> resource : resources) {
            entityNames.add(resource.getName());
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

    /**
     * Generates the scaffolding artifacts for the provided list of resources.
     */
    @Override
    public List<Resource<?>> generateFrom(List<Resource<?>> resources, String targetDir, boolean overwrite) {
        List<Resource<?>> result = new ArrayList<Resource<?>>();
        // Filter the resources into JPA entities
        List<JavaClass> filteredClasses = filterResources(resources);
        for (JavaClass klass : filteredClasses) {
            ShellMessages.info(writer, "Generating artifacts from Class: [" + klass.getQualifiedName() + "]");
            
            String resourceRootPath = getRootResourcePath();
            String entityResourcePath = parseResourcePath(klass);
            if (entityResourcePath == null || entityResourcePath.isEmpty()) {
                entityResourcePath = klass.getName().toLowerCase() + "s";
            }
            entityResourcePath = prompt.prompt("What REST URI under /" + resourceRootPath
                    + " should be used to locate instances of type [" + klass.getQualifiedName() + "] ?", entityResourcePath);
            entityResourcePath = trimSlashes(entityResourcePath);

            // Inspect the JPA entity and obtain a list of inspection results. Every inspected property is represented as a
            // Map<String,String> and all such inspection results are collated into a list.
            List<Map<String, String>> inspectionResults = metawidgetInspectorFacade.inspect(klass);
            String entityId = angularResultEnhancer.fetchEntityId(klass, inspectionResults);
            inspectionResults = angularResultEnhancer.enhanceResults(klass, inspectionResults);

            // TODO: Provide a 'utility' class for allowing transliteration across language naming schemes
            // We need this to use contextual naming schemes instead of performing toLowerCase etc. in FTLs.

            // Prepare the Freemarker data model
            Map<String, Object> root = new HashMap<String, Object>();
            root.put("entityName", klass.getName());
            root.put("entityId", entityId);
            root.put("properties", inspectionResults);
            root.put("projectId", StringUtils.camelCase(metadata.getProjectName()));
            root.put("projectTitle", StringUtils.uncamelCase(metadata.getProjectName()));
            root.put("resourceRootPath", resourceRootPath);
            root.put("resourcePath", entityResourcePath);
            root.put("parentDirectories", getParentDirectories(targetDir));

            // Process the Freemarker templates with the Freemarker data model and retrieve the generated resources from the
            // registry.
            processWithFreemarkerEvent.fire(new ProcessWithFreemarkerEvent(getEntityTemplates(targetDir, klass.getName()),
                    root, overwrite));
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

    /**
     * Configures the welcome file entry in the project's web application descriptor to the static ever-present
     * <code>index.html</code> file. This method adds the entry only if it is absent.
     */
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

    /**
     * Filters the provided {@link Resource}s to return a list of {@link JavaClass}es that will be inspected and scaffolded.
     * {@link Resource}s that are not {@link JavaResource}s will be ignored. {@link JavaResource}s that are not JPA entities
     * will also be ignored.
     *
     * @param targets The user-provided list of {@link Resource}s to be scaffolded.
     * @return A list of {@link JavaClass}es that will be inspected and scaffolded.
     */
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

    /**
     * Obtains the root path for REST resources so that the AngularJS resource factory will be generated with the correct REST
     * resource URL.
     *
     * @return The root path of the REST resources generated by the Forge REST plugin.
     */
    private String getRootResourcePath() {
        String resourceRootPath = getProjectConfiguration().getString(RestFacet.ROOTPATH);
        if (resourceRootPath == null) {
            throw new RuntimeException(
                    "This project does not have a rootpath for REST resources. You may need to run \"rest setup\" in Forge.");
        }
        resourceRootPath = trimSlashes(resourceRootPath);
        return resourceRootPath;
    }

    /**
     * Installs the templates into src/main/templates. All Freemarker templates would be copied into the
     * src/main/templates/angularjs directory, obeying the same structure as the one in this provider.
     */
    private void installTemplates() {
        // Install the required facet so that the templates directory is created if not present.
        if (!project.hasFacet(ScaffoldTemplateFacet.class)) {
            installFacets.fire(new InstallFacets(ScaffoldTemplateFacet.class));
        }

        ScaffoldTemplateFacet templates = project.getFacet(ScaffoldTemplateFacet.class);
        // Obtain a reference to the scaffold directory in the classpath
        URL resource = getClass().getClassLoader().getResource("scaffold");
        if (resource != null && resource.getProtocol().equals("jar")) {
            try {
                // Obtain a reference to the JAR containing the scaffold directory
                JarURLConnection connection = (JarURLConnection) resource.openConnection();
                JarFile jarFile = connection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                // Iterate through the JAR entries and copy files to the template directory. Only files ending with .ftl, and
                // present in the scaffold/ directory are copied.
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entryName = jarEntry.getName();
                    if (entryName.startsWith("scaffold/") && entryName.endsWith(".ftl")) {
                        String relativeFilename = entryName.substring("scaffold/".length());
                        InputStream is = jarFile.getInputStream(jarEntry);
                        // Copy the file into a sub-directory under src/main/templates named after the scaffold provider.
                        templates.createResource(is, ConstraintInspector.getName(getClass()), relativeFilename);
                    }
                }
            } catch (IOException ioEx) {
                throw new RuntimeException(ioEx);
            }
        }
    }

    /**
     * Produces a {@link TemplateLoaderConfig} used to configure the {@link FreemarkerClient} instance. If a template directory
     * is present in the current project, it would be used over the provider-supplied templates.
     *
     * @return A {@link TemplateLoaderConfig} instance used to configure the {@link FreemarkerClient} on the locations to load
     *         the Freemarker templates.
     */
    @Produces
    @ScaffoldQualifier
    public TemplateLoaderConfig createTemplateLoaderConfig(Shell shell) {
    	Project project = shell.getCurrentProject();
        String providerName = ConstraintInspector.getName(AngularScaffold.class);
        File templateBaseDir = getTemplateBaseDir(project, providerName);
        return new TemplateLoaderConfig(templateBaseDir, getClass(), SCAFFOLD_DIR);
    }

    /**
     * Provides the location of the templates directory for the specified scaffold provider.
     *
     * @param scaffoldProviderName The name of the scaffold provider. Used to distinguish between multiple scaffold providers
     *        installed in the project.
     * @return The location of the templates directory if it is present in the current project. A null value is returned if the
     *         project doesnt have a template directory.
     */
    private File getTemplateBaseDir(Project project, String scaffoldProviderName) {
        if (project != null && project.hasFacet(ScaffoldTemplateFacet.class)) {
            ScaffoldTemplateFacet templateFacet = project.getFacet(ScaffoldTemplateFacet.class);
            DirectoryResource templateDirectory = templateFacet.getTemplateDirectory(scaffoldProviderName);
            File templateBaseDir = templateDirectory.getUnderlyingResourceObject();
            return templateBaseDir;
        }
        return null;
    }

    /**
     * Provided a target directory, this method calculates the parent directories to re-create the path to the web resource
     * root.
     *
     * @param targetDir The target directory that would be used as the basis for calculating the parent directories.
     * @return The parent directories to traverse. Represented as a sequence of '..' characters with '/' to denote multiple
     *         parent directories.
     */
    private String getParentDirectories(String targetDir) {
        if (targetDir == null || targetDir.isEmpty()) {
            return "";
        } else {
            targetDir = trimSlashes(targetDir);
            int parents = countOccurrences(targetDir, '/') + 1;
            StringBuilder parentDirectories = new StringBuilder();
            for (int ctr = 0; ctr < parents; ctr++) {
                parentDirectories.append("../");
            }
            return parentDirectories.toString();
        }
    }

    private int countOccurrences(String searchString, char charToSearch) {
        int count = 0;
        for (int ctr = 0; ctr < searchString.length(); ctr++) {
            if (searchString.charAt(ctr) == charToSearch) {
                count++;
            }
        }
        return count;
    }

    private String getTargetDirConfigKey(ScaffoldProvider provider) {
        return provider.getClass().getName() + "_targetDir";
    }
    
    /**
     * IMPORTANT: Use this method always to obtain the configuration. DO NOT invoke this inside a constructor since the returned
     * {@link Configuration} instance would not be the project scoped one.
     * 
     * @return The project scoped {@link Configuration} instance
     */
    private Configuration getProjectConfiguration() {
        if (this.configuration == null) {
            this.configuration = configurationFactory.getProjectConfig(project);
        }
        return this.configuration;
    }
    
    private String parseResourcePath(JavaClass klass) {
        JavaSourceFacet java = project.getFacet(JavaSourceFacet.class);
        ResourcePathVisitor visitor = new ResourcePathVisitor(klass.getName());
        java.visitJavaSources(visitor);
        return visitor.getPath();
    }
    
    private String trimSlashes(String aString) {
        if (aString.startsWith("/")) {
            aString = aString.substring(1);
        }
        if (aString.endsWith("/")) {
            aString = aString.substring(0, aString.length() - 1);
        }
        return aString;
    }

}
