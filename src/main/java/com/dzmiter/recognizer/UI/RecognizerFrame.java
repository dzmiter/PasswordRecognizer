package com.dzmiter.recognizer.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class RecognizerFrame extends JFrame {

  private List<JTable> soundTables;

  public RecognizerFrame() throws Exception {
    soundTables = new ArrayList<JTable>();
    setTitle("Password Recognizer");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(800, 600));
    setLocationRelativeTo(null);
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    final JPanel contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(contentPane);
    contentPane.setLayout(new GridBagLayout());

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
    mntmExit.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    });
    mnFile.add(mntmExit);

    JMenu mnHelp = new JMenu("Help");
    menuBar.add(mnHelp);

    JMenuItem mntmHelp = new JMenuItem("Help");
    mntmHelp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(contentPane, "Help text!", "Help", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    mnHelp.add(mntmHelp);

    JMenuItem mntmAbout = new JMenuItem("About");
    mntmAbout.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(contentPane, "This is password recognizer", "About", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    mnHelp.add(mntmAbout);

    GridBagConstraints c = new GridBagConstraints();
    c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.2;
    c.gridx = 0;
    c.gridy = 0;
    SoundTable rightSounds = new SoundTable(getListSelectionListener());
    contentPane.add(rightSounds, c);

    c = new GridBagConstraints();
    c.weighty = 1.0;

    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.6;
    c.gridx = 1;
    c.gridy = 0;
    JPanel amplitudes = buildMiddlePanel();
    contentPane.add(amplitudes, c);

    SoundTable leftSounds = new SoundTable(getListSelectionListener());
    c = new GridBagConstraints();
    c.weightx = 0.2;
    c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.gridx = 2;
    c.gridy = 0;
    contentPane.add(leftSounds, c);

    soundTables.add(leftSounds.getSoundTable());
    soundTables.add(rightSounds.getSoundTable());
  }

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

  private JPanel buildMiddlePanel() {
    JPanel amplitudes = new JPanel();
    amplitudes.setLayout(new GridBagLayout());

    //TODO: will be replaced
    JPanel equalizer = new JPanel();
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTH;
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    c.weightx = 1;
    c.weighty = 0.5;
    amplitudes.add(equalizer, c);

    JPanel htmlPanel = new JPanel();
    htmlPanel.setBorder(BorderFactory.createTitledBorder("HTML"));

    String text = "<html><h2>What is Google Labs?</h2>" +
        "<font face=’verdana’ size = 2>" +
        " Google Labs is a playground <br>" +
        " where our more adventurous users can play around with <br>" +
        " prototypes of some of our wild and crazy ideas and <br>" +
        " offer feedback directly to the engineers who developed<br>" +
        " them. Please note that Labs is the first phase in <br>" +
        " a lengthy product development process and none of this <br>" +
        " stuff is guaranteed to make it onto Google.com. <br>" +
        " While some of our crazy ideas might grow into the <br>" +
        " next Gmail or iGoogle, others might turn out to be, <br>" +
        " well, just plain crazy.</html>";

    Font font = new Font(null, Font.PLAIN, 10);

    JLabel htmlLabel = new JLabel();
    htmlLabel.setText(text);
    htmlLabel.setFont(font);
    htmlPanel.add(htmlLabel);
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;
    c.anchor = GridBagConstraints.NORTH;
    c.gridx = 0;
    c.gridy = 1;
    c.gridwidth = 2;
    c.weightx = 1;
    c.weighty = 0.4;
    amplitudes.add(htmlPanel, c);

    JButton playStopButton = new JButton("Play / Stop");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1;
    c.weighty = 0.1;
    amplitudes.add(playStopButton, c);

    JButton compareButton = new JButton("Compare");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 1;
    c.gridy = 2;
    c.weightx = 1;
    c.weighty = 0.1;
    amplitudes.add(compareButton, c);

    return amplitudes;
  }

  private ListSelectionListener getListSelectionListener() {
    return new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) return;
        ListSelectionModel current = (ListSelectionModel) event.getSource();
        List<JTable> tables = getSoundTablesExceptCurrentBy(current);
        for (JTable table : tables) {
          table.clearSelection();
        }
      }
    };
  }

  private List<JTable> getSoundTablesExceptCurrentBy(ListSelectionModel currentModel) {
    List<JTable> soundTables = new ArrayList<JTable>();
    for (JTable table : this.soundTables) {
      if (!table.getSelectionModel().equals(currentModel)) {
        soundTables.add(table);
      }
    }
    return soundTables;
  }
}