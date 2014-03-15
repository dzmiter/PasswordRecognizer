package com.dzmiter.recognizer.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class PlaybackMonitor extends JPanel implements Runnable {
  private static final Color JFC_BLUE = new Color(204, 204, 255);
  private static final Color JFC_DARK_BLUE = JFC_BLUE.darker();
  private static final Font FONT_24 = new Font("serif", Font.BOLD, 24);
  private static final Font FONT_42 = new Font("serif", Font.BOLD, 42);
  private double duration, seconds;
  private boolean isRunning = false;
  private FontMetrics fm28, fm42;

  public PlaybackMonitor(double duration) {
    seconds = 0;
    this.duration = duration;
    fm28 = getFontMetrics(new Font("serif", Font.BOLD, 28));
    fm42 = getFontMetrics(FONT_42);
  }

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Dimension d = getSize();
    g2.setBackground(Color.BLACK);
    g2.clearRect(0, 0, d.width, d.height);
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setColor(JFC_BLUE);
    g2.setFont(FONT_24);
    g2.drawString("Recording...", 5, fm28.getHeight() - 5);
    if (duration > 0) {
      g2.setFont(FONT_42);
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
      g2.setColor(JFC_DARK_BLUE);
      for (; x < num; x += 1.0) {
        g2.fill(new Rectangle2D.Double(x * ww + 5, d.height - hh - 5, ww - 1, hh));
      }
    }
  }

  public void start() {
    Thread pbThread = new Thread(this);
    isRunning = true;
    pbThread.start();
  }

  public void stop() {
    isRunning = false;
  }

  @Override
  public void run() {
    while (isRunning) {
      try {
        Thread.sleep(99);
        seconds += 0.1;
      } catch (Exception e) {
        break;
      }
      repaint();
      if (duration <= seconds) {
        break;
      }
    }
  }
}