/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.lint.rhino.css;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.io.FilenameUtils;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.wtf.core.WTFAttribute;
import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTest;
import org.wtf.core.WTFTestConfig;
import org.wtf.core.feature.lint.rhino.Main;

import static org.wtf.core.feature.logger.BaseLogger.LOG;


public class WTFRhinoCSSTest extends WTFTest {

  @DataProvider(name = "WTF_CSS_LINT", parallel = true)
  protected Object[][] WTFCSSLintDataProvider_VI(final ITestContext context) {
    if (!WTFTestConfig.isCSSLintEnabled()) {
     Object [][] data = { {"CSSLint flag not set (-csslint)", true}};
     return getWebdriverFriendlyData(data, context);
    }

    Collection <File> cssFiles = new ArrayList<File>();
    System.out.println("CSS Lint SRC folders: " +WTFTestConfig.getCSSLintFileFolderlist());
    for (String fileOrFolder : WTFTestConfig.getCSSLintFileFolderlist()) {
      File fObj = new File(fileOrFolder);
      if (fObj.isFile()) {
        cssFiles.add(fObj);
      } else {
        addCSSFilesRecursively(fObj, cssFiles);
      }
    }

    if (cssFiles.size() == 0) {
      Object [][] data = { {"CSS Lint didn't find any issues! All Happy", true}};
      return getWebdriverFriendlyData(data, context);
    }

    Object [][] data = new Object [cssFiles.size()][2];

    int index = 0;
    for (File flObj : cssFiles) {
      data[index][0] = flObj.getPath();
      data[index][1] = false;
      ++index;
    }
    return getWebdriverFriendlyData(data, context);
  }

  public void verifyCSSLint(String filePath, boolean status, WTFEnv env) {
    if (!status) {
      String cssIssues = "";

      synchronized(this) {
        cssIssues = getCSSLint(filePath);
      }

      boolean found = true;
      String revFilePath = new StringBuffer(filePath).reverse().toString();
      revFilePath = ".." + new StringBuffer(revFilePath.substring(0, revFilePath.length() < 70 ? revFilePath.length() : 70)).reverse().toString();
      env.getTestResult().setAttribute("DASH_CLASS_NAME", revFilePath);

      if (cssIssues.split("csslint: There are ").length > 1) {
        String issueCount = cssIssues.split("csslint: There are ")[1].split(" problems ")[0].trim();

        String issueSummary = String.format("%s.css [%s issues found]", FilenameUtils.getBaseName(filePath), issueCount);
        env.getTestResult().setAttribute("DASH_METHOD_NAME", issueSummary);
        env.getTestResult().setAttribute(WTFAttribute.TEST_DESCRIPTION, issueSummary);
      } else {
        found = false;
        String issueSummary = String.format("%s.css [No issues found!]", FilenameUtils.getBaseName(filePath));
        env.getTestResult().setAttribute("DASH_METHOD_NAME", issueSummary);
        env.getTestResult().setAttribute(WTFAttribute.TEST_DESCRIPTION, issueSummary);
      }

      if (found) {
        LOG(Level.INFO, cssIssues.toString());
        org.testng.Assert.assertTrue(false, "CSS Lint issues found..Please check the test logs..");
      }

    } else {
      env.getTestResult().setAttribute(WTFAttribute.TEST_DESCRIPTION, filePath);
    }
  }

  public static String getCSSLint(String cssFile) {
    String xmlFilePath = WTFRhinoCSSTest.class.getResource("/webdriver/bin/rhino/csslint-rhino.js").getFile();
    List <String> argLst = new ArrayList <String>();
    argLst.add(xmlFilePath);
    argLst.add(cssFile);

    //argLst.add("--format=lint-xml");
    argLst.add("--rules=adjoining-classes,empty-rules,display-property-grouping,font-faces,font-sizes,qualified-headings,unique-headings,zero-units,vendor-prefix,gradients,regex-selectors,import,compatible-vendor-prefixes,duplicate-properties");

    String[] argss = argLst.toArray(new String[argLst.size()]);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(os);
    Main.setOut(ps);
    Main.main(argss);
    return os.toString();
  }

  static void addCSSFilesRecursively(File file, Collection <File> all) {
    File[] children = file.listFiles();
    if (children != null) {
      for (File child : children) {
        if (child.isFile() && FilenameUtils.getExtension(child.getName()).equalsIgnoreCase("CSS")) {
          all.add(child);
        }
        addCSSFilesRecursively(child, all);
      }
    }
  }
}
