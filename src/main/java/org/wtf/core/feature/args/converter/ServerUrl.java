/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.args.converter;

import java.util.List;

import org.wtf.core.annotation.site.TestSite.Country;


/**
 * Service URLs.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public class ServerUrl {
  public static enum Server {
    US(Country.US, null),
    UK(Country.UK, null),
    DE(Country.DE, null),
    AU(Country.AU, null),
    CA(Country.CA, null),
    CAFR(Country.CAFR, null),
    IT(Country.IT, null),
    ES(Country.ES, null),
    FR(Country.FR, null),
    CH(Country.CH, null),
    IN(Country.IN, null);

    public Country site;
    public String url;

    private Server(Country site, String url) {
      this.site = site;
      this.url = url;
    }

    public Server setServer(String url) {
      this.url = url;
      return this;
    }

    public static Server getServer(Country site) {
      for (Server server : Server.values()) {
        if (server.site.equals(site)) {
          return server;
        }
      }
      return null;
    }
  }

  public static String getServer(List <Server> servers, Country site) {
    for (Server server : servers) {
      if (server.site.equals(site)) {
        return server.url;
      }
    }
    return null;
  }
}
