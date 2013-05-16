/**
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.scaffold.angularjs.scenario.dronetests.helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class HasLandedOnNewUserIdentityView implements ExpectedCondition<Boolean> {

    @Override
    public Boolean apply(WebDriver driver) {
        return driver.getCurrentUrl().endsWith("/UserIdentitys/new") && driver.findElement(By.id("saveUserIdentity")).isEnabled();
    }

}
