package org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class HasLandedOnNewCustomerView implements ExpectedCondition<Boolean> {

    @Override
    public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().endsWith("/Customers/new") && !driver.findElement(By.id("saveCustomer")).isEnabled();
    }

}
