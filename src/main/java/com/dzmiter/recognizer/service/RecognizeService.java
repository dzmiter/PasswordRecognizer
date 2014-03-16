package com.dzmiter.recognizer.service;

import com.dzmiter.recognizer.domain.CustomProperties;
import com.mathworks.toolbox.javabuilder.MWException;
import vText.vTextClass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public class RecognizeService {

  private String minThreshold;
  private String maxThreshold;
  private vTextClass vText;

  public RecognizeService() {
    try {
      CustomProperties properties = new CustomProperties("recognize.properties");
      minThreshold = properties.getProperty("minThreshold");
      maxThreshold = properties.getProperty("maxThreshold");
      vText = new vTextClass();
    } catch (MWException e) {
      e.printStackTrace();
    }
  }

  public int recognizePassword(String sourcePath, String comparePath) {
    try {
      Object[] result = vText.recognize(1, sourcePath, comparePath, minThreshold, maxThreshold);
      return Integer.parseInt(result[0].toString());
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  public void filterNoise(String sourcePath, String filteredPath) {
    try {
      vText.filterbgnoise(1, sourcePath, filteredPath);
    } catch (MWException e) {
      e.printStackTrace();
    }
  }

  public int recognizePasswordWithConclusion(Set<File> setA, Set<File> setB) {
    List<Integer> result = recognizePassword(setA, setB);
    if (result.isEmpty()) return -1;
    int conclusion = 0;
    for (Integer i : result) {
      if (i > 0) conclusion += i;
    }
    return conclusion / result.size();
  }

  public List<Integer> recognizePassword(Set<File> setA, Set<File> setB) {
    List<Integer> result = new ArrayList<Integer>();
    for (File fileA : setA) {
      for (File fileB : setB) {
        result.add(recognizePassword(fileA.getAbsolutePath(), fileB.getAbsolutePath()));
      }
    }
    return result;
  }
}