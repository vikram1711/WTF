/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.browser;

import static org.wtf.core.annotation.browser.TestBrowser.Browser.HTML_UNIT;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTest;
import org.wtf.core.annotation.browser.TestBrowser;
import org.wtf.core.annotation.browser.TestBrowser.Browser;

/**
 * Test @Browser annonotation. 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
@TestBrowser(browsers = {HTML_UNIT})
public class TestBrowserAnnotation extends WTFTest {

  @Test(dataProvider = "WEBDRIVER", description = "Test browser annotation.")
  public void testBrowserAnnotation(WTFEnv env) {
    Assert.assertEquals(env.getBrowser(), Browser.HTML_UNIT,
                        "Browser is not HTML_UNIT as annotated.");
  }
}
