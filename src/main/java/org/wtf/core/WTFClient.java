/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;

import org.wtf.core.WTFObject;

/**
 * An abstract webdriver client.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public abstract class WTFClient extends WTFObject {

  private WebDriver driver;
  private WebDriverWait wait;

  public WTFClient(WebDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
  }

  /**
   * Returns the webdriver.
   */
  protected WebDriver getDriver() {
    return driver;
  }

  /**
   * Returns the webdriver wait.
   */
  protected WebDriverWait getWait() {
    return wait;
  }

  public boolean notNullNotNoneNotEmpty(String value) {
    return value != null && !value.equals("none") && !value.equals("");
  }
}
