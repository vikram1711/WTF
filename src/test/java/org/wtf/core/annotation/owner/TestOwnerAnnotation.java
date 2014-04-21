/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.owner;

import static org.wtf.core.annotation.browser.TestBrowser.Browser.HTML_UNIT;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTest;
import org.wtf.core.annotation.browser.TestBrowser;
import org.wtf.core.annotation.owner.TestOwner;
import org.wtf.core.annotation.owner.TestOwnerAnnotationReader;



/**
 * Test @TestOwner annonotation. 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
@TestOwner(email = "vsundramurthy")
@TestBrowser(browsers = {HTML_UNIT})
public class TestOwnerAnnotation extends WTFTest {

  @Test(dataProvider = "WEBDRIVER", description = "Test owner annotation.")
  public void testOwnerAnnotation(WTFEnv env) {
    Assert.assertEquals(TestOwnerAnnotationReader
        .getTestOwner(env.getTestResult().getMethod().getMethod()),
                      "vsundramurthy",
                      "Browser is not HTML_UNIT as annotated.");
  }
}
