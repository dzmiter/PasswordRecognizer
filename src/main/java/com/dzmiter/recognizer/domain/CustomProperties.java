package com.dzmiter.recognizer.domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
      InputStream stream;
      try {
        stream = new FileInputStream(propertiesFileName);
      } catch (FileNotFoundException fnEx) {
        stream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
      }
      defaults.load(stream);
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}