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
import org.jboss.forge.scaffold.angularjs.TestHelpers;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.freemarker.TemplateLoaderConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests to verify that the generated HTML of the search page is generated correctly.
 */
public class FreemarkerClientPartialsSearchInputTest {

    private static FreemarkerClient freemarkerClient;

    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null, FreemarkerClientPartialsSearchInputTest.class,
                "/scaffold"));
    }

    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_VERSION_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, Object> root = createInspectionResultWrapper(ENTITY_NAME, ENTITY_ID_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, ONE_TO_MANY_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, MANY_TO_MANY_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateBasicStringProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, BASIC_STRING_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "fullName"));
    }

    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, NUMBER_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > input");
        assertThat(formInputElement.attr("id"), equalTo("score"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "score"));
    }

    @Test
    public void testGenerateBasicDateProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, DATE_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > input");
        assertThat(formInputElement.attr("id"), equalTo("dateOfBirth"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "dateOfBirth"));
    }

    @Test
    public void testGenerateOneToOneProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, ONE_TO_ONE_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > select");
        assertThat(formInputElement.attr("id"), equalTo("voucher"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "voucher"));
    }

    @Test
    public void testGenerateManyToOneProperty() throws Exception {
        Map<String, Object> root = TestHelpers.createInspectionResultWrapper(ENTITY_NAME, MANY_TO_ONE_PROP);

        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.form-group");
        assertThat(container, notNullValue());

        Elements formInputElement = container.select("div.col-sm-10 > select");
        assertThat(formInputElement.attr("id"), equalTo("customer"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search" + "." + "customer"));
    }

}
