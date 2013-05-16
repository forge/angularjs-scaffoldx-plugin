/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs;

import org.jboss.forge.project.Project;
import org.jboss.forge.test.AbstractShellTest;
import org.junit.Before;

/**
 *  The abstract test class that contains infrastructural setup logic.
 *  This creates a project before every test executed in a sub-class. 
 */
public abstract class AbstractHtml5ScaffoldTest extends AbstractShellTest {

    protected Project project;
    
    protected Project current;
    
    @Before
    public void setup() throws Exception {
        current = getShell().getCurrentProject();
        project = setupScaffoldProject();
    }

    protected Project setupScaffoldProject() throws Exception {
        Project project = initializeJavaProject();
        queueInputLines("HIBERNATE", "JBOSS_AS7", "", "", "");
        getShell().execute("persistence setup");
        queueInputLines("", "", "");
        getShell().execute("validation setup --provider JAVA_EE");
        queueInputLines("", "", "", "");
        getShell().execute("scaffold-x setup --scaffoldType angularjs");
        return project;
    }

    protected void generateScaffold() throws Exception {
        queueInputLines("", "", "", "", "");
        getShell().execute("cd ~~");
        getShell().execute("scaffold-x from src/main/java/com/test/model/*");
    }
    
    protected void generateRestResources() throws Exception {
        getShell().execute("rest setup --activatorType WEB_XML");
        getShell().execute("rest endpoint-from-entity --contentType application/json com.test.model.*");
    }

}
