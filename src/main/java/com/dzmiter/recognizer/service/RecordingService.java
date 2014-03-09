package com.dzmiter.recognizer.service;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecordingService {

  public static final long RECORD_TIME = 5000;
  private File soundFile;
  private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
  private TargetDataLine line;

  public RecordingService(File soundFile) {
    this.soundFile = soundFile;
    if (!soundFile.exists()) try {
      soundFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public RecordingService(String path) {
    this(new File(path));
  }

  public void startRecording() {
    try {
      AudioFormat format = getAudioFormat();
      DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
      if (!AudioSystem.isLineSupported(info)) {
        System.out.println("Line not supported");
        System.exit(0);
      }
      line = (TargetDataLine) AudioSystem.getLine(info);
      line.open(format);
      line.start();
      System.out.println("Start capturing...");
      AudioInputStream ais = new AudioInputStream(line);
      System.out.println("Start recording...");
      AudioSystem.write(ais, fileType, soundFile);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private AudioFormat getAudioFormat() {
    float sampleRate = 8000;
    int sampleSizeInBits = 16;
    int channels = 1;
    return new AudioFormat(sampleRate, sampleSizeInBits,
        channels, true, true);
  }

  public void stopRecording() {
    line.stop();
    line.close();
    System.out.println("Finished");
  }
}