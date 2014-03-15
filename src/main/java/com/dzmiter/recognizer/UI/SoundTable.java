package com.dzmiter.recognizer.UI;

import com.dzmiter.recognizer.domain.CustomProperties;
import com.dzmiter.recognizer.domain.LinkedHashSetWithGet;
import com.dzmiter.recognizer.domain.SetWithGet;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
class SoundTable extends JPanel implements ActionListener {

  private TableModel dataModel;
  private SetWithGet<File> sounds;
  private JTable table;
  private JFileChooser fileChooser;
  private CustomProperties soundProperties;

  public SoundTable(ListSelectionListener selectionListener) {
    fileChooser = new JFileChooser(System.getProperty("user.dir"));
    fileChooser.setMultiSelectionEnabled(true);
    sounds = new LinkedHashSetWithGet<File>();
    soundProperties = new CustomProperties("sound.properties");
    setLayout(new BorderLayout());

    final String[] names = {"#", "Name"};

    dataModel = new AbstractTableModel() {
      public int getColumnCount() {
        return names.length;
      }

      public int getRowCount() {
        return sounds.size();
      }

      public String getValueAt(int row, int col) {
        if (col == 0) {
          return String.valueOf(row + 1);
        } else if (col == 1) {
          File file = sounds.get(row);
          return file.getName();
        }
        return null;
      }

      public String getColumnName(int col) {
        return names[col];
      }

      public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
      }

      public boolean isCellEditable(int row, int col) {
        return false;
      }

      public void setValueAt(Object aValue, int row, int col) {
      }
    };

    table = new JTable(dataModel);
    table.getSelectionModel().addListSelectionListener(selectionListener);
    table.setRowHeight(30);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    TableColumn col = table.getColumn("#");
    col.setMaxWidth(20);
    table.sizeColumnsToFit(0);

    JScrollPane scrollPane = new JScrollPane(table);
    EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
    scrollPane.setBorder(new CompoundBorder(eb, new EtchedBorder()));
    add(scrollPane);

    JPanel p1 = new JPanel();
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
    JMenu menu = menuBar.add(new JMenu("Add"));
    JMenuItem menuItem = menu.add(new JMenuItem("Local files"));
    menuItem.addActionListener(this);
    p1.add(menuBar);
    menuItem = menu.add(new JMenuItem("Record now"));
    menuItem.addActionListener(this);
    p1.add(menuBar);

    menuBar = new JMenuBar();
    menuBar.setBorder(new BevelBorder(BevelBorder.RAISED));
    menu = menuBar.add(new JMenu("Remove"));
    JMenuItem item = menu.add(new JMenuItem("Selected"));
    item.addActionListener(this);
    item = menu.add(new JMenuItem("All"));
    item.addActionListener(this);
    p1.add(menuBar);

    add(BorderLayout.SOUTH, p1);
  }

  public void actionPerformed(ActionEvent e) {
    Object object = e.getSource();
    if (object instanceof JMenuItem) {
      JMenuItem mi = (JMenuItem) object;
      String text = mi.getText();
      if (text.equals("Local files")) {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File[] files = fileChooser.getSelectedFiles();
          sounds.addAll(checkAndGetAudioFiles(files));
          tableChanged();
        }
      } else if (text.equals("Record now")) {
        //TODO: record
      } else if (text.equals("Selected")) {
        int rows[] = table.getSelectedRows();
        List<File> tmp = new ArrayList<File>();
        for (int row : rows) {
          tmp.add(sounds.get(row));
        }
        sounds.removeAll(tmp);
        tableChanged();
      } else if (text.equals("All")) {
        sounds.clear();
        tableChanged();
      }
    }
  }

  private Set<File> checkAndGetAudioFiles(File[] files) {
    List<String> allowedExtensions = Arrays.asList(soundProperties.getProperty("allowedFormats").split(","));
    Set<File> result = new LinkedHashSet<File>();
    for (File file : files) {
      String ext = file.getName().substring(file.getName().lastIndexOf('.') + 1);
      if (allowedExtensions.contains(ext)) {
        result.add(file);
      }
    }
    return result;
  }

  public Set<File> getSounds() {
    return sounds;
  }

  public JTable getSoundTable() {
    return table;
  }

  public void tableChanged() {
    table.tableChanged(new TableModelEvent(dataModel));
  }
}