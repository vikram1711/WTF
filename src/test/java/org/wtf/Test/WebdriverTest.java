package org.wtf.Test;

import org.testng.annotations.Test;
import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTest;

public class WebdriverTest extends WTFTest {

  @Test(dataProvider = "WEBDRIVER")
  public void WebdriverTest1(WTFEnv env) {
    
  }
}
