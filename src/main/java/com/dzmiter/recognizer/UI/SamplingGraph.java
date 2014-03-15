package com.dzmiter.recognizer.UI;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

class SamplingGraph extends JPanel {

  private static final Color LINE_COLOR = Color.GREEN;
  private static final Font INFO_FONT = new Font("serif", Font.PLAIN, 14);
  private AudioInputStream audioInputStream;
  private String fileName;
  private String errStr;
  private double duration;
  private List<Line2D.Double> lines;

  public SamplingGraph() {
    lines = new ArrayList<Line2D.Double>();
    setBackground(new Color(18, 152, 212));
  }

  public void paint(Graphics g) {
    Dimension d = getSize();
    int w = d.width;
    int h = d.height;
    int infoPad = 15;

    Graphics2D g2 = (Graphics2D) g;
    g2.setBackground(getBackground());
    g2.clearRect(0, 0, w, h);
    g2.setColor(Color.white);
    g2.fillRect(0, h - infoPad, w, infoPad);

    if (errStr != null) {
      g2.setColor(LINE_COLOR);
      g2.setFont(new Font("serif", Font.BOLD, 18));
      g2.drawString("ERROR", 5, 20);
      AttributedString as = new AttributedString(errStr);
      as.addAttribute(TextAttribute.FONT, INFO_FONT, 0, errStr.length());
      AttributedCharacterIterator aci = as.getIterator();
      FontRenderContext frc = g2.getFontRenderContext();
      LineBreakMeasurer lbm = new LineBreakMeasurer(aci, frc);
      float x = 5, y = 25;
      lbm.setPosition(0);
      while (lbm.getPosition() < errStr.length()) {
        TextLayout tl = lbm.nextLayout(w - x - 5);
        if (!tl.isLeftToRight()) {
          x = w - tl.getAdvance();
        }
        tl.draw(g2, x, y += tl.getAscent());
        y += tl.getDescent() + tl.getLeading();
      }
    } else {
      g2.setColor(Color.BLACK);
      g2.setFont(INFO_FONT);
      g2.drawString("File: " + fileName + "  Length: " + String.valueOf(duration), 3, h - 3);

      if (audioInputStream != null) {
        g2.setColor(LINE_COLOR);
        for (int i = 1; i < lines.size(); i++) {
          g2.draw(lines.get(i));
        }
      }
    }
  }

  public void drawSamplingGraph(File file) {
    if (file == null || !file.isFile()) {
      lines.clear();
      repaint();
      return;
    }
    try {
      errStr = null;
      audioInputStream = AudioSystem.getAudioInputStream(file);
      fileName = file.getName();
      long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
      duration = milliseconds / 1000.0;
      createWaveForm();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  private void createWaveForm() throws IOException {
    lines.clear();
    AudioFormat format = audioInputStream.getFormat();
    byte[] audioBytes = new byte[(int) (audioInputStream.getFrameLength() * format.getFrameSize())];
    audioInputStream.read(audioBytes);

    Dimension d = getSize();
    int w = d.width != 0 ? d.width : 1;
    int h = d.height - 15;
    int[] audioData = null;
    if (format.getSampleSizeInBits() == 16) {
      int lengthInSamples = audioBytes.length / 2;
      audioData = new int[lengthInSamples];
      if (format.isBigEndian()) {
        for (int i = 0; i < lengthInSamples; i++) {
                         /* First byte is MSB (high order) */
          int MSB = (int) audioBytes[2 * i];
                         /* Second byte is LSB (low order) */
          int LSB = (int) audioBytes[2 * i + 1];
          audioData[i] = MSB << 8 | (255 & LSB);
        }
      } else {
        for (int i = 0; i < lengthInSamples; i++) {
                         /* First byte is LSB (low order) */
          int LSB = (int) audioBytes[2 * i];
                         /* Second byte is MSB (high order) */
          int MSB = (int) audioBytes[2 * i + 1];
          audioData[i] = MSB << 8 | (255 & LSB);
        }
      }
    } else if (format.getSampleSizeInBits() == 8) {
      int lengthInSamples = audioBytes.length;
      audioData = new int[lengthInSamples];
      if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
        for (int i = 0; i < audioBytes.length; i++) {
          audioData[i] = audioBytes[i];
        }
      } else {
        for (int i = 0; i < audioBytes.length; i++) {
          audioData[i] = audioBytes[i] - 128;
        }
      }
    }

    int frames_per_pixel = audioBytes.length / format.getFrameSize() / w;
    byte my_byte;
    double y_last = 0;
    int numChannels = format.getChannels();
    for (double x = 0; x < w && audioData != null; x++) {
      int idx = (int) (frames_per_pixel * numChannels * x);
      if (format.getSampleSizeInBits() == 8) {
        my_byte = (byte) audioData[idx];
      } else {
        my_byte = (byte) (128 * audioData[idx] / 32768);
      }
      double y_new = (double) (h * (128 - my_byte) / 256);
      lines.add(new Line2D.Double(x, y_last, x, y_new));
      y_last = y_new;
    }

    repaint();
  }
}