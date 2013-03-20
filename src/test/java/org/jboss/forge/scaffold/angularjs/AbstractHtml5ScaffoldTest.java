package org.jboss.forge.scaffold.angularjs;

import javax.inject.Inject;

import org.jboss.forge.project.Project;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.forge.test.web.DroneTest;
import org.junit.Before;

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
