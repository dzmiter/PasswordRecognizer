package com.dzmiter.recognizer.event;

import com.dzmiter.recognizer.domain.CustomProperties;
import com.dzmiter.recognizer.domain.EmptySoundFile;
import com.dzmiter.recognizer.domain.WavFile;
import com.dzmiter.recognizer.service.RecognizeService;

import java.io.File;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public class SaveAndOptimizeAction implements SaveAction {

  protected File file;

  public SaveAndOptimizeAction(File file) {
    this.file = file;
  }

  protected File optimizeAndGetFile() {
    File filtered = new File(EmptySoundFile.prepareFilePath(true));
    RecognizeService recognizeService = new RecognizeService();
    recognizeService.filterNoise(file.getAbsolutePath(), filtered.getAbsolutePath());
    file.delete();
    File output = null;
    try {
      WavFile wavFile = WavFile.openWavFile(filtered);
      output = new File(EmptySoundFile.prepareFilePath(true));
      CustomProperties properties = new CustomProperties("recognize.properties");
      int maxFramesCount = Integer.parseInt(properties.getProperty("maxFramesCount"));
      wavFile.trimSilence(output, maxFramesCount);
      wavFile.close();
      filtered.delete();
    } catch (Exception e) {
      e.printStackTrace();
      if (output != null && output.exists()) {
        output.delete();
      }
      output = filtered;
    }
    return output;
  }

  @Override
  public void doAction() {
    optimizeAndGetFile();
  }
}