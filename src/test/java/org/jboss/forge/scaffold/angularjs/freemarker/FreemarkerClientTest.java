/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs.freemarker;

import static org.jboss.forge.scaffold.angularjs.TestHelpers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.core.IsNull;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.freemarker.TemplateLoaderConfig;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests to verify that Freemarker templates that generate JavaScript work. Verifies that the templates dont error out during
 * processing. Functional tests verify whether the generated JavaScript actually work.
 */
public class FreemarkerClientTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null, FreemarkerClient.class, "/scaffold"));
    }
    
    @Test
    public void testGenerateNewEntityController() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);
        
        String output = freemarkerClient.processFTL(root, "scripts/controllers/newEntityController.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateEditEntityController() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);
        
        String output = freemarkerClient.processFTL(root, "scripts/controllers/editEntityController.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchEntityController() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);
        
        String output = freemarkerClient.processFTL(root, "scripts/controllers/searchEntityController.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateEntityFactory() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);
        
        String output = freemarkerClient.processFTL(root, "scripts/services/entityFactory.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }

    @Test
    public void testGenerateDetailPartial() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);
        
        String output = freemarkerClient.processFTL(root, "views/detail.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchPartial() throws Exception {
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(ENTITY_ID_PROP);
        entityAttributeProperties.add(ENTITY_VERSION_PROP);
        entityAttributeProperties.add(BASIC_STRING_PROP);
        Map<String, Object> root = createEntityRootmap(entityAttributeProperties);
        
        String output = freemarkerClient.processFTL(root, "views/search.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateIndex() throws Exception {
        Map<String, Object> root = createGlobalRootmap();
        
        String output = freemarkerClient.processFTL(root, "index.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }

    @Test
    public void testGenerateAngularApplication() throws Exception {
        Map<String, Object> root = createGlobalRootmap();
        
        String output = freemarkerClient.processFTL(root, "scripts/app.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
}
