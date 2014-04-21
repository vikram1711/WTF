/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.site;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.wtf.core.annotation.AnnotationReader;
import org.wtf.core.annotation.site.TestSite.Country;


/**
 * Test timeout annotation reader.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 * @author jevasudevan@ebay.com (Jeganathan Vasudevan)
 */
public class TestSiteAnnotationReader {

	public static Country[] getIncludedSites(Method javaMethod) {
		TestSite annotation = (TestSite) AnnotationReader.getValue(javaMethod,
				TestSite.class);
		if (annotation == null) {
			return Country.values();
			/*List<Country> siteList = new ArrayList<Country>();
			return siteList.toArray(new Country[siteList.size()]);*/
		}
		return annotation.countries();
	}

	public static Country[] getExcludedSites(Method javaMethod) {
		ExcludeSite annotation = (ExcludeSite) AnnotationReader.getValue(javaMethod,
				ExcludeSite.class);
		if (annotation == null) {
			List<Country> siteList = new ArrayList<Country>();
			return siteList.toArray(new Country[siteList.size()]);
		}
		return annotation.countries();
	}
}
