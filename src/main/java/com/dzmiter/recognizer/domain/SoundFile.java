package com.dzmiter.recognizer.domain;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundFile extends File {

  private Clip clip;

  public SoundFile(String path) {
    super(path);
    try {
      AudioInputStream stream = AudioSystem.getAudioInputStream(super.getAbsoluteFile());
      clip = AudioSystem.getClip();
      clip.open(stream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public SoundFile(File soundFile) {
    this(soundFile.getAbsolutePath());
  }

  public synchronized void play() {
    clip.setFramePosition(0);
    clip.start();
  }

  public synchronized boolean isPlaying() {
    return clip.isRunning();
  }

  public synchronized void togglePlayStop() {
    if (clip.isRunning()) {
      stop();
    } else {
      play();
    }
  }

  public synchronized void stop() {
    if (clip.isRunning()) {
      clip.stop();
      clip.setFramePosition(0);
    }
  }
}