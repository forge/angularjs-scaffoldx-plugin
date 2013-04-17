package org.jboss.forge.scaffold.angularjs.scenario.dronetests.singleentityvalidations;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchCustomerView;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

@Ignore
@RunWith(Arquillian.class)
@RunAsClient
public class CustomerViewWithValidationsClient {

    @Drone
    WebDriver driver;
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    public void testSaveNewCustomer(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the Customers nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("Customers")).click();
        wait.until(new HasLandedOnSearchCustomerView());
        
        // Choose to create a new customer 
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewCustomerView());
        
        // Enter the customer details and save
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("John Doe");
        driver.findElement(By.id("dateOfBirth")).clear();
        driver.findElement(By.id("dateOfBirth")).sendKeys("2013-01-10");
        driver.findElement(By.id("ficoCreditScore")).clear();
        driver.findElement(By.id("ficoCreditScore")).sendKeys("700");
        driver.findElement(By.id("saveCustomer")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditCustomerView());
        assertEquals(baseUrl.toString() + "app.html#/Customers/edit/1", driver.getCurrentUrl());
        assertEquals("John Doe", driver.findElement(By.id("firstName")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("dateOfBirth")).getAttribute("value"));
        
        // Browse to search customer view and verify if searching for the customer works 
        driver.findElement(By.id("cancel")).click();
        wait.until(new HasLandedOnSearchCustomerView());
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("John");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        List<WebElement> searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
        
        // Browse to the edit View of a search result and verify if the details are displayed
        driver.findElement(By.xpath("id(\"search-results-body\")/tr[1]/td[1]/a")).click();
        wait.until(new HasLandedOnEditCustomerView());
        assertEquals("John Doe", driver.findElement(By.id("firstName")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("dateOfBirth")).getAttribute("value"));
        
        // Edit the details, save and reverify the details
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("Jane Doe");
        driver.findElement(By.id("saveCustomer")).click();
        wait.until(new HasLandedOnEditCustomerView());
        assertEquals("Jane Doe", driver.findElement(By.id("firstName")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("dateOfBirth")).getAttribute("value"));
    }
    
    @Test
    public void testSaveNewCustomerWithoutRequiredFields(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the Customers nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("Customers")).click();
        wait.until(new HasLandedOnSearchCustomerView());
        
        // Choose to create a new customer 
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewCustomerView());
        
        // Enter the customer details and save
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("dateOfBirth")).clear();
        driver.findElement(By.id("ficoCreditScore")).clear();
        
        assertTrue(isBootstrapErrorDisplayedForFormControl("firstNameControls", "required"));
        assertTrue(isBootstrapErrorDisplayedForFormControl("ficoCreditScoreControls", "required"));
    }
    
    @Test
    public void testSaveNewCustomerWithInvalidLengths(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the Customers nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("Customers")).click();
        wait.until(new HasLandedOnSearchCustomerView());
        
        // Choose to create a new customer 
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewCustomerView());
        
        // Enter the customer details
        driver.findElement(By.id("firstName")).clear();
        driver.findElement(By.id("firstName")).sendKeys("A");
        
        assertTrue(isBootstrapErrorDisplayedForFormControl("firstNameControls", "minimum length is 3"));
        
        driver.findElement(By.id("firstName")).clear();
        String TWO_HUNDRED_As = new String(new char[200]).replaceAll("\0", "A");
        driver.findElement(By.id("firstName")).sendKeys(TWO_HUNDRED_As);

        assertTrue(isBootstrapErrorDisplayedForFormControl("firstNameControls", "maximum length is 100"));
    }
    
    @Test
    public void testSaveNewCustomerWithInvalidNumbers(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the Customers nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("Customers")).click();
        wait.until(new HasLandedOnSearchCustomerView());
        
        // Choose to create a new customer 
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewCustomerView());
        
        // Enter the customer details
        driver.findElement(By.id("ficoCreditScore")).clear();
        driver.findElement(By.id("ficoCreditScore")).sendKeys("A");
        assertTrue(isBootstrapErrorDisplayedForFormControl("ficoCreditScoreControls", "not a number"));
        
        // Enter the customer details
        driver.findElement(By.id("ficoCreditScore")).clear();
        driver.findElement(By.id("ficoCreditScore")).sendKeys("200");
        assertTrue(isBootstrapErrorDisplayedForFormControl("ficoCreditScoreControls", "minimum allowed is 300"));
        
        // Enter the customer details
        driver.findElement(By.id("ficoCreditScore")).clear();
        driver.findElement(By.id("ficoCreditScore")).sendKeys("1000");
        assertTrue(isBootstrapErrorDisplayedForFormControl("ficoCreditScoreControls", "maximum allowed is 850"));
    }

    private boolean isBootstrapErrorDisplayedForFormControl(String formElementId, String message) {
        for (WebElement element : driver.findElements(By.xpath("id('" + formElementId + "')/span"))) {
            if (element.isDisplayed()) {
                return element.getText().equals(message);
            }
        }
        return false;
    }
    
}
