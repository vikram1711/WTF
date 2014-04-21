/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.multiplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.wtf.core.WTFTest;
import org.wtf.core.WTFTestConfig;
import org.wtf.core.annotation.browser.TestBrowserAnnotationReader;
import org.wtf.core.annotation.browser.TestBrowser.Browser;
import org.wtf.core.annotation.site.TestSiteAnnotationReader;
import org.wtf.core.annotation.site.TestSite.Country;
import org.wtf.core.feature.args.converter.ServerEnvironment.Environment;
import org.wtf.core.feature.retry.BaseTestRetryAnalyzer;



/**
 * Intercept, multiplex and filter tests for the given Browser and Sites combination.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public class TestMultiplier implements IMethodInterceptor {

  public List<IMethodInstance> intercept(List<IMethodInstance> methodInstances, ITestContext context) {

	List <IMethodInstance> allowedMethods = new ArrayList <IMethodInstance>();
	Browser browserTypeFromXml = Browser.parse(WTFTest.getParameter(context, "browser-type"));

    // County/Site list from user input.
    List <Country> requestedCountryList =
        new ArrayList <Country> (Arrays.asList(WTFTestConfig.getSites()));

    // Browser list from user input.
    List <Browser> requestedBrowserList = new ArrayList<Browser>();
    if (browserTypeFromXml != null) {    
    	requestedBrowserList.add(browserTypeFromXml);
    } else {
    	requestedBrowserList = new ArrayList <Browser> (Arrays.asList(WTFTestConfig.getBrowsers()));
    }

    // Browser list from user input.
    List <Environment> requestedEnvironmentList =
        new ArrayList <Environment> (Arrays.asList(WTFTestConfig.getEnvironments()));

    // Create all combinations of county and browser tests based on user input.
    //if (browserTypeFromXml == null)
    for (IMethodInstance methodInstance : methodInstances) {
      allowedMethods.addAll(duplicate(methodInstance.getMethod(),
                                      requestedCountryList,
                                      requestedBrowserList,
                                      requestedEnvironmentList));
    }

    List <IMethodInstance> allowedMethodsFinal = new ArrayList <IMethodInstance>();

    for (IMethodInstance methodInstance : allowedMethods) {
      // list of countries annotated on the test method.
      List <Country> enabledCountryList =
        new ArrayList <Country> (Arrays.asList(
            TestSiteAnnotationReader.getIncludedSites(methodInstance.getMethod().getMethod())));
      Collections.sort(enabledCountryList);
      
      // list of excluded countries annotated on the test method.
      List <Country> excludedCountryList =
        new ArrayList <Country> (Arrays.asList(
            TestSiteAnnotationReader.getExcludedSites(methodInstance.getMethod().getMethod())));
      Collections.sort(excludedCountryList);

      // If no country/site annotation found, set country US as the default one.
      /*if (enabledCountryList.size() == 0) {
        enabledCountryList.add(Country.US);
      }*/

      // list of browsers annotated on the test method.
      List <Browser> enabledBrowserList =
        new ArrayList <Browser> (Arrays.asList(
            TestBrowserAnnotationReader.getIncludedBrowsers(methodInstance.getMethod().getMethod())));
      Collections.sort(enabledBrowserList);

      // list of excluded browsers annotated on the test method.
      List <Browser> excludedBrowserList =
        new ArrayList <Browser> (Arrays.asList(
            TestBrowserAnnotationReader.getExcludedBrowsers(methodInstance.getMethod().getMethod())));
      Collections.sort(excludedBrowserList);
      
      if (Collections.binarySearch(enabledCountryList,
                                   ((BaseTestRetryAnalyzer)methodInstance
                                       .getMethod()
                                       .getRetryAnalyzer()).country) >= 0 && 
          Collections.binarySearch(excludedCountryList,
                                    ((BaseTestRetryAnalyzer)methodInstance
                                    	.getMethod()
                                         .getRetryAnalyzer()).country) < 0) {
        if (enabledBrowserList.size() > 0 || excludedBrowserList.size() > 0) {
          if (Collections.binarySearch(excludedBrowserList,
                  ((BaseTestRetryAnalyzer)methodInstance
                          .getMethod()
                          .getRetryAnalyzer()).browser) >= 0) {
                // skip adding into allowed metho list. 
        	  	//allowedMethodsFinal.add(methodInstance);
          } else if (Collections.binarySearch(enabledBrowserList,
                                       ((BaseTestRetryAnalyzer)methodInstance
                                           .getMethod()
                                           .getRetryAnalyzer()).browser) >= 0) {

            allowedMethodsFinal.add(methodInstance);
          } else if (Collections.binarySearch(enabledBrowserList, Browser.HTML_UNIT) >= 0) {
            ((BaseTestRetryAnalyzer)methodInstance
                .getMethod()
                .getRetryAnalyzer()).browser = Browser.HTML_UNIT;

            allowedMethodsFinal.add(methodInstance);
          }
        } else {
          allowedMethodsFinal.add(methodInstance);
        }
      }
    }

    return allowedMethodsFinal;
  }

  public List<IMethodInstance> duplicate(ITestNGMethod testNGMethod,
                                         List <Country> requestedCountryList,
                                         List <Browser> requestedBrowserList,
                                         List <Environment> requestedEnvironmentList) {
    List <IMethodInstance> allowedMethods = new ArrayList <IMethodInstance>();

    for (Environment environment : requestedEnvironmentList) {
      for (Country country : requestedCountryList) {
        for (Browser browser : requestedBrowserList) {
          final ITestNGMethod clonedTestNGMethod = testNGMethod.clone();
          final Object[] instances = testNGMethod.getTestClass().getInstances(true);
  
          BaseTestRetryAnalyzer bt = new BaseTestRetryAnalyzer();
          bt.environment = environment;
          bt.country = country;
          bt.browser = browser;
          clonedTestNGMethod.setRetryAnalyzer(bt);
  
          allowedMethods.add(new IMethodInstance() {
            public Object[] getInstances() {
              return instances;
            }
      
            public ITestNGMethod getMethod() {
              return clonedTestNGMethod;
            }
  
            public Object getInstance() {
              return instances;
            }
          });
        }
      }
    }
    return allowedMethods;
  }
}