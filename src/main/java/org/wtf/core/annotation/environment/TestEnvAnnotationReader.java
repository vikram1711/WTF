/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.annotation.environment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.wtf.core.annotation.AnnotationReader;
import org.wtf.core.annotation.environment.TestEnv.Env;


/**
 * Test timeout annotation reader.
 * 
 * @author venkatesan.sundramurthy@gmail.com (Venkatesan Sundramurthy)
 */
public class TestEnvAnnotationReader {

  public static Env [] getSites(Method javaMethod) {
    TestEnv annotation = (TestEnv) AnnotationReader.getValue(javaMethod, TestEnv.class);
    if (annotation == null) {
      List<Env> siteList = new ArrayList<Env>();
      return siteList.toArray(new Env[siteList.size()]);
    }
    return annotation.env();
  }
}
