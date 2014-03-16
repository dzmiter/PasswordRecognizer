package com.dzmiter.recognizer.UI;

import com.dzmiter.recognizer.domain.CustomProperties;
import com.dzmiter.recognizer.domain.EmptySoundFile;
import com.dzmiter.recognizer.event.SaveAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
public class RecordFrame extends JFrame implements ActionListener {

  private EmptySoundFile emptySoundFile;
  private PlaybackMonitor playbackMonitor;
  private JButton okButton;
  private JButton cancelButton;
  private SaveAction saveAction;

  public RecordFrame(File file, SaveAction saveAction) {
    this.saveAction = saveAction;
    emptySoundFile = new EmptySoundFile(file);
    setTitle("Record sound");
    setSize(500, 150);
    setLocationRelativeTo(null);
    setResizable(false);
    setLayout(new BorderLayout());
    CustomProperties properties = new CustomProperties("recording.properties");
    int time = Integer.parseInt(properties.getProperty("recordTimeSeconds"));
    playbackMonitor = new PlaybackMonitor(time, emptySoundFile);
    add(BorderLayout.CENTER, playbackMonitor);
    JPanel buttons = new JPanel();
    okButton = new JButton("Start");
    okButton.addActionListener(this);
    cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(this);
    buttons.add(okButton);
    buttons.add(cancelButton);
    add(BorderLayout.SOUTH, buttons);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object source = e.getSource();
    if (source instanceof JButton) {
      JButton button = (JButton) source;
      if (button.getText().equals("Start")) {
        final Thread playbackThread = playbackMonitor.start();
        okButton.setEnabled(false);
        cancelButton.setText("Stop");
        new Thread(new Runnable() {
          @Override
          public void run() {
            try {
              playbackThread.join();
              okButton.setEnabled(true);
              okButton.setText("Save");
              cancelButton.setText("Cancel");
            } catch (InterruptedException e1) {
              e1.printStackTrace();
            }
          }
        }).start();
      } else if (button.getText().equals("Stop")) {
        playbackMonitor.stop();
        okButton.setText("Save");
        okButton.setEnabled(true);
        cancelButton.setText("Cancel");
      } else if (button.getText().equals("Save")) {
        saveAction.doAction();
        dispose();
      } else if (button.getText().equals("Cancel")) {
        emptySoundFile.delete();
        dispose();
      }
    }
  }
}