/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core;

import static org.wtf.core.feature.logger.BaseLogger.LOG;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import org.wtf.core.annotation.browser.TestBrowser.Browser;
import org.wtf.core.annotation.environment.TestEnv.Env;
import org.wtf.core.annotation.site.TestSite.Country;
import org.wtf.core.feature.args.WTFTestArgs;
import org.wtf.core.feature.args.converter.ServerUrl;
import org.wtf.core.feature.args.converter.ServerEnvironment.Environment;
import org.wtf.core.feature.args.converter.ServerUrl.Server;

import org.wtf.core.WTFEnv;
import org.wtf.core.WTFTestConfig;


/**
 * Webdriver test configuration for handling test flags and eCafe URLUtil.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public class WTFTestConfig {
  public static Boolean initOnce = false;

  public static Properties sysProperties = null;
  public static final String NONE_TEXT = "none"; 
  public static final String PROD_ENVIRONMENT = "PROD";
  public static final String QA_ENVIRONMENT = "QA";
  public static final String QA_BASE_SERVER_URL = "qa.ebay.com";
  public static final String PROD_BASE_SERVER_URL = "ebay.com";
  public static final String PREPROD_BASE_SERVER_URL = "latest.ebay.com";
  public static final String DEFAULT_TEST_RETRY_COUNT = "0";
  public static final String DEFAULT_TEST_RERUN_COUNT = "1";
  public static final String TEST_SERVER_WAR_FILE = null;
  public static final String TEST_SERVER_PORT = "none";
  public static final String TEST_REPORT_EMAIL_HOST = "qa-ipmail01-d1.qa.ebay.com";
  public static final String TEST_REPORT_EMAIL_CC = "";
  public static final String TEST_REPORT_EMAIL_SUBJECT = "";
  public static final String TEST_REPORT_EMAIL_FROM = "dl-ebay-serengeti-admin@ebay.com";

  public static final String DEFAULT_SITE = "US";
  public static final String DEFAULT_BROWSER = "firefox";

  // Test flag names
  public static final String flagServerPool = "ebay.webdriver.server.pool";
  public static final String flagServerEnvironment = "ebay.webdriver.server.environment";
  public static final String flagServerURL = "ebay.webdriver.server.url";
  public static final String flagServerHubURL = "ebay.webdriver.server.hub.url";
  public static final String flagServerBackupHubURL = "ebay.webdriver.server.backup.hub.url";
  public static final String flagServerTestRetry = "ebay.webdriver.test.retry";
  public static final String flagBrowser = "ebay.webdriver.browser";
  public static final String flagPiplineMonitor = "ebay.pipeline.monitor";
  public static final String flagChromeDriver = "ebay.webdriver.chrome.driver";
  public static final String flagTestReRun = "ebay.webdriver.test.rerun";
  public static final String flagTestServerWar = "ebay.webdriver.test.server.war";
  public static final String flagTestServerPort = "ebay.webdriver.test.server.port";
  public static final String flagReportSuiteName = "ebay.webdriver.report.suite.name";

  public static final String flagReportEmailEnable = "ebay.webdriver.report.email.enable";
  public static final String flagReportEmailHost = "ebay.webdriver.report.email.host";
  public static final String flagReportEmailCC = "ebay.webdriver.report.email.cc";
  public static final String flagReportEmailFrom = "ebay.webdriver.report.email.from";
  public static final String flagReportEmailSubject = "ebay.webdriver.report.email.subject";
  
  public static final String flagReportDomainName = "ebay.webdriver.report.domain.name";
  public static final String flagReportUsingBaseLogger = "ebay.webdriver.report.using.baselogger";
  public static final String flagReportEnable = "ebay.webdriver.reporter.enable";
  public static final String flagCalLogAnalyzer = "ebay.webdriver.cal.log.analyzer";

  public static final String flagEpProxyurl = "ebay.webdriver.ep.proxy.url";

  public static final String flagSites = "ebay.webdriver.test.site";
  public static final String flagBrowsers = "ebay.webdriver.test.browser";

  public static final String flagScreenShot = "ebay.webdriver.test.screenshot";

  public static final String flagReportFailureByOwner = "ebay.webdriver.report.failure.owner.enable";

  public static void updateSystemProperties(String propertyName, String propertyValue) {
    if (!System.getProperties().containsKey(propertyName)) {
      System.getProperties().setProperty(propertyName, propertyValue);
    }
  }

  /**
   * Returns the server Environment.
   */
  public static String getServerEnvironment() {
    return WTFTestArgs.commandLineArgs.environment.toString();
  }

  /**
   * Returns the server Pool.
   */
  public static String getServerPool() {
    return WTFTestArgs.commandLineArgs.pool;
  }

  /**
   * Returns the server URL.
   */
  public static String getBaseServerURL() {
    return getServerBaseUrlBasedOnServerEnvironment();
  }

  public static String getServerBaseUrlBasedOnServerEnvironment() {
    String baseUrl = QA_BASE_SERVER_URL; 
    if (getServerEnvironment().equalsIgnoreCase(Environment.PROD.toString())) {
      baseUrl = PROD_BASE_SERVER_URL;}
    if (getServerEnvironment().equalsIgnoreCase(Environment.PREPROD.toString())) {
          baseUrl = PREPROD_BASE_SERVER_URL;
    }
    return baseUrl;
  }

  public static String getServerBaseUrlBasedOnServerEnvironment(Environment environment) {
    switch (environment) {
      default:
      case QA:
        return QA_BASE_SERVER_URL;
      case PROD:
        return PROD_BASE_SERVER_URL;
      case PREPROD:
        return PREPROD_BASE_SERVER_URL;
    }
  }

  /**
   * Returns the server URL.
   */
  public static String getServerURL() {
    System.out.println(Env.getEnvFromEnvString(flagServerURL));
    if(WTFTestArgs.commandLineArgs.environment.size()==1 && WTFTestArgs.commandLineArgs.environment.contains("PROD"))//Chnages by K Vikram
      return getServerUrlBasedOnServerEnvironmentPoolAndUrl(Country.US, Environment.PROD);
    if(WTFTestArgs.commandLineArgs.environment.size()==1 && WTFTestArgs.commandLineArgs.environment.contains("PREPROD"))
      return getServerUrlBasedOnServerEnvironmentPoolAndUrl(Country.US, Environment.PREPROD);
    else
      return getServerUrlBasedOnServerEnvironmentPoolAndUrl(Country.US, Environment.QA);
  }

  /**
   * Returns the server URL.
   */
  public static String getServerURL(WTFEnv env) {
    return getServerUrlBasedOnServerEnvironmentPoolAndUrl(env.getSite(), env.getEnvironment());
  }

  /**
   * Returns the server URL.
   */
  public static String getServerURL(String url) {
    if (WTFTestArgs.commandLineArgs.server == null) {
      return url;
    } else {
      return getServerURL(Country.US);
    }
  }
  
  public static String getUserAgent() {
    return WTFTestArgs.commandLineArgs.userAgent;
  }
  /**
   * Returns the server URL.
   */
  public static String getServerURL(Country site, String url) {
    if (WTFTestArgs.commandLineArgs.server == null) {
      return url;
    } else {
      return getServerURL(site);
    }
  }

  /**
   * Returns the server URL.
   */
  public static Boolean isServerOverride() {
    return WTFTestArgs.commandLineArgs.server == null ? false : true;
  }

  /**
   * Returns the server URL.
   */
  public static String getServerURL(Country site) {
    return ServerUrl.getServer(WTFTestArgs.commandLineArgs.wtfServers, site);
  }

  public static boolean isPoolEnabled() {
    return WTFTestArgs.commandLineArgs.pool == null ? false : true;
  }

  public static String getServerUrlBasedOnServerEnvironmentPoolAndUrl() {
    return getServerUrlBasedOnServerEnvironmentPoolAndUrl(Country.US, Environment.QA);
  }

  public static String getServerUrlBasedOnServerEnvironmentPoolAndUrl(Country site, Environment environment) {
    String serverURL = getServerBaseUrlBasedOnServerEnvironment(environment);

    if (isPoolEnabled()) {
      serverURL = String.format("%s.%s", getServerPool(), serverURL);
    }
    return serverURL;
  }

  public static String getRemoteWebDriverHubURL() {
    return WTFTestArgs.commandLineArgs.grid? WTFTestArgs.commandLineArgs.gridUrl : null;
  }

  public static String getBackupRemoteWebDriverHubURL() {
    return WTFTestArgs.commandLineArgs.backupGrid? WTFTestArgs.commandLineArgs.backupGridUrl : null;
  }

  public static String getTestRetry() {
    return WTFTestArgs.commandLineArgs.retry.toString();
  }

  public static void setTestRetry(String testRetry) {
    WTFTestArgs.commandLineArgs.retry = Integer.parseInt(testRetry);
  }

  // legacy
  public static String getBrowser() {
    return null;
  }

  public static String getPipelineMonitor() {
    return WTFTestArgs.commandLineArgs.gyro ? "True" : null;
  }

  public static String getChromeDriver() {
    return "";
  }

  public static String getTestReRun() {
    return WTFTestArgs.commandLineArgs.rerun.toString();
  }

  public static String getReportSuiteName() {
    return WTFTestArgs.commandLineArgs.gyroSuiteName;
  }

  public static String getReportDomainName() {
    return WTFTestArgs.commandLineArgs.gyroDomainName;
  }

  public static String getSuiteName() {
    return WTFTestArgs.commandLineArgs.suiteName;
  }

  public static void setServerUrl(String url) {
    List <Server> servers = new ArrayList <Server>();
    servers.add(Server.US.setServer(url));
    WTFTestArgs.commandLineArgs.wtfServers = servers;
  }

  public static boolean isSmartLogEnabled() {
    return WTFTestArgs.commandLineArgs.smartLog;
  }

  public static boolean isEpProxyEnabled() {
    return WTFTestArgs.commandLineArgs.epProxy != null ? true : false;
  }

  public static boolean isEmailBrowserNameHidden() {
    return WTFTestArgs.commandLineArgs.emailHideBrowserName;
}

  public static String getEpProxyUrl() {
    return WTFTestArgs.commandLineArgs.epProxy;
  }

  public static boolean reportToGyroEnabled() {
    return WTFTestArgs.commandLineArgs.gyro;
  }

  public static boolean reportViaEmailEnabled() {
    return WTFTestArgs.commandLineArgs.emailFailure;
  }

  public static String getCC() {
    return WTFTestArgs.commandLineArgs.emailCC;
  }

  public static String getSubject() {
    return WTFTestArgs.commandLineArgs.emailSubject;
  }

  public static boolean analyzeCalLogs() {
    return false;
  }

  public static boolean screenShotEnabled() {
    return WTFTestArgs.commandLineArgs.screenshot;
  }

  public static boolean reportFailureByOwnerEnabled() {
    return WTFTestArgs.commandLineArgs.printFailureByOwner != null ? true : false;
  }

  public static boolean reportConsolidatedFailureEnabled() {
    return WTFTestArgs.commandLineArgs.emailGroup != null ? true : false;
  }

  public static boolean reportConsolidatedOnlyIfFailedEnabled() {
    return WTFTestArgs.commandLineArgs.emailFailedOnly;
  }

  public static boolean reportConsolidatedFailureWithDescriptionOnlyEnabled() {
    return WTFTestArgs.commandLineArgs.emailDescriptiononly;
  }

  public static String getGroupEmailId() {
    return WTFTestArgs.commandLineArgs.emailGroup;
  }

  public static Country [] getSites() {
    if (WTFTestArgs.commandLineArgs.wtfSites.size() == 0) {
      WTFTestArgs.commandLineArgs.wtfSites.add(Country.US);
    }
    return WTFTestArgs.commandLineArgs.wtfSites.toArray(new Country[WTFTestArgs.commandLineArgs.wtfSites.size()]);
  }

  public static Browser [] getBrowsers() {
    if (WTFTestArgs.commandLineArgs.wtfBrowsers.size() == 0) {
      WTFTestArgs.commandLineArgs.wtfBrowsers.add(Browser.FIREFOX);
    }
    return WTFTestArgs.commandLineArgs.wtfBrowsers.toArray(new Browser[WTFTestArgs.commandLineArgs.wtfBrowsers.size()]);
  }


  public static Environment [] getEnvironments() {
    if (WTFTestArgs.commandLineArgs.wtfEnvironments.size() == 0) {
      WTFTestArgs.commandLineArgs.wtfEnvironments.add(Environment.QA);
    }
    return WTFTestArgs.commandLineArgs.wtfEnvironments.toArray(new Environment[WTFTestArgs.commandLineArgs.wtfEnvironments.size()]);
  }

  public static Boolean isCSSLintEnabled() {
    return WTFTestArgs.commandLineArgs.cssLint;
  }

  public static List <String> getCSSLintFileFolderlist() {
    return WTFTestArgs.commandLineArgs.cssLintFolder;
  }

  public static void initFlags() {
    if (WTFTestConfig.initOnce) {
      return;
    }
    WTFTestConfig.initOnce = true;
    // put your init flags code here, this will get called before running any tests.
    LOG(Level.INFO, "Test Run Started..");
    LOG(null, "");
    LOG(Level.INFO, "Tips 1: Do Git pull as often as possible.");
    LOG(Level.INFO, "Tips 2: For every Git push, Please make sure WTF build (WTFBuildChecker) is GREEN here http://go/wtfci");
    LOG(Level.INFO, "Tips 3: Join this DL and ask your WTF related questions DL-eBay-qa-threadsafe-webdriver-framework@corp.ebay.com");
    LOG(null, "");

    WTFTestArgs.init();

    updateSystemProperties("store", "service");
    //updateSystemProperties("service.url", "http://logsvcs.vip.qa.ebay.com/testlog/");
    updateSystemProperties("service.url", "http://logsvcs.stratus.qa.ebay.com/testlog/");    

    if (WTFTestArgs.commandLineArgs.grid) {
      LOG(Level.INFO, String.format("On Grid Mode. Using Grid: %s", WTFTestArgs.commandLineArgs.gridUrl));
    }
  }
}
