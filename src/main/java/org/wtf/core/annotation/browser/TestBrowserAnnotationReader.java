/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.browser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.wtf.core.annotation.AnnotationReader;
import org.wtf.core.annotation.browser.TestBrowser.Browser;

import org.wtf.core.annotation.browser.TestBrowser;

/**
 * Test timeout annotation reader.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public class TestBrowserAnnotationReader {

	public static Browser[] getIncludedBrowsers(Method javaMethod) {
		TestBrowser annotation = (TestBrowser) AnnotationReader.getValue(
				javaMethod, TestBrowser.class);
		if (annotation == null) {
			return Browser.values();
			//List<Browser> siteList = new ArrayList<Browser>();
			//return siteList.toArray(new Browser[siteList.size()]);
		}
		return annotation.browsers();
	}

	public static Browser[] getExcludedBrowsers(Method javaMethod) {
		ExcludeBrowser annotation = (ExcludeBrowser) AnnotationReader.getValue(
				javaMethod, ExcludeBrowser.class);
		if (annotation == null) {
			List<Browser> siteList = new ArrayList<Browser>();
			return siteList.toArray(new Browser[siteList.size()]);
		}
		return annotation.browsers();
	}
}
