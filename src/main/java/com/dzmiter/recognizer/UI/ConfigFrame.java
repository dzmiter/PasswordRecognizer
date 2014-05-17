package com.dzmiter.recognizer.UI;

import com.dzmiter.recognizer.domain.CustomProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public class ConfigFrame extends JFrame {

  private CustomProperties properties;
  private JTextField minThreshold;
  private JTextField maxThreshold;

  public ConfigFrame() {
    properties = new CustomProperties("recognize.properties");

    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/config.png")));
    setTitle("Recognizer configuration");
    setAlwaysOnTop(true);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setSize(450, 300);
    setResizable(false);
    setLocationRelativeTo(null);

    JPanel contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JLabel lblMinThreshold = new JLabel("Min threshold:");
    lblMinThreshold.setFont(new Font("Times New Roman", Font.PLAIN, 14));
    lblMinThreshold.setBounds(10, 45, 100, 14);
    contentPane.add(lblMinThreshold);

    minThreshold = new JTextField();
    minThreshold.setBounds(120, 42, 304, 20);
    minThreshold.setText(properties.getProperty("minThreshold"));
    contentPane.add(minThreshold);
    minThreshold.setColumns(10);

    JLabel lblMaxThreshold = new JLabel("Max threshold:");
    lblMaxThreshold.setFont(new Font("Times New Roman", Font.PLAIN, 14));
    lblMaxThreshold.setBounds(10, 70, 100, 14);
    contentPane.add(lblMaxThreshold);

    maxThreshold = new JTextField();
    maxThreshold.setBounds(120, 67, 304, 20);
    maxThreshold.setText(properties.getProperty("maxThreshold"));
    contentPane.add(maxThreshold);
    maxThreshold.setColumns(10);

    JLabel lblRecognizerConfiguration = new JLabel("Recognizer configuration");
    lblRecognizerConfiguration.setFont(new Font("Plantagenet Cherokee", Font.PLAIN, 14));
    lblRecognizerConfiguration.setBounds(120, 11, 304, 20);
    contentPane.add(lblRecognizerConfiguration);

    JButton btnCancel = new JButton("Cancel");
    btnCancel.setBounds(334, 228, 90, 23);
    btnCancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
    contentPane.add(btnCancel);

    JButton btnSave = new JButton("Save");
    btnSave.setBounds(234, 228, 90, 23);
    btnSave.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        properties.setProperty("minThreshold", minThreshold.getText());
        properties.setProperty("maxThreshold", maxThreshold.getText());
        properties.setProperty("maxFramesCount", properties.getProperty("maxFramesCount"));
        OutputStream out;
        try {
          out = new FileOutputStream("recognize.properties");
          properties.store(out, null);
          out.close();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        dispose();
      }
    });
    contentPane.add(btnSave);
  }
}