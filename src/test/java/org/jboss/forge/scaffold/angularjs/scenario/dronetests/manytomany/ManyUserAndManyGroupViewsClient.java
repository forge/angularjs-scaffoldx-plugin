/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs.scenario.dronetests.manytomany;

import static org.junit.Assert.*;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditGroupIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnEditUserIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewGroupIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnNewUserIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchGroupIdentityView;
import org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers.HasLandedOnSearchUserIdentityView;
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
public class ManyUserAndManyGroupViewsClient {

    @Drone
    WebDriver driver;
    
    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    @InSequence(1)
    public void testSaveNewGroupIdentity(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the GroupIdentitys nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("GroupIdentitys")).click();
        wait.until(new HasLandedOnSearchGroupIdentityView());
        
        // Choose to create a new group 
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewGroupIdentityView());
        
        // Enter the group details and save
        driver.findElement(By.id("groupName")).clear();
        driver.findElement(By.id("groupName")).sendKeys("G1");
        driver.findElement(By.id("saveGroupIdentity")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditGroupIdentityView());
        assertEquals(baseUrl.toString() + "app.html#/GroupIdentitys/edit/1", driver.getCurrentUrl());
        assertEquals("G1", driver.findElement(By.id("groupName")).getAttribute("value"));
        
        // Browse to search group view and verify if searching for the group works 
        driver.findElement(By.id("cancel")).click();
        wait.until(new HasLandedOnSearchGroupIdentityView());
        driver.findElement(By.id("groupName")).clear();
        driver.findElement(By.id("groupName")).sendKeys("G");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        List<WebElement> searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
        
        // Browse to the edit View of a search result and verify if the details are displayed
        driver.findElement(By.xpath("id(\"search-results-body\")/tr[1]/td[1]/a")).click();
        wait.until(new HasLandedOnEditGroupIdentityView());
        assertEquals("G1", driver.findElement(By.id("groupName")).getAttribute("value"));
        
        // Edit the details, save and reverify the details
        driver.findElement(By.id("groupName")).clear();
        driver.findElement(By.id("groupName")).sendKeys("G-One");
        driver.findElement(By.id("saveGroupIdentity")).click();
        wait.until(new HasLandedOnEditGroupIdentityView());
        assertEquals("G-One", driver.findElement(By.id("groupName")).getAttribute("value"));
    }
    
    @Test
    @InSequence(2)
    public void testSaveNewUserIdentity(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the UserIdentitys nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("UserIdentitys")).click();
        wait.until(new HasLandedOnSearchUserIdentityView());
        
        // Choose to create a new user
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewUserIdentityView());
        
        // Enter the user details and save
        driver.findElement(By.id("userName")).clear();
        driver.findElement(By.id("userName")).sendKeys("U1");
        driver.findElement(By.id("saveUserIdentity")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditUserIdentityView());
        assertEquals(baseUrl.toString() + "app.html#/UserIdentitys/edit/2", driver.getCurrentUrl());
        assertEquals("U1", driver.findElement(By.id("userName")).getAttribute("value"));
        
        // Browse to search customer view and verify if searching for the customer works 
        driver.findElement(By.id("cancel")).click();
        wait.until(new HasLandedOnSearchUserIdentityView());
        driver.findElement(By.id("userName")).clear();
        driver.findElement(By.id("userName")).sendKeys("U");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("id(\"search-results-body\")/tr")));
        List<WebElement> searchResults = driver.findElements(By.xpath("id(\"search-results-body\")/tr"));
        assertEquals(1, searchResults.size());
        
        // Browse to the edit View of a search result and verify if the details are displayed
        driver.findElement(By.xpath("id(\"search-results-body\")/tr[1]/td[1]/a")).click();
        wait.until(new HasLandedOnEditUserIdentityView());
        assertEquals("U1", driver.findElement(By.id("userName")).getAttribute("value"));
        
        // Edit the details, save and reverify the details
        driver.findElement(By.id("userName")).clear();
        driver.findElement(By.id("userName")).sendKeys("U-One");
        driver.findElement(By.id("saveUserIdentity")).click();
        wait.until(new HasLandedOnEditUserIdentityView());
        assertEquals("U-One", driver.findElement(By.id("userName")).getAttribute("value"));
    }
    
    @Test
    @InSequence(3)
    public void testEstablishCustomerStoreOrderRelation(@ArquillianResource URL baseUrl) throws Exception {
        Wait<WebDriver> wait = new WebDriverWait(driver, 10);
        
        // Click on the Customers nav entry
        driver.get(baseUrl.toString() + "app.html#/");
        driver.findElement(By.linkText("GroupIdentitys")).click();
        wait.until(new HasLandedOnSearchGroupIdentityView());
        
        // Choose to create a new store order
        driver.findElement(By.id("Create")).click();
        wait.until(new HasLandedOnNewGroupIdentityView());
        
        // Enter the store order details and save
        driver.findElement(By.id("groupName")).clear();
        driver.findElement(By.id("groupName")).sendKeys("G1");
        WebElement usersElement = driver.findElement(By.id("users"));
        Select orders = new Select(usersElement);
        orders.selectByVisibleText("2");
        driver.findElement(By.id("saveGroupIdentity")).click();
        
        // Verify the details are presented in the Edit view 
        wait.until(new HasLandedOnEditGroupIdentityView());
        assertEquals(baseUrl.toString() + "app.html#/GroupIdentitys/edit/3", driver.getCurrentUrl());
        assertEquals("G1", driver.findElement(By.id("groupName")).getAttribute("value"));
        usersElement = driver.findElement(By.id("users"));
        orders = new Select(usersElement);
        assertEquals("2", orders.getFirstSelectedOption().getText());
    }
    
}
