/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs.scenario;

import java.io.InputStream;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.StringContains;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.shell.util.Streams;
import org.junit.Assert;

/**
 * Test helpers to aid in verifying whether the required files are generated, and in some cases also verify whether their
 * contents are the same as expected.
 */
public class TestHelpers {

    private static final String SCENARIO_OUTPUTS = "expected-scenario-outputs/";

    public static void assertStaticFilesAreGenerated(WebResourceFacet web) {
        assertStaticJSAssetsAreGenerated(web);
        assertStaticCSSAssetsAreGenerated(web);
        assertImageAssetsAreGenerated(web);
    }

    public static void assertWebResourceContents(WebResourceFacet web, String relativePath, String scenario) {
        FileResource<?> actualResource = web.getWebResource(relativePath);
        String actualContents = Streams.toString(actualResource.getResourceInputStream());
        String expectedContents = Streams.toString(getScenarioResource(relativePath, scenario));
        // TODO: This needs a better matcher that pinpoints which line and which character has not matched.
        Assert.assertThat(actualContents, IsEqual.equalTo(expectedContents));
    }

    private static InputStream getScenarioResource(String relativePath, String scenario) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(SCENARIO_OUTPUTS + scenario + relativePath);
    }

    /**
     * Verifies if the expected static JS assets are available under the {@link WebResourceFacet}.
     * 
     * This may also include files with minimal amount of generated JavaScript in them. For example, AngularJS files that only
     * have the module name inserted into them by the FTLs, can be verified here, since the content of the files is otherwise
     * static and not dependent on the object model.
     * 
     * @param web The {@link WebResourceFacet} representing the web resources for the generated Java project.
     */
    private static void assertStaticJSAssetsAreGenerated(WebResourceFacet web) {
        // AngularJS
        FileResource<?> angularjs = web.getWebResource("/scripts/vendor/angular.js");
        Assert.assertTrue(angularjs.exists());

        FileResource<?> angularResource = web.getWebResource("/scripts/vendor/angular-resource.js");
        Assert.assertTrue(angularResource.exists());

        // Filters
        FileResource<?> startFromFilter = web.getWebResource("/scripts/filters/startFromFilter.js");
        Assert.assertTrue(startFromFilter.exists());
        String startFromFilterContents = Streams.toString(startFromFilter.getResourceInputStream());
        Assert.assertThat(startFromFilterContents, StringContains.containsString("angular.module('test')"));
        FileResource<?> genericSearchFilter = web.getWebResource("/scripts/filters/genericSearchFilter.js");
        Assert.assertTrue(genericSearchFilter.exists());
        String genericSearchFilterContents = Streams.toString(startFromFilter.getResourceInputStream());
        Assert.assertThat(genericSearchFilterContents, StringContains.containsString("angular.module('test')"));

        // Services
        FileResource<?> locationParser = web.getWebResource("/scripts/services/locationParser.js");
        Assert.assertTrue(locationParser.exists());
        String locationParserContents = Streams.toString(locationParser.getResourceInputStream());
        Assert.assertThat(locationParserContents, StringContains.containsString("angular.module('test')"));
    }

    /**
     * Verifies if the expected static CSS assets are available under the {@link WebResourceFacet}.
     * 
     * @param web The {@link WebResourceFacet} representing the web resources for the generated Java project.
     */
    private static void assertStaticCSSAssetsAreGenerated(WebResourceFacet web) {
        // Twitter BootStrap
        FileResource<?> bootstrapCss = web.getWebResource("/styles/bootstrap.css");
        Assert.assertTrue(bootstrapCss.exists());

        FileResource<?> bootstrapResponsiveCss = web.getWebResource("/styles/bootstrap-responsive.css");
        Assert.assertTrue(bootstrapResponsiveCss.exists());

        // Application
        FileResource<?> applicationCss = web.getWebResource("/styles/main.css");
        Assert.assertTrue(applicationCss.exists());
    }

    /**
     * Verifies if the expected static image assets are available under the {@link WebResourceFacet}.
     * 
     * @param web The {@link WebResourceFacet} representing the web resources for the generated Java project.
     */
    private static void assertImageAssetsAreGenerated(WebResourceFacet web) {
        // Forge Logo
        FileResource<?> logoImage = web.getWebResource("/img/forge-logo.png");
        Assert.assertTrue(logoImage.exists());
    }
}
