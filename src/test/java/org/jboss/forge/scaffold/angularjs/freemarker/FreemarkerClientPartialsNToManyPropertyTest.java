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

public class FreemarkerClientPartialsNToManyPropertyTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null,
                FreemarkerClientPartialsNToManyPropertyTest.class, "/scaffold"));
    }
    
    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_VERSION_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        String oneToManyProperty = "orders";
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ONE_TO_MANY_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.control-group > div.controls");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements selectElement = nToManyWidgetElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(oneToManyProperty));
        assertThat(selectElement.attr("multiple"), notNullValue());
        assertThat(selectElement.attr("ng-model"), equalTo(oneToManyProperty+"Selection"));
        String collectionElement = oneToManyProperty.substring(0, 1);
        String optionsExpression = collectionElement +".text for "+ collectionElement +" in " + oneToManyProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }
    
    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        String manyToManyProperty = "users";
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, MANY_TO_MANY_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/nToManyPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements nToManyWidgetElement = html.select("div.control-group > div.controls");
        assertThat(nToManyWidgetElement, notNullValue());

        Elements selectElement = nToManyWidgetElement.select(" > select");
        assertThat(selectElement.attr("id"), equalTo(manyToManyProperty));
        assertThat(selectElement.attr("multiple"), notNullValue());
        assertThat(selectElement.attr("ng-model"), equalTo(manyToManyProperty+"Selection"));
        String collectionElement = manyToManyProperty.substring(0, 1);
        String optionsExpression = collectionElement +".text for "+ collectionElement +" in " + manyToManyProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
    }

}
