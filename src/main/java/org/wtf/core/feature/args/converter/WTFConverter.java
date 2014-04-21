/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.args.converter;

import java.util.ArrayList;
import java.util.List;

import org.wtf.core.annotation.browser.TestBrowser.Browser;
import org.wtf.core.annotation.site.TestSite.Country;
import org.wtf.core.feature.args.converter.ServerEnvironment.Environment;
import org.wtf.core.feature.args.converter.ServerUrl.Server;



public abstract class WTFConverter {

  public static List <Browser> browserConveter(List <String> values) {
    List <Browser> browserList = new ArrayList <Browser>();
    for (String value : values) {
      String [] browsers = value.split(",");
      for (String browser : browsers) {
        browserList.add(Browser.parse(browser));
      }
    }
    return browserList;
  }

  public static List <Country> siteConverter(List <String> values) {
    List <Country> siteList = new ArrayList<Country>();
    for (String value : values) {
      String [] sites = value.split(",");
      for (String site : sites) {
        siteList.add(Country.parse(site));
      }
    }
    return siteList;
  }

  public static List <Environment> environmentConverter(List <String> values) {
    List <Environment> envList = new ArrayList<Environment>();
    for (String value : values) {
      String [] envs = value.split(",");
      for (String env : envs) {
        envList.add(Environment.getEnv(env));
      }
    }
    return envList;
  }

  public static List<Server> serverConverter(List <String> values) {
    if (values == null) {
      return  null;
    }

    List <Server> serverList = new ArrayList <Server>();
    for (String value : values) {
      String [] servers = value.split(",");
      for (String server : servers) {
        String [] serverParts = server.split("=>");
        Server serverLocal = Server.getServer(Country.parse(serverParts[0]));
        serverLocal.url = serverParts[1];
        serverList.add(serverLocal);
      }
    }
    return serverList;
  }
}
