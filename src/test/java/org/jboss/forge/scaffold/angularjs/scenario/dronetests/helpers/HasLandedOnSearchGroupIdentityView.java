package org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class HasLandedOnSearchGroupIdentityView implements ExpectedCondition<Boolean> {

    @Override
    public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().endsWith("/GroupIdentitys") && driver.findElement(By.id("Create")).isEnabled();
    }

}
