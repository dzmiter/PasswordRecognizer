package com.dzmiter.recognizer.domain;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class EmptySoundFile extends File implements Runnable {

  private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
  private TargetDataLine line;

  public EmptySoundFile(File soundFile) {
    this(soundFile.getAbsolutePath());
  }

  public EmptySoundFile(String path) {
    super(path);
    if (!this.exists()) try {
      this.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String prepareFilePath(boolean isFiltered) {
    StringBuilder sb = new StringBuilder(System.getProperty("user.dir"));
    sb.append(File.separator);
    sb.append("TEMP");
    sb.append(File.separator);
    File file = new File(sb.toString());
    file.mkdirs();
    sb.append("Recording_");
    sb.append(new Date().getTime());
    if (isFiltered) {
      sb.append("_filtered");
    }
    sb.append(".wav");
    return sb.toString();
  }

  public void startRecording() {
    Thread current = new Thread(this);
    current.start();
  }

  private AudioFormat getAudioFormat() {
    CustomProperties properties = new CustomProperties("recording.properties");
    float sampleRate = Float.parseFloat(properties.getProperty("sampleRate"));
    int sampleSizeInBits = Integer.parseInt(properties.getProperty("sampleSizeInBits"));
    int channels = Integer.parseInt(properties.getProperty("channels"));
    boolean signed = Boolean.parseBoolean(properties.getProperty("signed"));
    boolean bigEndian = Boolean.parseBoolean(properties.getProperty("bigEndian"));
    return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
  }

  public void stopRecording() {
    line.stop();
    line.close();
  }

  @Override
  public void run() {
    try {
      AudioFormat format = getAudioFormat();
      DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
      if (!AudioSystem.isLineSupported(info)) {
        System.out.println("Line not supported");
        return;
      }
      line = (TargetDataLine) AudioSystem.getLine(info);
      line.open(format);
      line.start();
      AudioInputStream ais = new AudioInputStream(line);
      AudioSystem.write(ais, fileType, this);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}