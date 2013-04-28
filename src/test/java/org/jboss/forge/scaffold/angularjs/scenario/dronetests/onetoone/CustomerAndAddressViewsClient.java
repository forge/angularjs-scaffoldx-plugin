package org.jboss.forge.scaffold.angularjs.scenario.dronetests.onetoone;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditAddressView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewAddressView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchAddressView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchCustomerView;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

@Ignore
@RunWith(Arquillian.class)
@RunAsClient
public class CustomerAndAddressViewsClient {

    @Drone
    WebDriver driver;
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    @InSequence(1)
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
    @InSequence(2)
    public void testSaveNewAddress(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the Address nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("Addresss")).click();
        wait.until(new HasLandedOnSearchAddressView());
        
        // Choose to create a new address
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewAddressView());
        
        // Enter the address details and save
        driver.findElement(By.id("street")).clear();
        driver.findElement(By.id("street")).sendKeys("6747 Crystal Limits");
        driver.findElement(By.id("city")).clear();
        driver.findElement(By.id("city")).sendKeys("Markinch");
        driver.findElement(By.id("state")).clear();
        driver.findElement(By.id("state")).sendKeys("Mississippi");
        driver.findElement(By.id("country")).clear();
        driver.findElement(By.id("country")).sendKeys("USA");
        driver.findElement(By.id("postalcode")).clear();
        driver.findElement(By.id("postalcode")).sendKeys("39512-8569");
        driver.findElement(By.id("saveAddress")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditAddressView());
        assertEquals(baseUrl.toString() + "app.html#/Addresss/edit/2", driver.getCurrentUrl());
        assertEquals("6747 Crystal Limits", driver.findElement(By.id("street")).getAttribute("value"));
        assertEquals("Markinch", driver.findElement(By.id("city")).getAttribute("value"));
        assertEquals("Mississippi", driver.findElement(By.id("state")).getAttribute("value"));
        assertEquals("USA", driver.findElement(By.id("country")).getAttribute("value"));
        assertEquals("39512-8569", driver.findElement(By.id("postalcode")).getAttribute("value"));
        
        // Browse to search address view and verify if searching for the address works 
        driver.findElement(By.id("cancel")).click();
        wait.until(new HasLandedOnSearchAddressView());
        driver.findElement(By.id("street")).clear();
        driver.findElement(By.id("street")).sendKeys("6747");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        List<WebElement> searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
        
        // Browse to the edit View of a search result and verify if the details are displayed
        driver.findElement(By.xpath("id(\"search-results-body\")/tr[1]/td[1]/a")).click();
        wait.until(new HasLandedOnEditAddressView());
        assertEquals("6747 Crystal Limits", driver.findElement(By.id("street")).getAttribute("value"));
        assertEquals("Markinch", driver.findElement(By.id("city")).getAttribute("value"));
        assertEquals("Mississippi", driver.findElement(By.id("state")).getAttribute("value"));
        assertEquals("USA", driver.findElement(By.id("country")).getAttribute("value"));
        assertEquals("39512-8569", driver.findElement(By.id("postalcode")).getAttribute("value"));
        
        // Edit the details, save and reverify the details
        driver.findElement(By.id("street")).clear();
        driver.findElement(By.id("street")).sendKeys("6017 Thunder Way");
        driver.findElement(By.id("city")).clear();
        driver.findElement(By.id("city")).sendKeys("Skookumchuck");
        driver.findElement(By.id("postalcode")).clear();
        driver.findElement(By.id("postalcode")).sendKeys("39200-1131");
        driver.findElement(By.id("saveAddress")).click();
        wait.until(new HasLandedOnEditAddressView());
        assertEquals("6017 Thunder Way", driver.findElement(By.id("street")).getAttribute("value"));
        assertEquals("Skookumchuck", driver.findElement(By.id("city")).getAttribute("value"));
        assertEquals("Mississippi", driver.findElement(By.id("state")).getAttribute("value"));
        assertEquals("USA", driver.findElement(By.id("country")).getAttribute("value"));
        assertEquals("39200-1131", driver.findElement(By.id("postalcode")).getAttribute("value"));
    }
    
    @Test
    @InSequence(3)
    public void testEstablishCustomerAddressRelation(@ArquillianResource URL baseUrl) throws Exception {
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
        WebElement addressElement = driver.findElement(By.id("shippingAddress"));
        Select addresses = new Select(addressElement);
        addresses.selectByVisibleText("2");
        driver.findElement(By.id("saveCustomer")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditCustomerView());
        assertEquals(baseUrl.toString() + "app.html#/Customers/edit/3", driver.getCurrentUrl());
        assertEquals("John Doe", driver.findElement(By.id("firstName")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("dateOfBirth")).getAttribute("value"));
        addressElement = driver.findElement(By.id("shippingAddress"));
        addresses = new Select(addressElement);
        assertEquals("2", addresses.getFirstSelectedOption().getText());
        
        // Browse to the search view and verify if searching through HTML dropdowns work
        driver.findElement(By.id("cancel")).click();
        wait.until(new HasLandedOnSearchCustomerView());
        addressElement = driver.findElement(By.id("shippingAddress"));
        addresses = new Select(addressElement);
        addresses.selectByVisibleText("2");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        List<WebElement> searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
    }
    
}
