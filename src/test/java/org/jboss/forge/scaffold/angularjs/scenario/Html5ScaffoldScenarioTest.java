package org.jboss.forge.scaffold.angularjs.scenario;

import static org.jboss.forge.scaffold.angularjs.scenario.TestHelpers.assertStaticFilesAreGenerated;
import static org.jboss.forge.scaffold.angularjs.scenario.TestHelpers.assertWebResourceContents;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.jboss.forge.parser.java.JavaClass;
import org.jboss.forge.project.facets.WebResourceFacet;
import org.jboss.forge.scaffold.angularjs.AbstractHtml5ScaffoldTest;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditAddressView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditGroupIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditStoreOrderView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditUserIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewAddressView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewGroupIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewStoreOrderView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewUserIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchAddressView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchGroupIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchStoreOrderView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchUserIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.manytomany.ManyUserAndManyGroupViewsClient;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.manytoone.ManyStoreOrderAndOneCustomerViewsClient;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.onetomany.OneCustomerAndManyStoreOrderViewsClient;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.onetoone.CustomerAndAddressViewsClient;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.singleentity.CustomerViewClient;
import org.jboss.forge.test.web.DroneTest;
import org.junit.Assert;
import org.junit.Test;

public class Html5ScaffoldScenarioTest extends AbstractHtml5ScaffoldTest {

    @Inject
    protected DroneTest webTest;
    
