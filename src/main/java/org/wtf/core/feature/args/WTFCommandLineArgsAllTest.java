/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.args;

import com.beust.jcommander.Parameter;


/**
 * WTF command line args declarations.
 * 
 * @author jevasudevan@ebay.com (Jeganathan Vasudevan)
 */
@SuppressWarnings("restriction")
public class WTFCommandLineArgsAllTest extends WTFCommandLineArgs {

  public static final String HELP = "-help";
	 @Parameter(names = HELP, description = "Shows help/usage document")
  public boolean help = true;
	  
  public static final String TESTNG_XML_FILE = "-testngXmlFile";
	 @Parameter(names = TESTNG_XML_FILE, 
		description = "Testng xml file with fullpath")
  public String testngXmlFile = null;
 
  public static final String PRINT_TESTNG_XML = "-printTestngXml";
	 @Parameter(names = PRINT_TESTNG_XML, 
		description = "Prints testng xml file content")
  public boolean printTestngXml = false;
	  
  public static final String AUT = "-aut";
  @Parameter(names = AUT,  description = "Comma seperated applications under test " +
		"(Electronics,BlackBerry)")
  public String aut = "Electronics";
	      
  public static final String TEST_CLASS = "-testclass";
  @Parameter(names = TEST_CLASS, description = "The list of test classes")
  public String testClass = "*";
  
  public static final String TEST_NAMES = "-testnames";
  @Parameter(names = TEST_NAMES, description = "The list of test names to run")
  public String testNames= "*";
	
  public static final String TEST_TYPE = "-testType";
  @Parameter(names = TEST_TYPE, 
			description = "Group name denotes test type (smoke or regression)")
  public String testType = "regression";
  
  public static final String LISTENER = "-listener";
  @Parameter(names = LISTENER, description = "List of .class files or list of class names" +
      " implementing ITestListener or ISuiteListener")
  public String listener;

  public static final String OUTPUT_DIRECTORY = "-d";
  @Parameter(names = OUTPUT_DIRECTORY, description ="Output directory")
  public String outputDirectory = System.getProperty("user.dir") + "\\results";
  
  public WTFCommandLineArgsAllTest() {
	listener = "com.ebay.webdriver.wtf.core.listener.BaseListener," +
			"com.ebay.webdriver.wtf.dash.testng.listener.json.WTFDashNodeJsonDataPushListener";
	pool = "staging";
	threadCount =  2;
	gridUrl = "blank";
  }
}
