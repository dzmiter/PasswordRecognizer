package com.dzmiter.recognizer.UI;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class PreLoader extends JPanel {

  private Image image;

  public PreLoader() {
    initialize();
  }

  public static JFrame showPreloader() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    frame.setUndecorated(true);
    frame.add(new PreLoader());
    frame.pack();
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    return frame;
  }

  public final void initialize() {
    URL preloader = getClass().getClassLoader().getResource("img/preloader.gif");
    ImageIcon imageIcon = new ImageIcon(preloader);
    image = imageIcon.getImage();
    setPreferredSize(new Dimension(150, 150));
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, 150, 150, this);
  }
}