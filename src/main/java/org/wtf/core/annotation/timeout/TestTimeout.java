/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.timeout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Test timeout annotation.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TestTimeout {
  int seconds();
}
