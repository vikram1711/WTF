/**
 * Copyright (C) 2014 WTF org.
 */

package org.wtf.core.feature.lint.rhino.css;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IStringConverter;

public class StringListConverter implements IStringConverter <List <String>> {
  public List <String> convert(String value) {
    List <String> stringList = new ArrayList <String>();
    for (String str : value.split(",")) {
      stringList.add(str);
    }
    return stringList;
  }
}