    @Test
    public void testScaffoldForSingleEntity() throws Exception {
        generateCustomerEntity();

        generateRestResources();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "single-entity");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "single-entity");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "single-entity");
        assertWebResourceContents(web, "/views/Customer/detail.html", "single-entity");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "single-entity");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "single-entity");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "single-entity");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "single-entity");

        verifyBuildWithTest(CustomerViewClient.class, new Class<?>[] { HasLandedOnNewCustomerView.class,
                HasLandedOnEditCustomerView.class, HasLandedOnSearchCustomerView.class });
    }

    @Test
    public void testScaffoldForManyToOneRelation() throws Exception {
        generateCustomerEntity();

        generateStoreOrderEntity();

        generateManyStoreOrderOneCustomerRelation();

        generateRestResources();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "many-to-one");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "many-to-one");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "many-to-one");
        assertWebResourceContents(web, "/views/Customer/detail.html", "many-to-one");
        assertWebResourceContents(web, "/views/StoreOrder/search.html", "many-to-one");
        assertWebResourceContents(web, "/views/StoreOrder/detail.html", "many-to-one");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/newStoreOrderController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editStoreOrderController.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchStoreOrderController.js", "many-to-one");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "many-to-one");
        assertWebResourceContents(web, "/scripts/services/StoreOrderFactory.js", "many-to-one");
        
        verifyBuildWithTest(ManyStoreOrderAndOneCustomerViewsClient.class, new Class<?>[] { HasLandedOnNewCustomerView.class,
                HasLandedOnEditCustomerView.class, HasLandedOnSearchCustomerView.class, HasLandedOnNewStoreOrderView.class,
                HasLandedOnEditStoreOrderView.class, HasLandedOnSearchStoreOrderView.class });
    }

    @Test
    public void testScaffoldForOneToOneRelation() throws Exception {
        generateCustomerEntity();

        generateAddressEntity();

        generateOneCustomerOneAddressRelation();

        generateRestResources();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "one-to-one");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "one-to-one");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "one-to-one");
        assertWebResourceContents(web, "/views/Customer/detail.html", "one-to-one");
        assertWebResourceContents(web, "/views/Address/search.html", "one-to-one");
        assertWebResourceContents(web, "/views/Address/detail.html", "one-to-one");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/newAddressController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/editAddressController.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/controllers/searchAddressController.js", "one-to-one");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "one-to-one");
        assertWebResourceContents(web, "/scripts/services/AddressFactory.js", "one-to-one");
        
        verifyBuildWithTest(CustomerAndAddressViewsClient.class, new Class<?>[] { HasLandedOnNewCustomerView.class,
            HasLandedOnEditCustomerView.class, HasLandedOnSearchCustomerView.class, HasLandedOnNewAddressView.class,
            HasLandedOnEditAddressView.class, HasLandedOnSearchAddressView.class });
    }

    @Test
    public void testScaffoldForOneToManyRelation() throws Exception {
        generateCustomerEntity();

        generateStoreOrderEntity();

        generateOneCustomerManyStoreOrderRelation();

        generateRestResources();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "one-to-many");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "one-to-many");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/Customer/search.html", "one-to-many");
        assertWebResourceContents(web, "/views/Customer/detail.html", "one-to-many");
        assertWebResourceContents(web, "/views/StoreOrder/search.html", "one-to-many");
        assertWebResourceContents(web, "/views/StoreOrder/detail.html", "one-to-many");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newCustomerController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editCustomerController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchCustomerController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/newStoreOrderController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editStoreOrderController.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchStoreOrderController.js", "one-to-many");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/CustomerFactory.js", "one-to-many");
        assertWebResourceContents(web, "/scripts/services/StoreOrderFactory.js", "one-to-many");
        
        verifyBuildWithTest(OneCustomerAndManyStoreOrderViewsClient.class, new Class<?>[] { HasLandedOnNewCustomerView.class,
            HasLandedOnEditCustomerView.class, HasLandedOnSearchCustomerView.class, HasLandedOnNewStoreOrderView.class,
            HasLandedOnEditStoreOrderView.class, HasLandedOnSearchStoreOrderView.class });
    }

    @Test
    public void testScaffoldForManyToManyRelation() throws Exception {
        generateUserIdentityEntity();

        generateGroupIdentityEntity();

        generateManyGroupManyUserRelation();

        generateRestResources();

        generateScaffold();

        WebResourceFacet web = project.getFacet(WebResourceFacet.class);

        // Check if the static assets exist
        assertStaticFilesAreGenerated(web);

        // Check the generated Index page
        assertWebResourceContents(web, "/index.html", "many-to-many");

        // Check the generated Angular Module
        assertWebResourceContents(web, "/scripts/app.js", "many-to-many");

        // Check the generated Angular Views (templates/partials)
        assertWebResourceContents(web, "/views/GroupIdentity/search.html", "many-to-many");
        assertWebResourceContents(web, "/views/GroupIdentity/detail.html", "many-to-many");
        assertWebResourceContents(web, "/views/UserIdentity/search.html", "many-to-many");
        assertWebResourceContents(web, "/views/UserIdentity/detail.html", "many-to-many");

        // Check the generated Angular Controllers
        assertWebResourceContents(web, "/scripts/controllers/newGroupIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editGroupIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchGroupIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/newUserIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/editUserIdentityController.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/controllers/searchUserIdentityController.js", "many-to-many");

        // Check the generated Angular services
        assertWebResourceContents(web, "/scripts/services/GroupIdentityFactory.js", "many-to-many");
        assertWebResourceContents(web, "/scripts/services/UserIdentityFactory.js", "many-to-many");
        
        verifyBuildWithTest(ManyUserAndManyGroupViewsClient.class, new Class<?>[] { HasLandedOnNewUserIdentityView.class,
            HasLandedOnEditUserIdentityView.class, HasLandedOnSearchUserIdentityView.class, HasLandedOnNewGroupIdentityView.class,
            HasLandedOnEditGroupIdentityView.class, HasLandedOnSearchGroupIdentityView.class });
    }

    private void generateCustomerEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named Customer");
        getShell().execute("field string --named firstName");
        getShell().execute("field temporal --type DATE --named dateOfBirth");
        getShell().execute("constraint NotNull --onProperty firstName");
    }

    private void generateStoreOrderEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named StoreOrder");
        getShell().execute("field string --named product");
        getShell().execute("field temporal --type DATE --named orderDate");
        getShell().execute("constraint NotNull --onProperty product");
    }

    private void generateAddressEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named Address");
        getShell().execute("field string --named street");
        getShell().execute("field string --named city");
        getShell().execute("field string --named state");
        getShell().execute("field string --named country");
        getShell().execute("field string --named postalcode");
    }

    private void generateUserIdentityEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named UserIdentity");
        getShell().execute("field string --named userName");
    }

    private void generateGroupIdentityEntity() throws Exception {
        queueInputLines("");
        getShell().execute("entity --named GroupIdentity");
        getShell().execute("field string --named groupName");
    }

    private void generateOneCustomerManyStoreOrderRelation() throws Exception {
        getShell().execute("cd ../Customer.java");
        getShell().execute("field oneToMany --named orders --fieldType com.test.model.StoreOrder.java --fetchType EAGER");
    }

    private void generateOneCustomerOneAddressRelation() throws Exception {
        getShell().execute("cd ../Customer.java");
        getShell().execute("field oneToOne --named shippingAddress --fieldType com.test.model.Address.java");
    }

    private void generateManyStoreOrderOneCustomerRelation() throws Exception {
        getShell().execute("cd ../StoreOrder.java");
        getShell().execute("field manyToOne --named customer --fieldType com.test.model.Customer.java");
    }

    private void generateManyGroupManyUserRelation() throws Exception {
        getShell().execute("cd ../GroupIdentity.java");
        getShell().execute("field manyToMany --named users --fieldType com.test.model.UserIdentity.java --fetchType EAGER");
    }

    private void verifyBuildWithTest(Class<?> testClass, Class<?>[] helpers) throws Exception {
        assertTrue(webTest != null);
        this.webTest.setup(project);
        JavaClass clazz = this.webTest.from(current, testClass);

        this.webTest.buildDefaultDeploymentMethod(project, clazz, Arrays.asList(
                 ".addAsResource(\"META-INF/persistence.xml\", \"META-INF/persistence.xml\")"
                 ));
        this.webTest.addAsTestClass(project, clazz);
        List<JavaClass> classes = new ArrayList<JavaClass>();
        for(Class<?> helper: helpers) {
            JavaClass helperClass = this.webTest.from(current, helper);
            classes.add(helperClass);
        }
        this.webTest.addHelpers(project, classes.toArray(new JavaClass[0]));

        try
        {
           getShell().execute("build -X install");
        }
        catch (Exception e)
        {
           System.err.println(getOutput());
           throw e;
        }
    }

}
