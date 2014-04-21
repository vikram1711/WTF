/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core;

import static org.wtf.core.feature.logger.BaseLogger.LOG;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.testng.TestNG;
import org.testng.xml.Parser;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import org.wtf.core.annotation.browser.TestBrowser.Browser;
import org.wtf.core.annotation.site.TestSite.Country;
import org.wtf.core.feature.args.WTFCommandLineArgsAllTest;
import org.wtf.core.feature.args.WTFTestArgs;
import org.wtf.core.feature.args.converter.WTFConverter;
import org.wtf.core.feature.args.converter.ServerEnvironment.Environment;
import org.wtf.core.interfaces.ApplicationUnderTest;

import com.beust.jcommander.JCommander;
import org.wtf.core.WTFTestConfig;
import org.wtf.core.WTFTestNG;

public class WTFTestNG extends TestNG {

  public static WTFCommandLineArgsAllTest cla = new WTFCommandLineArgsAllTest();
  private HashMap<String, String[]> autWithPackageDetails = null;
  private List<XmlSuite> testSuites = new ArrayList<XmlSuite>();
  private String methodSelectorScript = null;
  private boolean includeMethodSelectorScript = false;

  public WTFTestNG() throws IOException {
    super();
  }

  public WTFTestNG parseAndValidateCommandLineInputs(ApplicationUnderTest autWithPackageDetails, 
		  String[] args) {
	  return parseAndValidateCommandLineInputs(autWithPackageDetails, args, 
			  includeMethodSelectorScript);
  }
  
  public WTFTestNG parseAndValidateCommandLineInputs(
		  ApplicationUnderTest autWithPackageDetails, String[] args,
		  boolean includeMethodSelectorScript) {
	args = autWithPackageDetails.resolveGroupNames(args);
	this.includeMethodSelectorScript = includeMethodSelectorScript; 
    this.autWithPackageDetails = autWithPackageDetails.get();
    JCommander m_jCommander = new JCommander(cla);
    if (args.length > 0) {
      try {
        m_jCommander.parse(args);
        if (args.length == 1
            && (args[0].contains("-help") || args[0].contains("-h"))) {
          m_jCommander.usage();
          System.exit(0);
        }
      } catch (Exception e) {
        LOG(Level.SEVERE,
            "Invalid test araguments please refer the below arg usage.");
        m_jCommander.usage();
        LOG(Level.INFO, "Going down..");
        System.exit(0);
      }      
    }

    if (cla.gridUrl != null && cla.gridUrl.length() > 0
        && !("blank".equalsIgnoreCase(cla.gridUrl))) {
      cla.grid = true;
      System.out.println("Running tests against grid: " + cla.gridUrl);
    }
    
    if (cla.gyroSuiteName != null && cla.gyroSuiteName.length() > 0
            && !("blank".equalsIgnoreCase(cla.gyroSuiteName))) {
          cla.gyro = true;
          System.out.println("Reporting test results to gyro");
    }
    
    setSystemProperties();

    if (cla.outputDirectory != null && cla.outputDirectory.length() > 0) {
      setOutputDirectory(cla.outputDirectory + "\\results");
    }

    if (cla.browsers == null || cla.browsers.size() == 0) {
    	cla.browsers.add("ff");
    }
    if (cla.sites == null || cla.sites.size() == 0) {
    	cla.sites.add("us");
    }
    if (cla.environment == null || cla.environment.size() == 0) {
    	cla.environment.add("qa");
    }
    cla.wtfBrowsers = WTFConverter.browserConveter(cla.browsers);
    cla.wtfSites = WTFConverter.siteConverter(cla.sites);
    cla.wtfEnvironments = WTFConverter.environmentConverter(cla.environment);
    cla.wtfServers = WTFConverter.serverConverter(cla.server);
    // Remove Duplicates.
    cla.wtfBrowsers =
        new ArrayList <Browser>(new HashSet <Browser>(cla.wtfBrowsers));
    cla.wtfSites = new ArrayList<Country>(new HashSet<Country>(cla.wtfSites));
    cla.wtfEnvironments = new ArrayList<Environment>(new HashSet<Environment>(cla.wtfEnvironments));
    System.setProperty("HIGHLIGHT_ELEMENT", cla.highLightElement.toString());

    //TODO(jevasudevan): Remove this duplicate logic
    WTFTestArgs.commandLineArgs = cla;
    WTFTestConfig.initOnce = true;
    return this;
  }

