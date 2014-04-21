/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.environment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Sites annotation.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
@Retention(value = RetentionPolicy.RUNTIME)
public @interface TestEnv {

  Env[] env();

  public static enum Env {
    QA, PRE_PROD, PROD;
    
    public static Env getEnvFromEnvString(String envString) {
	    if (envString.equalsIgnoreCase("qa")) {
	      return Env.QA;
	    } else if (envString.equalsIgnoreCase("prod")) {
	      return Env.PROD;
	    }    if (envString.equalsIgnoreCase("preprod")) {
	      return Env.PRE_PROD;
	    } else {
	      return Env.QA;
	    }
    }
  }
}
