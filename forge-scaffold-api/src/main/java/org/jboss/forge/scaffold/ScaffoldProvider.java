/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold;

import java.util.List;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.Facet;
import org.jboss.forge.resources.Resource;
import org.jboss.forge.resources.java.JavaResource;
import org.jboss.forge.shell.plugins.Plugin;

/**
 * Provides an implementation of Scaffolding for various UI code generation operations.
 * 
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ScaffoldProvider extends Facet
{
   /**
    * Set up this scaffold provider, installing any necessary {@link Facet} or {@link Plugin} implementations as
    * necessary. Install the templates in the provider to the src/main/templates directory of the project.
    */
   List<Resource<?>> setup(String targetDir, boolean overwrite, boolean installTemplates);

   /**
    * Generate a set of create, read, update, delete pages for a given JPA entity {@link JavaClass}.
    */
   List<Resource<?>> generateFrom(List<JavaResource> entity, boolean overwrite);

   /**
    * Attempt to locate all {@link Resource}s generated by this {@link ScaffoldProvider}.
    */
   List<Resource<?>> getGeneratedResources(String targetDir);

}