  public WTFTestNG setMethodSelectorScript(String methodSelectorScript) {
	  this.methodSelectorScript = methodSelectorScript;
	  return this;
  }
    
  /**
   * Provides method selector bean shell script which will eventually be
   * embedded into programatically generated testng.xml
   * 
   * @param aut
   *          Application Under Test
   * @param includedMethods
   *          Methods name to consider for test run
   * @param testType
   *          Test type 'regression' to run all tests or 'smoke' for P1 tests
   * @param site
   *          Site of the application under test
   * 
   * @return String version of the method selector script
   */
  public String methodSelectorScript(String aut, String includedMethods,
      String testType, String site) {
    String beanShellMethodSelectorScript = ""
        + "boolean methodEligibleToRun = false;"
        + "boolean isIncludedMethod = false;"
        + "boolean setupMethod = groups.containsKey(\"all\");"
        + "String testType = \""
        + testType
        + "\";"
        + "String includedMethods = \""
        + includedMethods
        + "\";"
        + "if (includedMethods.equals(\"*\") || includedMethods.contains(method.getName()))"
        + "{"
        + "isIncludedMethod = true;"
        + "}"
        + "if (setupMethod "
        +      "|| (groups.containsKey(\"" + aut + "\") "
        +      "&& (groups.containsKey(\"allSites\") || groups.containsKey(\"" + site.toUpperCase() + "\")))"
        + ") "
        + "{"
        + " methodEligibleToRun = true; "
        + "} "
        + "methodEligibleToRun = (!setupMethod && testType.equals(\"smoke\"))"
        + "? (methodEligibleToRun && groups.containsKey(\"smoke\")) "
        + ": methodEligibleToRun;"
        + "return setupMethod "
        + "? methodEligibleToRun : (methodEligibleToRun && isIncludedMethod);";
    return beanShellMethodSelectorScript;
  }

  public List<XmlSuite> createXmlSuites() {
	testSuites.add(createXmlSuite());    
	return testSuites;
  }

