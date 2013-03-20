package org.jboss.forge.scaffold.angularjs.freemarker;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.core.IsNull;
import org.jboss.forge.scaffold.angularjs.FreemarkerClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreemarkerClientTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient();
    }
    
    @Test
    public void testGenerateNewEntityController() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("projectId", "testProject");
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "scripts/controllers/newEntityController.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateEditEntityController() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("projectId", "testProject");
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "scripts/controllers/editEntityController.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchEntityController() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("projectId", "testProject");
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "scripts/controllers/searchEntityController.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateEntityFactory() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("projectId", "testProject");
        root.put("entityName", "SampleEntity");
        root.put("resourceRootPath", "rest");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "scripts/services/entityFactory.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateDetailPartial() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "views/detail.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateSearchPartial() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "views/search.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
    @Test
    public void testGenerateIndex() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        List<String> entityNames = new ArrayList<String>();
        entityNames.add("SampleEntity");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        root.put("projectId", "scaffold");
        root.put("projectTitle", "scaffold");
        root.put("properties", entityAttributeProperties);
        String output = freemarkerClient.processFTL(root, "index.html.ftl");
        assertThat(output, IsNull.notNullValue());
    }

    @Test
    public void testGenerateAngularApplication() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, String> versionProperties = new HashMap<String, String>();
        versionProperties.put("name", "version");
        versionProperties.put("hidden", "true");
        versionProperties.put("type", "number");
        
        Map<String, String> sampleAttributeProperties = new HashMap<String, String>();
        sampleAttributeProperties.put("name", "sampleAttribute");
        sampleAttributeProperties.put("type", "java.lang.String");
        
        List<Map<String,String>> entityAttributeProperties = new ArrayList<Map<String,String>>();
        entityAttributeProperties.add(idProperties);
        entityAttributeProperties.add(versionProperties);
        entityAttributeProperties.add(sampleAttributeProperties);
        
        List<String> entityNames = new ArrayList<String>();
        entityNames.add("SampleEntity");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityNames", entityNames);
        root.put("properties", entityAttributeProperties);
        root.put("projectId", "scaffold");
        root.put("projectTitle", "scaffold");
        String output = freemarkerClient.processFTL(root, "scripts/app.js.ftl");
        assertThat(output, IsNull.notNullValue());
    }
    
}
