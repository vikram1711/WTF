/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.site;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Sites annotation.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TestSite {
  public static enum Country {
    US("us"), UK("uk"), GB("gb"), DE("de"), AU("au"), CA("ca"), CAFR("cafr"), IT("it"), ES("es"), FR("fr"), AT("at"), IE("ie"), IN("in"), CH("ch");

    private String countryCode;

    private Country(String countRyCode) {
      this.countryCode = countRyCode;
    }

    public String getCountryCode() {
      return countryCode;
    }

    public static Country parse(String site) {
      if (site.equalsIgnoreCase("us")) {
        return Country.US;
      } else if (site.equalsIgnoreCase("uk")) {
        return  Country.UK;
      } else if (site.equalsIgnoreCase("gb")) {
          return  Country.GB;
      } else if (site.equalsIgnoreCase("de")) {
        return Country.DE;
      } else if (site.equalsIgnoreCase("au")) {
        return Country.AU;
      } else if (site.equalsIgnoreCase("ca")) {
        return Country.CA;
      } else if (site.equalsIgnoreCase("cafr")) {
        return Country.CAFR;
      } else if (site.equalsIgnoreCase("it")) {
        return Country.IT;
      } else if (site.equalsIgnoreCase("es")) {
        return Country.ES;
      } else if (site.equalsIgnoreCase("fr")) {
        return Country.FR;
      }	else if (site.equalsIgnoreCase("at")) {
        return Country.AT;
      } else if (site.equalsIgnoreCase("ie")) {
        return Country.IE;
      } else if (site.equalsIgnoreCase("ch")) {
    	return Country.CH;
      }	else if (site.equalsIgnoreCase("in")) {
    	return Country.IN;
      }	else {
        return Country.US;
      }
    }
  }

  Country[] countries();
}
