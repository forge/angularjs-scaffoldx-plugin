package org.jboss.forge.scaffold.angularjs.freemarker;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.jboss.forge.scaffoldx.freemarker.FreemarkerClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class FreemarkerClientPartialsSearchResultsPaginatorTest {

    private static FreemarkerClient freemarkerClient;
    
    @BeforeClass
    public static void setupClass() throws Exception {
        freemarkerClient = new FreemarkerClient(null, FreemarkerClientPartialsSearchResultsPaginatorTest.class, "/scaffold");
    }
    
    private static String PAGINATOR_OUTPUT = "<div class=\"pagination pagination-centered\">\n" + 
    		"    <ul>\n" + 
    		"        <li ng-class=\"{disabled:currentPage == 0}\">\n" + 
    		"            <a id=\"prev\" href ng-click=\"previous()\">«</a>\n" + 
    		"        </li>\n" + 
    		"        <li ng-repeat=\"n in pageRange\" ng-class=\"{active:currentPage == n}\" ng-click=\"setPage(n)\">\n" + 
    		"            <a href ng-bind=\"n + 1\">1</a>\n" + 
    		"        </li>\n" + 
    		"        <li ng-class=\"{disabled: currentPage == (numberOfPages() - 1)}\">\n" + 
    		"            <a id=\"next\" href ng-click=\"next()\">»</a>\n" + 
    		"        </li>\n" + 
    		"    </ul>\n" + 
    		"</div>";
    
    @Test
    public void testGenerateSearchResultsPaginator() throws Exception {
        Map<String, String> idProperties = new HashMap<String, String>();
        idProperties.put("name", "id");
        idProperties.put("hidden", "true");
        idProperties.put("type", "number");
        
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("entityName", "SampleEntity");
        root.put("property", idProperties);
        String output = freemarkerClient.processFTL(root, "views/includes/searchResultsPaginator.html.ftl");
        assertThat(output.trim(), IsEqual.equalTo(PAGINATOR_OUTPUT));
    }

}
