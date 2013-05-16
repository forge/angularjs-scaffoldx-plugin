/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs.scenario.dronetests.manytoonerequired;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditStoreOrderView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewStoreOrderView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchCustomerView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchStoreOrderView;
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
public class ManyStoreOrderAndOneRequiredCustomerViewsClient {

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
    public void testSaveNewStoreOrder(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the StoreOrders nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("StoreOrders")).click();
        wait.until(new HasLandedOnSearchStoreOrderView());
        
        // Choose to create a new store order
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewStoreOrderView());
        
        // Enter the store order details
        driver.findElement(By.id("product")).clear();
        driver.findElement(By.id("product")).sendKeys("Apples");
        driver.findElement(By.id("orderDate")).clear();
        driver.findElement(By.id("orderDate")).sendKeys("2013-01-10");
        
        // Verify that the customer field is marked as required, before filling it. 
        isBootstrapErrorDisplayedForFormControl("customerControls", "required");
        
        // Establish the relationship between the store order and the existing customer.
        WebElement customerElement = driver.findElement(By.id("customer"));
        Select customers = new Select(customerElement);
        customers.selectByVisibleText("1");
        
        // Save
        driver.findElement(By.id("saveStoreOrder")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditStoreOrderView());
        assertEquals(baseUrl.toString() + "app.html#/StoreOrders/edit/2", driver.getCurrentUrl());
        assertEquals("Apples", driver.findElement(By.id("product")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("orderDate")).getAttribute("value"));
        customerElement = driver.findElement(By.id("customer"));
        customers = new Select(customerElement);
        assertEquals("1", customers.getFirstSelectedOption().getText());
        
        // Browse to search store order view and verify if searching for the store order works 
        driver.findElement(By.id("cancel")).click();
        wait.until(new HasLandedOnSearchStoreOrderView());
        driver.findElement(By.id("product")).clear();
        driver.findElement(By.id("product")).sendKeys("Apple");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        List<WebElement> searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
        
        // Verify if searching for the store order through HTML dropdowns work
        driver.findElement(By.id("product")).clear();
        customerElement = driver.findElement(By.id("customer"));
        customers = new Select(customerElement);
        customers.selectByVisibleText("1");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
        
        // Browse to the edit View of a search result and verify if the details are displayed
        driver.findElement(By.xpath("id(\"search-results-body\")/tr[1]/td[1]/a")).click();
        wait.until(new HasLandedOnEditStoreOrderView());
        assertEquals("Apples", driver.findElement(By.id("product")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("orderDate")).getAttribute("value"));
        
        // Edit the details, save and reverify the details
        driver.findElement(By.id("product")).clear();
        driver.findElement(By.id("product")).sendKeys("Oranges");
        driver.findElement(By.id("saveStoreOrder")).click();
        wait.until(new HasLandedOnEditStoreOrderView());
        assertEquals("Oranges", driver.findElement(By.id("product")).getAttribute("value"));
        assertEquals("2013-01-10", driver.findElement(By.id("orderDate")).getAttribute("value"));
        customerElement = driver.findElement(By.id("customer"));
        customers = new Select(customerElement);
        assertEquals("1", customers.getFirstSelectedOption().getText());
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
