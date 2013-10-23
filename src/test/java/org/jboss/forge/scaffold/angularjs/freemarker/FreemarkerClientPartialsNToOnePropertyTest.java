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
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jboss.forge.scaffoldx.freemarker.TemplateLoaderConfig;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests to verify that the generated HTML for 1:1 and M:1 associations of JPA entities are generated correctly.
 */
public class FreemarkerClientPartialsNToOnePropertyTest {

    private static FreemarkerClient freemarkerClient;

    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null,
                FreemarkerClientPartialsNToOnePropertyTest.class, "/scaffold"));
    }

    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("identifier", "id");
        idProperties.put("label", "Id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToOnePropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("identifier", "id");
        idProperties.put("label", "Id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");

        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToOnePropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }

    @Test
    public void testGenerateOneToOneProperty() throws Exception {
        Map<String, String> voucherProperties = new HashMap<String, String>();
        String oneToOneProperty = "voucher";
        voucherProperties.put("name", oneToOneProperty);
        voucherProperties.put("identifier", oneToOneProperty);
        voucherProperties.put("label", "Voucher");
        voucherProperties.put("type", "com.example.scaffoldtester.model.DiscountVoucher");
        voucherProperties.put("one-to-one", "true");
        voucherProperties.put("simpleType", "DiscountVoucher");
        voucherProperties.put("optionLabel", "id");

        Map<String, Object> root = new HashMap<String, Object>();
        String entityName = "SampleEntity";
        root.put("entityName", entityName);
        root.put("property", voucherProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToOnePropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));

        Elements oneToOneWidgetElement = html.select("div.control-group > div.controls");
        assertThat(oneToOneWidgetElement, notNullValue());

        Elements selectElement = oneToOneWidgetElement.select(" > select");
        assertThat(selectElement, notNullValue());
        assertThat(selectElement.attr("id"), equalTo(oneToOneProperty));
        String collectionElement = oneToOneProperty.substring(0, 1);
        String optionsExpression = collectionElement + ".text for " + collectionElement + " in "
                + oneToOneProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
        assertThat(selectElement.attr("ng-model"), equalTo(oneToOneProperty + "Selection"));
    }

    @Test
    public void testGenerateManyToOneProperty() throws Exception {
        Map<String, String> customerProperties = new HashMap<String, String>();
        String oneToOneProperty = "customer";
        customerProperties.put("name", oneToOneProperty);
        customerProperties.put("identifier", oneToOneProperty);
        customerProperties.put("label", "Customer");
        customerProperties.put("type", "com.example.scaffoldtester.model.Customer");
        customerProperties.put("many-to-one", "true");
        customerProperties.put("simpleType", "Customer");
        customerProperties.put("optionLabel", "id");

        Map<String, Object> root = new HashMap<String, Object>();
        String entityName = "SampleEntity";
        root.put("entityName", entityName);
        root.put("property", customerProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/nToOnePropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));

        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));

        Elements oneToOneWidgetElement = html.select("div.control-group > div.controls");
        assertThat(oneToOneWidgetElement, notNullValue());

        Elements selectElement = oneToOneWidgetElement.select(" > select");
        assertThat(selectElement, notNullValue());
        assertThat(selectElement.attr("id"), equalTo(oneToOneProperty));
        String collectionElement = oneToOneProperty.substring(0, 1);
        String optionsExpression = collectionElement + ".text for " + collectionElement + " in "
                + oneToOneProperty + "SelectionList";
        assertThat(selectElement.attr("ng-options"), equalTo(optionsExpression));
        assertThat(selectElement.attr("ng-model"), equalTo(oneToOneProperty + "Selection"));
    }

}
