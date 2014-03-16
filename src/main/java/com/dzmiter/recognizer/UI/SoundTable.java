package com.dzmiter.recognizer.UI;

import com.dzmiter.recognizer.domain.EmptySoundFile;
import com.dzmiter.recognizer.domain.IndexedSet;
import com.dzmiter.recognizer.domain.LinkedHashIndexedSet;
import com.dzmiter.recognizer.event.SaveAction;
import com.dzmiter.recognizer.event.SaveAndOptimizeAction;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Galantis FileNet toolkit
 * Copyright (с ) 2014. All Right Reserved.
 *
 * @author dbezugly
 */
class SoundTable extends JPanel implements ActionListener {

  private TableModel dataModel;
  private IndexedSet<File> sounds;
  private JTable table;
  private JFileChooser fileChooser;

  public SoundTable(ListSelectionListener selectionListener, JFileChooser fileChooser) {
    this.fileChooser = fileChooser;
    sounds = new LinkedHashIndexedSet<File>();
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(250, 400));

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
          sounds.addAll(Arrays.asList(files));
          tableChanged();
        }
      } else if (text.equals("Record now")) {
        File fileToRecord = new File(EmptySoundFile.prepareFilePath(false));
        RecordFrame recordFrame = new RecordFrame(fileToRecord,
            new AddToTableAction(fileToRecord),
            new AddToTableAndOptimizeAction(fileToRecord));
        recordFrame.setVisible(true);
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

  public IndexedSet<File> getSounds() {
    return sounds;
  }

  public JTable getSoundTable() {
    return table;
  }

  public void tableChanged() {
    table.tableChanged(new TableModelEvent(dataModel));
  }

  private class AddToTableAction implements SaveAction {
    private File file;

    public AddToTableAction(File file) {
      this.file = file;
    }

    @Override
    public void doAction() {
      sounds.add(file);
      tableChanged();
    }
  }

  private class AddToTableAndOptimizeAction extends SaveAndOptimizeAction {
    public AddToTableAndOptimizeAction(File file) {
      super(file);
    }

    @Override
    public void doAction() {
      File file = optimizeAndGetFile();
      sounds.add(file);
      tableChanged();
    }
  }
}