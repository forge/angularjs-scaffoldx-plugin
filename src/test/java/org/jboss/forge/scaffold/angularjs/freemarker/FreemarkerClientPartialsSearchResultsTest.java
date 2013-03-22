package org.jboss.forge.scaffold.angularjs.freemarker;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.forge.scaffold.angularjs.FreemarkerClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreemarkerClientPartialsSearchResultsTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(null);
    }
    
    @Test
    public void testGenerateHiddenProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(idProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
    }
    
    @Test
    public void testGenerateHiddenAndRequiredProperty() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("required", "true");
        idProperties.put("type", "number");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(idProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
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
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(ordersProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
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
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(usersProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(0, equalTo(headers.size()));
    }
    
    @Test
    public void testGenerateBasicStringProperty() throws Exception {
        Map<String, String> nameProperties = new HashMap<String, String>();
        nameProperties.put("name", "fullName");
        nameProperties.put("type", "java.lang.String");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(nameProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("FullName"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntitys"+ "/edit/{{result.id}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.fullName}}"));
    }
    
    @Test
    public void testGenerateBasicNumberProperty() throws Exception {
        Map<String, String> ageProperties = new HashMap<String, String>();
        ageProperties.put("name", "age");
        ageProperties.put("type", "number");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(ageProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Age"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntitys"+ "/edit/{{result.id}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.age}}"));
    }
    
    @Test
    public void testGenerateBasicDateProperty() throws Exception {
        Map<String, String> dateOfBirthProperties = new HashMap<String, String>();
        dateOfBirthProperties.put("name", "dateOfBirth");
        dateOfBirthProperties.put("type","java.util.Date");
        dateOfBirthProperties.put("datetime-type", "date");
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(dateOfBirthProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("DateOfBirth"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntitys"+ "/edit/{{result.id}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.dateOfBirth}}"));
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
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(voucherProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Voucher"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntitys"+ "/edit/{{result.id}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.voucher.id}}"));
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
        
        List<Map<String,? extends Object>> properties = new ArrayList<Map<String,? extends Object>>();
        properties.add(customerProperties);
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("properties", properties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResults.html.ftl");
        Document html = Jsoup.parseBodyFragment(output);
        assertThat(output.trim(), not(equalTo("")));
        
        Elements headers = html.select("table > thead > tr > th");
        assertThat(headers.size(), equalTo(1));
        assertThat(headers.text(), equalTo("Customer"));
        
        Elements resultRows = html.select("table > tbody > tr");
        assertThat(resultRows.attr("ng-repeat"), containsString("result in searchResults"));
        
        Elements resultCells = resultRows.select(" > td");
        assertThat(resultCells.size(), equalTo(1));
        assertThat(resultCells.select("a").attr("href"), equalTo("#/"+"SampleEntitys"+ "/edit/{{result.id}}"));
        assertThat(resultCells.select("a").text(), equalTo("{{result.customer.id}}"));
    }

}
