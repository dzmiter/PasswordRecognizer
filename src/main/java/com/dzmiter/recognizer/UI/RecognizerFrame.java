package com.dzmiter.recognizer.UI;

import com.dzmiter.recognizer.domain.CustomProperties;
import com.dzmiter.recognizer.domain.EmptySoundFile;
import com.dzmiter.recognizer.domain.IndexedSet;
import com.dzmiter.recognizer.domain.SoundFile;
import com.dzmiter.recognizer.event.SaveAndOptimizeAction;
import com.dzmiter.recognizer.service.RecognizeService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class RecognizerFrame extends JFrame {

  private List<JTable> soundTables;
  private SamplingGraph samplingGraph;
  private SoundFile selectedSoundFile;
  private JButton playStopButton;
  private RecognizeService recognizeService;
  private JPanel contentPane;

  public RecognizerFrame() throws Exception {
    recognizeService = new RecognizeService();
    soundTables = new ArrayList<JTable>();
    setTitle("Password Recognizer");
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/img/microphone.png")));
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(640, 480));
    setSize(new Dimension(1000, 600));
    setLocationRelativeTo(null);
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(contentPane);
    contentPane.setLayout(new BorderLayout());

    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);

    JMenu mnFile = new JMenu("File");
    menuBar.add(mnFile);

    JMenuItem mntmRecordSound = new JMenuItem("Record sound");
    mnFile.add(mntmRecordSound);
    mntmRecordSound.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        File fileToRecord = new File(EmptySoundFile.prepareFilePath(false));
        RecordFrame recordFrame = new RecordFrame(fileToRecord, null, new SaveAndOptimizeAction(fileToRecord));
        recordFrame.setVisible(true);
      }
    });

    JMenuItem mntmConfig = new JMenuItem("Config");
    mntmConfig.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ConfigFrame configFrame = new ConfigFrame();
        configFrame.setVisible(true);
      }
    });
    mnFile.add(mntmConfig);

//    JMenuItem mntmSearch = new JMenuItem("Search");
//    mnFile.add(mntmSearch);

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

    /*JMenuItem mntmHelp = new JMenuItem("Help");
    mntmHelp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(contentPane, "Help text!", "Help", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    mnHelp.add(mntmHelp);*/

    JMenuItem mntmAbout = new JMenuItem("About");
    final CustomProperties commonProperties = new CustomProperties("common.properties");
    mntmAbout.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(contentPane,
            commonProperties.getProperty("copyright"),
            "About", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    mnHelp.add(mntmAbout);

    final CustomProperties soundProperties = new CustomProperties("sound.properties");
    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    fileChooser.setMultiSelectionEnabled(true);
    fileChooser.setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        }
        String name = f.getName();
        List<String> allowedExtensions = Arrays.asList(soundProperties.getProperty("allowedFormats").split(","));
        return allowedExtensions.contains(name.substring(name.lastIndexOf('.') + 1));
      }

      @Override
      public String getDescription() {
        return soundProperties.getProperty("allowedFormats");
      }
    });

    SoundTable leftSounds = new SoundTable(getListSelectionListener(), fileChooser);
    contentPane.add(BorderLayout.WEST, leftSounds);

    JPanel amplitudes = initMainPanel();
    contentPane.add(BorderLayout.CENTER, amplitudes);

    SoundTable rightSounds = new SoundTable(getListSelectionListener(), fileChooser);
    contentPane.add(BorderLayout.EAST, rightSounds);

    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        samplingGraph.drawSamplingGraph(selectedSoundFile);
      }
    });

    soundTables.add(leftSounds.getSoundTable());
    soundTables.add(rightSounds.getSoundTable());
  }

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          JFrame preloader = PreLoader.showPreloader();
          RecognizerFrame frame = new RecognizerFrame();
          preloader.dispose();
          frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private JPanel initMainPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    samplingGraph = new SamplingGraph();
    panel.add(BorderLayout.CENTER, samplingGraph);
    JPanel buttons = new JPanel();
    playStopButton = new JButton("Play / Stop");
    playStopButton.setEnabled(false);
    playStopButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          selectedSoundFile.togglePlayStop();
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    });
    buttons.add(BorderLayout.SOUTH, playStopButton);
    final JButton compareButton = new JButton("Compare");
    compareButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        compareButton.setEnabled(false);
        boolean playEnabled = playStopButton.isEnabled();
        playStopButton.setEnabled(false);
        JFrame preloader = PreLoader.showPreloader();
        List<SoundTable> tables = getAllTables();
        Set<File> setA = new HashSet<File>();
        Set<File> setB = new HashSet<File>();
        for (int i = 0; i < tables.size(); i++) {
          Set<File> files = tables.get(i).getSounds();
          if (i % 2 == 0) {
            setA.addAll(files);
          } else {
            setB.addAll(files);
          }
        }
        recognizeService.refreshProperties();
        int conclusion = recognizeService.recognizePasswordWithConclusion(setA, setB);
        preloader.dispose();
        compareButton.setEnabled(true);
        playStopButton.setEnabled(playEnabled);
        JOptionPane.showMessageDialog(contentPane, "Match: " + conclusion + " %", "Conclusion", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    buttons.add(compareButton);
    panel.add(BorderLayout.SOUTH, buttons);
    return panel;
  }

  private ListSelectionListener getListSelectionListener() {
    return new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent event) {
        if (!event.getValueIsAdjusting()) {
          return;
        }
        ListSelectionModel current = (ListSelectionModel) event.getSource();
        List<JTable> tables = getSoundTablesExceptCurrentBy(current);
        for (JTable table : tables) {
          table.clearSelection();
        }
        int index = current.getMinSelectionIndex();
        playStopButton.setEnabled(index >= 0);
        SoundTable currentPane = getSoundTableBy(current);
        IndexedSet<File> sounds = currentPane.getSounds();
        if (selectedSoundFile != null) selectedSoundFile.stop();
        if (index >= 0) {
          File currentFile = sounds.get(index);
          selectedSoundFile = new SoundFile(currentFile);
        } else {
          selectedSoundFile = null;
        }
        samplingGraph.drawSamplingGraph(selectedSoundFile);
      }
    };
  }

  private List<SoundTable> getAllTables() {
    List<SoundTable> result = new ArrayList<SoundTable>();
    for (JTable table : this.soundTables) {
      SoundTable soundTable = (SoundTable) table.getParent().getParent().getParent();
      result.add(soundTable);
    }
    return result;
  }

  private SoundTable getSoundTableBy(ListSelectionModel currentModel) {
    for (JTable table : this.soundTables) {
      if (table.getSelectionModel().equals(currentModel)) {
        return (SoundTable) table.getParent().getParent().getParent();
      }
    }
    throw new RuntimeException("Not found!");
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