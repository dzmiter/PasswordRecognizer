package com.dzmiter.recognizer.domain;

import java.io.IOException;
import java.util.Properties;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public class CustomProperties extends Properties {
  public CustomProperties(String propertiesFileName) {
    defaults = new Properties();
    try {
      defaults.load(getClass().getClassLoader().getResourceAsStream(propertiesFileName));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}