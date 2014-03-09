package com.dzmiter.recognizer.service;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class PlaybackService {

  private Clip clip;

  public PlaybackService(File soundFile) {
    try {
      AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
      clip = AudioSystem.getClip();
      clip.open(stream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PlaybackService(String path) {
    this(new File(path));
  }

  public synchronized void play() {
    clip.setFramePosition(0);
    clip.start();
  }

  public synchronized void stop() {
    if (clip.isRunning()) {
      clip.stop();
      clip.setFramePosition(0);
    }
  }
}