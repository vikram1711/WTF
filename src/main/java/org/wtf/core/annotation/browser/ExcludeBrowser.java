/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.browser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.wtf.core.annotation.browser.TestBrowser.Browser;



/**
 * Sites annotation.
 * 
 * @author jevasudevan@ebay.com (Jeganathan Vasudevan)
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ExcludeBrowser {
  Browser[] browsers();
}
