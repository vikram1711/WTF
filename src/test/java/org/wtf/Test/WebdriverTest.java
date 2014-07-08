package org.wtf.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTest;

public class WebdriverTest extends WTFTest {

  @Test(dataProvider = "WEBDRIVER")
  public void webdriverTest1(WTFEnv env) {
	  WebDriver driver = env.getDriver();
	  driver.get("http://www.ebay.com");
	  WebElement schTxtBox = driver.findElement(By.name("_nkw"));
	  WebElement schTxtBtn = driver.findElement(By.id("gh-btn"));
	  schTxtBox.sendKeys("iPhone");
	  schTxtBtn.click();
    
  }
}
