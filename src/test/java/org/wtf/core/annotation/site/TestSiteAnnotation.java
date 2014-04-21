/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.site;

import static org.wtf.core.annotation.browser.TestBrowser.Browser.HTML_UNIT;
import static org.wtf.core.annotation.site.TestSite.Country.DE;
import static org.wtf.core.annotation.site.TestSite.Country.US;

import org.testng.Assert;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.Test;
import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTest;
import org.wtf.core.annotation.browser.TestBrowser;
import org.wtf.core.annotation.site.TestSite;
import org.wtf.core.annotation.site.TestSiteAnnotationReader;
import org.wtf.core.annotation.site.TestSite.Country;



/**
 * Test cases for the @Site annonotation and its associated annotation reader class. 
 * @author denlin
 */
@TestSite(countries = {US})
@TestBrowser(browsers = {HTML_UNIT})
public class TestSiteAnnotation extends WTFTest {
  @Test(dataProvider = "WEBDRIVER", 
        description = "Positive test for the @Site annotation at the class level.")
  public void testAnnotationForClass(WTFEnv env) {
    ITestResult testResult = env.getTestResult();
    ITestNGMethod testMethod = testResult.getMethod();
    Country[] countries = TestSiteAnnotationReader.getIncludedSites(testMethod.getMethod());
    Assert.assertTrue(
      countries.length == 1,
      "This test method should only have 1 site specified");
    Assert.assertTrue(
      countries[0].equals(Country.US),
      "This method should have been detected as annotated for Country.US");
  }
  
  @Test(dataProvider = "WEBDRIVER", 
        description = "Negative test for the @Site annotation at the class level.")
  public void testAnnotationForClass_NegativeTest(WTFEnv env) {
    ITestResult testResult = env.getTestResult();
    ITestNGMethod testMethod = testResult.getMethod();
    Country[] countries = TestSiteAnnotationReader.getIncludedSites(testMethod.getMethod());
    Assert.assertTrue(
      countries.length == 1,
      "This test method should only have 1 site specified");
    Assert.assertFalse(
      countries[0].equals(Country.UK),
      "This method should NOT have been detected as annotated for Country.UK");
    Assert.assertFalse(
      countries[0].equals(Country.DE),
      "This method should NOT have been detected as annotated for Country.DE");
  }
  
  @Test(dataProvider = "WEBDRIVER", 
        description = "Positive test for the @Site annotation at the class level.")
  @TestSite(countries = {DE})
  public void testAnnotationForMethod(WTFEnv env) {
    ITestResult testResult = env.getTestResult();
    ITestNGMethod testMethod = testResult.getMethod();
    Country[] countries = TestSiteAnnotationReader.getIncludedSites(testMethod.getMethod());
    Assert.assertTrue(
      countries.length == 1,
      "This test method should only have 1 site specified");
    Assert.assertTrue(
      countries[0].equals(Country.DE),
      "This method should have been detected as annotated for Country.DE");
  }
}