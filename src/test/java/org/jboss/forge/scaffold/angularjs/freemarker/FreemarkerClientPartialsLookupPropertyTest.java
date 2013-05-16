/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs.freemarker;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.jboss.forge.scaffold.angularjs.TestHelpers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.freemarker.TemplateLoaderConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;
import org.metawidget.util.simple.StringUtils;

/**
 * Tests to verify that the generated HTML for Enum properties of JPA entities are generated correctly.
 */
public class FreemarkerClientPartialsLookupPropertyTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null,
                FreemarkerClientPartialsLookupPropertyTest.class, "/scaffold"));
    }
    
    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_VERSION_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/lookupPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/lookupPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateEnumProperty() throws Exception {
        String enumProperty = "paymentType";
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENUM_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/lookupPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.control-group > div.controls");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements selectElement = nToManyWidgetElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(enumProperty));
        assertThat(selectElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME) + "." + enumProperty));
        String collectionElement = enumProperty.substring(0, 1);
        String optionsExpression = collectionElement +" for "+ collectionElement +" in " + enumProperty + "List";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }

}
