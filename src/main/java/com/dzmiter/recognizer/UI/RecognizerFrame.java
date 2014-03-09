package com.dzmiter.recognizer.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RecognizerFrame extends JFrame {

  /**
   * Create the frame.
   *
   * @throws UnsupportedLookAndFeelException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws ClassNotFoundException
   */
  public RecognizerFrame() throws Exception {
    setTitle("Password Recognizer");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(800, 600));
    setLocationRelativeTo(null);
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);

    JMenuItem mntmRecordSound = new JMenuItem("Record sound");
    mnFile.add(mntmRecordSound);

    JMenuItem mntmConfig = new JMenuItem("Config");
    mnFile.add(mntmConfig);

    JMenuItem mntmSearch = new JMenuItem("Search");
    mnFile.add(mntmSearch);

    JMenuItem mntmExit = new JMenuItem("Exit");
    mnFile.add(mntmExit);

    JMenu mnHelp = new JMenu("Help");
    menuBar.add(mnHelp);

    JMenuItem mntmHelp = new JMenuItem("Help");
    mnHelp.add(mntmHelp);

    JMenuItem mntmAbout = new JMenuItem("About");
    mnHelp.add(mntmAbout);

    JPanel contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    GridBagLayout gbl_contentPane = new GridBagLayout();
    gbl_contentPane.columnWidths = new int[]{0, 0};
    gbl_contentPane.rowHeights = new int[]{0, 0};
    contentPane.setLayout(gbl_contentPane);

    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(0, 0, 5, 5);
    c.weighty = 1.0;
    JPanel rightSounds = new JPanel();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.3;
    c.gridx = 0;
    c.gridy = 0;
    contentPane.add(rightSounds, c);
    GridBagLayout gbl_rightSounds = new GridBagLayout();
    gbl_rightSounds.columnWidths = new int[]{0, 0};
    gbl_rightSounds.rowHeights = new int[]{0, 0};
    gbl_rightSounds.columnWeights = new double[]{1.0, Double.MIN_VALUE};
    gbl_rightSounds.rowWeights = new double[]{1.0, Double.MIN_VALUE};
    rightSounds.setLayout(gbl_rightSounds);
    JTable tableRight = new JTable();
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridwidth = 2;
    c.gridy = 0;
    rightSounds.add(tableRight, c);
    JButton addRight = new JButton("Add");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.VERTICAL;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    rightSounds.add(addRight, c);
    JButton recordRight = new JButton("Record");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.VERTICAL;
    c.weightx = 1;
    c.gridx = 1;
    c.gridy = 1;
    rightSounds.add(recordRight, c);

    c = new GridBagConstraints();
    JPanel amplitudes = new JPanel();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 1;
    c.gridx = 2;
    c.gridy = 0;
    contentPane.add(amplitudes, c);
    GridBagLayout gbl_amplitudes = new GridBagLayout();
    gbl_amplitudes.columnWidths = new int[]{0};
    gbl_amplitudes.rowHeights = new int[]{0};
    gbl_amplitudes.columnWeights = new double[]{Double.MIN_VALUE};
    gbl_amplitudes.rowWeights = new double[]{Double.MIN_VALUE};
    amplitudes.setLayout(gbl_amplitudes);

    //TODO: will be replaced
    JPanel equalizer = new JPanel();
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTH;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    c.gridheight = 1;
    amplitudes.add(equalizer, c);

    JButton playStop = new JButton("Play / Stop");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1;
    c.gridheight = 1;
    amplitudes.add(playStop, c);

    JButton compare = new JButton("Compare");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.gridx = 1;
    c.gridy = 2;
    c.weightx = 1;
    c.gridheight = 1;
    amplitudes.add(compare, c);

    JTextArea info = new JTextArea();
    info.setEditable(false);
    info.setText("TEXT");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    c.gridwidth = 2;
    c.gridheight = 1;
    amplitudes.add(info, c);


    c = new GridBagConstraints();
    c.insets = new Insets(0, 0, 5, 5);
    JPanel leftSounds = new JPanel();
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.3;
    c.gridx = 3;
    c.gridy = 0;
    contentPane.add(leftSounds, c);
    GridBagLayout gbl_leftSounds = new GridBagLayout();
    gbl_leftSounds.columnWidths = new int[]{0};
    gbl_leftSounds.rowHeights = new int[]{0};
    gbl_leftSounds.columnWeights = new double[]{Double.MIN_VALUE};
    gbl_leftSounds.rowWeights = new double[]{Double.MIN_VALUE};
    leftSounds.setLayout(gbl_leftSounds);
    JTable tableLeft = new JTable();
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 0;
    c.gridwidth = 2;
    c.gridy = 0;
    leftSounds.add(tableLeft, c);
    JButton addLeft = new JButton("Add");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.VERTICAL;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    leftSounds.add(addLeft, c);
    JButton recordLeft = new JButton("Record");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.VERTICAL;
    c.weightx = 1;
    c.gridx = 1;
    c.gridy = 1;
    leftSounds.add(recordLeft, c);
  }

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          RecognizerFrame frame = new RecognizerFrame();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

}
