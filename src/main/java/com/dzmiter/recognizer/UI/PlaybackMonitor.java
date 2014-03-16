package com.dzmiter.recognizer.UI;

import com.dzmiter.recognizer.domain.EmptySoundFile;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlaybackMonitor extends JPanel implements Runnable {
  private static final Color INFO_COLOR = new Color(51, 170, 255);
  private static final Font LOADING_FONT = new Font("serif", Font.BOLD, 42);
  private double duration, seconds;
  private boolean isRunning = false;
  private FontMetrics fm28, fm42;
  private EmptySoundFile file;
  private String msg;

  public PlaybackMonitor(double duration, EmptySoundFile file) {
    seconds = 0;
    this.file = file;
    this.duration = duration;
    fm28 = getFontMetrics(new Font("serif", Font.BOLD, 28));
    fm42 = getFontMetrics(LOADING_FONT);
    msg = "Record sound";
  }

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Dimension d = getSize();
    g2.setBackground(new Color(240, 240, 240));
    g2.clearRect(0, 0, d.width, d.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(INFO_COLOR);
    g2.setFont(new Font("serif", Font.BOLD, 24));
    g2.drawString(msg, 5, fm28.getHeight() - 5);
    if (duration > 0) {
      g2.setFont(LOADING_FONT);
      String s = String.valueOf(seconds);
      s = s.substring(0, s.indexOf('.') + 2);
      int strW = (int) fm42.getStringBounds(s, g2).getWidth();
      g2.drawString(s, d.width - strW - 9, fm42.getAscent());

      int num = 30;
      int progress = (int) (seconds / duration * num);
      double ww = ((double) (d.width - 10) / (double) num);
      double hh = (int) (d.height * 0.25);
      double x = 0.0;
      for (; x < progress; x += 1.0) {
        g2.fill(new Rectangle2D.Double(x * ww + 5, d.height - hh - 5, ww - 1, hh));
      }
      g2.setColor(INFO_COLOR.darker());
      for (; x < num; x += 1.0) {
        g2.fill(new Rectangle2D.Double(x * ww + 5, d.height - hh - 5, ww - 1, hh));
      }
    }
  }

  public Thread start() {
    Thread pbThread = new Thread(this);
    isRunning = true;
    pbThread.start();
    return pbThread;
  }

  public void stop() {
    isRunning = false;
  }

  @Override
  public void run() {
    file.startRecording();
    msg = "Recording...";
    while (isRunning) {
      try {
        Thread.sleep(99);
        seconds += 0.1;
        repaint();
        if (duration <= seconds) {
          isRunning = false;
        }
      } catch (Exception e) {
        e.printStackTrace();
        isRunning = false;
      }
    }
    msg = "Completed";
    file.stopRecording();
    repaint();
  }
}