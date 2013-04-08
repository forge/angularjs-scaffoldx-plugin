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
import org.metawidget.util.simple.StringUtils;

public class FreemarkerClientPartialsBasicPropertyTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(new TemplateLoaderConfig(null, FreemarkerClientPartialsBasicPropertyTest.class,
                "/scaffold"));
    }
    
    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateBasicStringProperty() throws Exception {
        Map<String, String> nameProperties = new HashMap<String, String>();
        nameProperties.put("name", "fullName");
        nameProperties.put("type", "java.lang.String");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", nameProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"fullName"));
    }
    
    @Test
    public void testGenerateBasicStringPropertyWithMaxlength() throws Exception {
        Map<String, String> nameProperties = new HashMap<String, String>();
        nameProperties.put("name", "fullName");
        nameProperties.put("type", "java.lang.String");
        nameProperties.put("maximum-length", "100");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", nameProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"fullName"));
        assertThat(formInputElement.attr("ng-maxlength"), equalTo("100"));
    }
    
    @Test
    public void testGenerateBasicStringPropertyWithMinlength() throws Exception {
        Map<String, String> nameProperties = new HashMap<String, String>();
        nameProperties.put("name", "fullName");
        nameProperties.put("type", "java.lang.String");
        nameProperties.put("minimum-length", "5");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", nameProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"fullName"));
        assertThat(formInputElement.attr("ng-minlength"), equalTo("5"));
    }
    
    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, String> ageProperties = new HashMap<String, String>();
        ageProperties.put("name", "age");
        ageProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", ageProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("age"));
        assertThat(formInputElement.attr("type"), equalTo("number"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"age"));
    }
    
    @Test
    public void testGenerateBasicNumberPropertyWithMinConstraint() throws Exception {
        Map<String, String> ageProperties = new HashMap<String, String>();
        ageProperties.put("name", "age");
        ageProperties.put("type", "number");
        ageProperties.put("minimum-value","0");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", ageProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("age"));
        assertThat(formInputElement.attr("type"), equalTo("number"));
        assertThat(formInputElement.attr("min"), equalTo("0"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"age"));
    }
    
    @Test
    public void testGenerateBasicNumberPropertyWithMaxConstraint() throws Exception {
        Map<String, String> scoreProperties = new HashMap<String, String>();
        scoreProperties.put("name", "score");
        scoreProperties.put("type", "number");
        scoreProperties.put("maximum-value","100");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", scoreProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("score"));
        assertThat(formInputElement.attr("type"), equalTo("number"));
        assertThat(formInputElement.attr("max"), equalTo("100"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"score"));
    }
    
    @Test
    public void testGenerateBasicDateProperty() throws Exception {
        Map<String, String> dateOfBirthProperties = new HashMap<String, String>();
        dateOfBirthProperties.put("name", "dateOfBirth");
        dateOfBirthProperties.put("type","java.util.Date");
        dateOfBirthProperties.put("datetime-type", "date");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", dateOfBirthProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("dateOfBirth"));
        assertThat(formInputElement.attr("type"), equalTo("date"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"dateOfBirth"));
    }
    
    @Test
    public void testGenerateBasicTimeProperty() throws Exception {
        Map<String, String> alarmTimeProperties = new HashMap<String, String>();
        alarmTimeProperties.put("name", "alarmTime");
        alarmTimeProperties.put("type","java.util.Date");
        alarmTimeProperties.put("datetime-type", "time");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", alarmTimeProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("alarmTime"));
        assertThat(formInputElement.attr("type"), equalTo("time"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"alarmTime"));
    }
    
    @Test
    public void testGenerateBasicDatetimeProperty() throws Exception {
        Map<String, String> auditTimestampProperties = new HashMap<String, String>();
        auditTimestampProperties.put("name", "auditTimestamp");
        auditTimestampProperties.put("type","java.util.Date");
        auditTimestampProperties.put("datetime-type", "both");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", auditTimestampProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("auditTimestamp"));
        assertThat(formInputElement.attr("type"), equalTo("datetime"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"auditTimestamp"));
    }
    
    @Test
    public void testGenerateBasicBooleanProperty() throws Exception {
        Map<String, String> nameProperties = new HashMap<String, String>();
        nameProperties.put("name", "optForMail");
        nameProperties.put("type", "boolean");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", nameProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/basicPropertyDetail.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        assertThat(container.attr("ng-class"), not(equalTo("")));
        
        Elements formInputElement = html.select("div.control-group input");
        assertThat(formInputElement.attr("id"), equalTo("optForMail"));
        assertThat(formInputElement.attr("type"), equalTo("checkbox"));
        assertThat(formInputElement.attr("ng-model"), equalTo(StringUtils.camelCase("SampleEntity")+"."+"optForMail"));
    }

}
