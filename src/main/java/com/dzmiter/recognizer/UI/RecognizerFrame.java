package com.dzmiter.recognizer.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecognizerFrame extends JFrame {

  private List<JTable> tables;

  public RecognizerFrame() throws Exception {
    tables = new ArrayList<JTable>();
    setTitle("Password Recognizer");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(1000, 600));
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
    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(contentPane);
    contentPane.setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.weighty = 1.0;
    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.1;
    c.gridx = 0;
    c.gridy = 0;
    JPanel rightSounds = buildSoundTablePanel();
    contentPane.add(rightSounds, c);

    c = new GridBagConstraints();
    c.weighty = 1.0;

    c.fill = GridBagConstraints.BOTH;
    c.weightx = 0.8;
    c.gridx = 1;
    c.gridy = 0;
    JPanel amplitudes = buildMiddlePanel();
    contentPane.add(amplitudes, c);

    JPanel panel = buildSoundTablePanel();
    GridBagConstraints gbc_panel = new GridBagConstraints();
    gbc_panel.weightx = 0.1;
    gbc_panel.weighty = 1.0;
    gbc_panel.fill = GridBagConstraints.BOTH;
    gbc_panel.gridx = 2;
    gbc_panel.gridy = 0;
    contentPane.add(panel, gbc_panel);
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
    GridBagLayout gbl_amplitudes = new GridBagLayout();
    amplitudes.setLayout(gbl_amplitudes);

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

    JButton playStop = new JButton("Play / Stop");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 0;
    c.gridy = 2;
    c.weightx = 1;
    c.weighty = 0.1;
    amplitudes.add(playStop, c);

    JButton compare = new JButton("Compare");
    c = new GridBagConstraints();
    c.fill = GridBagConstraints.CENTER;
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 1;
    c.gridy = 2;
    c.weightx = 1;
    c.weighty = 0.1;
    amplitudes.add(compare, c);

    return amplitudes;
  }

  private JPanel buildSoundTablePanel() {
    final JPanel rightSounds = new JPanel();
    GridBagLayout gbl_rightSounds = new GridBagLayout();
    gbl_rightSounds.columnWeights = new double[]{Double.MIN_VALUE};
    gbl_rightSounds.rowWeights = new double[]{Double.MIN_VALUE};
    rightSounds.setLayout(gbl_rightSounds);

    DefaultTableModel dm = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };
    dm.setDataVector(new Object[][]{{"1"},
        {"2"}}, new Object[]{"Sound"});

    final JTable tableRight = new JTable(dm);
    tables.add(tableRight);
    tableRight.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      public void valueChanged(ListSelectionEvent event) {
        for (JTable table : tables) {
          table.clearSelection();
        }
      }
    });
    tableRight.setRowHeight(30);
    tableRight.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.BOTH;
    c.anchor = GridBagConstraints.NORTH;
    c.gridx = 0;
    c.gridwidth = 3;
    c.gridy = 0;
    c.weightx = 1;
    JScrollPane rightSoundsPane = new JScrollPane(tableRight);
    rightSounds.add(rightSoundsPane, c);
    JButton addRight = new JButton("Add");
    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 1;
    rightSounds.add(addRight, c);
    JButton recordRight = new JButton("Record");
    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 1;
    c.gridy = 1;
    c.weightx = 1;
    rightSounds.add(recordRight, c);

    JButton btnDelete = new JButton("Delete");
    c = new GridBagConstraints();
    c.anchor = GridBagConstraints.SOUTH;
    c.gridx = 2;
    c.gridy = 1;
    c.weightx = 1;
    rightSounds.add(btnDelete, c);
    return rightSounds;
  }

}