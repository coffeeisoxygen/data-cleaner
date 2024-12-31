package com.coffeecode.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.coffeecode.model.FileModel;
import com.coffeecode.service.FileReaderService;
import com.coffeecode.util.TableUtils;
import com.opencsv.exceptions.CsvException;

public class MainFrameController {

    private FileReaderService fileService;
    private FileModel fileModel;

    public void handleFileUpload(FileReaderService fileService, JFileChooser fileChooser, DefaultTableModel tableModel,
            JComboBox<Integer> headerPicker) {
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                this.fileService = fileService;
                fileModel = fileService.loadFile(file.getAbsolutePath(), "UTF-8", ",", false);
                updateHeaderPicker(fileModel, headerPicker);
                updateTableModel(fileModel, 0, tableModel); // Default to first row as header
            } catch (IOException | CsvException ex) {
                JOptionPane.showMessageDialog(null, "Gagal membaca file: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void handleRefresh(DefaultTableModel tableModel, JComboBox<Integer> headerPicker) {
        if (fileModel != null) {
            int selectedHeaderRow = (int) headerPicker.getSelectedItem();
            updateTableModel(fileModel, selectedHeaderRow, tableModel);
        }
    }

    public void handleHeaderSelection(DefaultTableModel tableModel, JComboBox<Integer> headerPicker) {
        if (fileModel != null) {
            int selectedHeaderRow = (int) headerPicker.getSelectedItem();
            updateTableModel(fileModel, selectedHeaderRow, tableModel);
        }
    }

    public void handleFixFormatting(DefaultTableModel tableModel, JTable table) {
        int columnIndex = table.getSelectedColumn();
        if (columnIndex != -1) {
            TableUtils.fixColumnFormatting(tableModel, columnIndex);
        }
    }

    public void handleReplaceText(DefaultTableModel tableModel, JTable table) {
        int columnIndex = table.getSelectedColumn();
        if (columnIndex != -1) {
            String textToReplace = JOptionPane.showInputDialog(null, "Enter text to replace:");
            String replacementText = JOptionPane.showInputDialog(null, "Enter replacement text:");
            if (textToReplace != null && replacementText != null) {
                TableUtils.replaceTextInColumn(tableModel, columnIndex, textToReplace, replacementText);
            }
        }
    }

    private void updateHeaderPicker(FileModel fileModel, JComboBox<Integer> headerPicker) {
        headerPicker.removeAllItems();
        for (int i = 0; i < fileModel.getData().size(); i++) {
            headerPicker.addItem(i);
        }
    }

    private void updateTableModel(FileModel fileModel, int headerRowIndex, DefaultTableModel tableModel) {
        if (fileModel.getData().isEmpty()) {
            return;
        }

        // Extract column names from the selected header row
        String[] columnNames = fileModel.getData().get(headerRowIndex);

        // Add "No" column to the column names
        String[] extendedColumnNames = new String[columnNames.length + 1];
        extendedColumnNames[0] = "No";
        System.arraycopy(columnNames, 0, extendedColumnNames, 1, columnNames.length);

        // Extract row data starting from the row after the header
        List<String[]> data = new ArrayList<>(
                fileModel.getData().subList(headerRowIndex + 1, fileModel.getData().size()));

        // Add row numbers to the data
        List<Object[]> extendedData = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            Object[] row = new Object[data.get(i).length + 1];
            row[0] = i + 1; // Row number
            System.arraycopy(data.get(i), 0, row, 1, data.get(i).length);
            extendedData.add(row);
        }

        // Update table model
        tableModel.setDataVector(extendedData.toArray(new Object[0][]), extendedColumnNames);
    }
}
