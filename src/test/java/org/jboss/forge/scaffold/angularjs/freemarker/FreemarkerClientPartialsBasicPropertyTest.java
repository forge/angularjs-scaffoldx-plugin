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
 * Tests to verify that the generated HTML for basic properties of JPA entities are generated correctly.
 */
public class FreemarkerClientPartialsBasicPropertyTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null, FreemarkerClientPartialsBasicPropertyTest.class,
                "/scaffold"));
    }
    
    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_VERSION_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateBasicStringProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, BASIC_STRING_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"fullName"));
    }

    @Test
    public void testGenerateBasicStringPropertyWithMaxlength() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, STRING_PROP_WITH_MAX_LEN);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"fullName"));
        assertThat(formInputElement.attr("ng-maxlength"), equalTo("100"));
    }
    
    @Test
    public void testGenerateBasicStringPropertyWithMinlength() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, STRING_PROP_WITH_MIN_LEN);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"fullName"));
        assertThat(formInputElement.attr("ng-minlength"), equalTo("5"));
    }
    
    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, NUMBER_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("score"));
        assertThat(formInputElement.attr("type"), equalTo("number"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"score"));
    }
    
    @Test
    public void testGenerateBasicNumberPropertyWithMinConstraint() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, NUMBER_PROP_WITH_MIN_VAL);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("score"));
        assertThat(formInputElement.attr("type"), equalTo("number"));
        assertThat(formInputElement.attr("min"), equalTo("0"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"score"));
    }
    
    @Test
    public void testGenerateBasicNumberPropertyWithMaxConstraint() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, NUMBER_PROP_WITH_MAX_VAL);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("score"));
        assertThat(formInputElement.attr("type"), equalTo("number"));
        assertThat(formInputElement.attr("max"), equalTo("100"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"score"));
    }
    
    @Test
    public void testGenerateBasicDateProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, DATE_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group date");
        assertThat(formInputElement.attr("id"), equalTo("dateOfBirth"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"dateOfBirth"));
    }
    
    @Test
    public void testGenerateBasicTimeProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, TIME_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group time");
        assertThat(formInputElement.attr("id"), equalTo("alarmTime"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"alarmTime"));
    }
    
    @Test
    public void testGenerateBasicDatetimeProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, DATETIME_PROP);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group datetime");
        assertThat(formInputElement.attr("id"), equalTo("auditTimestamp"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"auditTimestamp"));
    }
    
    @Test
    public void testGenerateBasicBooleanProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, BOOLEAN_PROP);
        
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.form-group input");
        assertThat(formInputElement.attr("id"), equalTo("optForMail"));
        assertThat(formInputElement.attr("type"), equalTo("checkbox"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase(ENTITY_NAME)+"."+"optForMail"));
    }

}
