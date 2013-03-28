package org.jboss.forge.scaffold.angularjs.freemarker;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreemarkerClientPartialsSearchInputTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(null, FreemarkerClientPartialsSearchInputTest.class, "/scaffold");
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
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
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
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateOneToManyProperty() throws Exception {
        Map<String, String> ordersProperties = new HashMap<String, String>();
        String oneToManyProperty = "orders";
        ordersProperties.put("name", oneToManyProperty);
        ordersProperties.put("type", "java.lang.String");
        ordersProperties.put("n-to-many", "true");
        ordersProperties.put("parameterized-type", "com.example.scaffoldtester.model.StoreOrder");
        ordersProperties.put("type", "java.util.Set");
        ordersProperties.put("simpleType", "StoreOrder");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", ordersProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(""));
    }
    
    @Test
    public void testGenerateManyToManyProperty() throws Exception {
        Map<String, String> usersProperties = new HashMap<String, String>();
        String manyToManyProperty = "users";
        usersProperties.put("name", manyToManyProperty);
        usersProperties.put("type", "java.lang.String");
        usersProperties.put("n-to-many", "true");
        usersProperties.put("parameterized-type", "com.example.scaffoldtester.model.UserIdentity");
        usersProperties.put("type", "java.util.Set");
        usersProperties.put("simpleType", "UserIdentity");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", usersProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
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
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        
        Elements formInputElement = container.select("div.controls > input");
        assertThat(formInputElement.attr("id"), equalTo("fullName"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search"+"."+"fullName"));
    }
    
    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, String> ageProperties = new HashMap<String, String>();
        ageProperties.put("name", "age");
        ageProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", ageProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        
        Elements formInputElement = container.select("div.controls > input");
        assertThat(formInputElement.attr("id"), equalTo("age"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search"+"."+"age"));
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
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        
        Elements formInputElement = container.select("div.controls > input");
        assertThat(formInputElement.attr("id"), equalTo("dateOfBirth"));
        assertThat(formInputElement.attr("type"), equalTo("text"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search"+"."+"dateOfBirth"));
    }
    
    @Test
    public void testGenerateOneToOneProperty() throws Exception {
        Map<String, String> voucherProperties = new HashMap<String, String>();
        String oneToOneProperty = "voucher";
        voucherProperties.put("name", oneToOneProperty);
        voucherProperties.put("type", "com.example.scaffoldtester.model.DiscountVoucher");
        voucherProperties.put("one-to-one", "true");
        voucherProperties.put("simpleType", "DiscountVoucher");
        voucherProperties.put("optionLabel", "id");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", voucherProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        
        Elements formInputElement = container.select("div.controls > select");
        assertThat(formInputElement.attr("id"), equalTo("voucher"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search"+"."+"voucher"));
    }
    
    @Test
    public void testGenerateManyToOneProperty() throws Exception {
        Map<String, String> customerProperties = new HashMap<String, String>();
        String oneToOneProperty = "customer";
        customerProperties.put("name", oneToOneProperty);
        customerProperties.put("type", "com.example.scaffoldtester.model.Customer");
        customerProperties.put("many-to-one", "true");
        customerProperties.put("simpleType", "Customer");
        customerProperties.put("optionLabel", "id");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", customerProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchFormInput.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements container = html.select("div.control-group");
        assertThat(container, notNullValue());
        
        Elements formInputElement = container.select("div.controls > select");
        assertThat(formInputElement.attr("id"), equalTo("customer"));
        assertThat(formInputElement.attr("ng-model"), equalTo("search"+"."+"customer"));
    }

}
