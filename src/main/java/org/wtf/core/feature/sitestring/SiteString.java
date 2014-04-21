/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.sitestring;


import java.util.HashMap;
import java.util.Map;

import org.wtf.core.WTFEnv;
import org.wtf.core.annotation.site.TestSite.Country;


/**
 * Stores and retrieves country specific strings.
 */
public class SiteString {

  private Map <Country, String> map;

  public SiteString() {
    map = new HashMap <Country, String>();
  }

  public SiteString(Map <Country, String> map) {
    this();
    this.map = map;
  }

  public String get(Country site) {
    return getDefaultValue(site);
  }

  public String get(WTFEnv env) {
    return getDefaultValue(env.getSite());
  }

  public void set(Country site, String text) {
    map.put(site, text);
  }

  private String getDefaultValue(Country site) {
    return map.containsKey(site) ? map.get(site) : map.get(Country.US);
  }

  public SiteString US(String text) {
    map.put(Country.US, text);
    return this;
  }

  public SiteString UK(String text) {
    map.put(Country.UK, text);
    return this;
  }

  public SiteString DE(String text) {
    map.put(Country.DE, text);
    return this;
  }
  
  public SiteString AU(String text) {
    map.put(Country.AU, text);
    return this;
  }

  public SiteString CA(String text) {
    map.put(Country.CA, text);
    return this;
  }

  public SiteString CAFR(String text) {
    map.put(Country.CAFR, text);
    return this;
  }

  public SiteString IT(String text) {
    map.put(Country.IT, text);
    return this;
  }

  public SiteString ES(String text) {
    map.put(Country.ES, text);
    return this;
  }

  public SiteString FR(String text) {
    map.put(Country.FR, text);
    return this;
  }

  public static SiteString SS() {
    return new SiteString();
  }
}
