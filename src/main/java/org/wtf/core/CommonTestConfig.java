/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core;

import org.wtf.core.WTFTestConfig;

/**
 * Common test config.
 * 
 * @author psaha@ebay.com (Parasar Saha)
 */


public class CommonTestConfig extends WTFTestConfig {

  private static final String EBAY_HOME_URL_PART = "http://www.";

  public static String getEbayHomePage() {
    String serverURl = WTFTestConfig.getServerURL(
        String.format(EBAY_HOME_URL_PART, WTFTestConfig.getServerURL()));
    return serverURl; 
  }

  public static String getEbaySRPPage() {
    return getEbayHomePage();
  }
}