  public WTFTestNG setXmlSuites() {
    if (cla.testngXmlFile != null) {
      try {
        setXmlSuites(new ArrayList(new Parser(cla.testngXmlFile).parse()));
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else {
      setXmlSuites(createXmlSuites());
    }

    if (cla.printTestngXml) {
      for (XmlSuite xmlSuite : m_suites) {
        System.out.println(xmlSuite.toXml());
      }
    }
    return this;
  }

  public void run() {
    super.run();
    if (hasFailure()) {
      System.out.println("There are test FAILURES, Please find results at: "
          + getOutputDirectory());
      System.exit(1);
    }
    System.out.println("All tests PASSED. Please find results at: "
        + getOutputDirectory());
    System.exit(0);

  }

  /**
   * Helper function to create Xml suite instance.
   * 
   * @param aut
   *          Application Under Test
   * @return Instance of XmlSuite
   */
  public XmlSuite createXmlSuite() {
    XmlSuite suite = new XmlSuite();
    suite.setName("BuyerExperience: Electronics Verticals");
    suite.setParallel("methods");
    suite.setVerbose(1);

    // Global parameters
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("pool_type", cla.environment.toString());
    parameters.put("pool", cla.pool);
    suite.setParameters(parameters);

    suite.setListeners(getListeners());
    for (String aut : cla.aut.split(",")) {
    	for (Browser browser : cla.wtfBrowsers) {   
    		for (Country site : cla.wtfSites) {
    			addXmlTest(suite, aut, site.getCountryCode(), browser.browserName);;
		 }
    	}
    }
               
    return suite;
  }

  public WTFTestNG setSystemProperties() {
    cla.wtfBrowsers = new ArrayList<Browser>(new HashSet<Browser>(cla.wtfBrowsers));
    cla.wtfSites = new ArrayList<Country>(new HashSet<Country>(cla.wtfSites));
    System.setProperty("HIGHLIGHT_ELEMENT", cla.highLightElement.toString());

    System.setProperty("service.url", "http://logsvcs.stratus.qa.ebay.com/testlog/");
    System.setProperty("store", "service");
    
    if (cla.grid) {
    	if (System.getenv("tsguid")== null) {
    		System.setProperty("tsguid", "BuyerExperienceVerticals: Electronics");
    	}
    	System.setProperty("stepguid", System.getProperty("tsguid"));
    }
    return this;
  }

  public List<String> getListeners() {
    String[] tempListeners = cla.listener.split(",");
    List<String> listeners = new ArrayList<String>();

    for (String listener : tempListeners) {
      listeners.add(listener);
    }
    return listeners;
  }

  public XmlTest addXmlTest(XmlSuite suite, String aut, String site,
      String browser) {
    Map<String, String> parameters = new HashMap<String, String>();
    parameters.put("site", site.toUpperCase());
    parameters.put("browser-type", browser.toLowerCase());

    XmlTest test = new XmlTest(suite);    
    test.setThreadCount(cla.threadCount);

    String[] includeClasses = cla.testClass.split(",");
    String[] includeMethods = cla.testNames.split(",");
    String packageName = null;

    if (autWithPackageDetails != null && !autWithPackageDetails.isEmpty()) {
      Object[] keys = (Object[]) autWithPackageDetails.keySet().toArray();
      for (Object key : keys) {
        for (String autName : autWithPackageDetails.get(key)) {
          if (autName.equalsIgnoreCase(aut.toLowerCase())) {
        	aut = autName;
            packageName = (String) key;
            break;
          }
        }
        if (packageName != null)
          break;
      }
    }

    test.setName(aut + "_" + browser.toLowerCase() + "_" + site.toLowerCase());
    parameters.put("page", aut);
    if (includeMethodSelectorScript) {
    	test.setBeanShellExpression(methodSelectorScript != null 
    			? methodSelectorScript :
    				methodSelectorScript(aut, cla.testNames, cla.testType, site));
    }   

    test.setParallel("true");
    test.setParameters(parameters);

    if (includeClasses[0].equals("*") && includeMethods[0].equals("*")) {
      List<XmlPackage> testPackages = new ArrayList<XmlPackage>();

      XmlPackage xmlPackage = new XmlPackage();
      xmlPackage.setName(packageName + ".*");
      testPackages.add(xmlPackage);
      test.setXmlPackages(testPackages);
    } else if (!includeClasses[0].equals("*")) {
      List<XmlClass> testClass = new ArrayList<XmlClass>();
      for (String clazz : includeClasses) {
        testClass.add(new XmlClass(packageName + "." + clazz));
      }
      test.setXmlClasses(testClass);
    } else if (!includeMethods[0].equals("*")) {
      List<XmlClass> testClass = new ArrayList<XmlClass>();
      List<XmlInclude> xmlIncluded = new ArrayList<XmlInclude>();
      XmlClass xmlClass = null;
      int index = 0;
      for (String clazzAndMethod : includeMethods) {
        index = clazzAndMethod.indexOf(".");
        xmlIncluded.add(new XmlInclude(clazzAndMethod.substring(index + 1)));
        xmlClass = new XmlClass(packageName + "."
            + clazzAndMethod.substring(0, index));
        xmlClass.setIncludedMethods(xmlIncluded);
        testClass.add(xmlClass);
      }
      test.setXmlClasses(testClass);
    }
    return test;
  }
}
