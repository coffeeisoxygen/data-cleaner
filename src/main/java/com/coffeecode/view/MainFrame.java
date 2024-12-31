package com.coffeecode.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.coffeecode.model.FileModel;
import com.coffeecode.service.FileService;
import com.opencsv.exceptions.CsvException;

public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private FileService fileService;
    private FileModel fileModel;
    private JComboBox<Integer> headerPicker;

    public MainFrame() {
        fileService = new FileService();
        setTitle("Aplikasi Java Swing Modern");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponent();
    }

    private void initComponent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel);

        // Top panel for buttons
        JPanel topPanel = createTopPanel();

        // Bottom panel for table view
        JPanel bottomPanel = createBottomPanel();

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // Align buttons to the right
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create buttons
        JButton buttonUpload = createModernButton("Upload File");
        JButton refreshBtn = createModernButton("Refresh");
        JButton deleteRowsBtn = createModernButton("Delete Rows");
        JButton deleteCoulmnsBtn = createModernButton("Delete Columns");

        // Add action listener for file upload
        buttonUpload.addActionListener((ActionEvent e) -> handleFileUpload());
        refreshBtn.addActionListener((ActionEvent e) -> handleRefresh());

        // Add buttons to top panel
        topPanel.add(refreshBtn);
        topPanel.add(deleteRowsBtn);
        topPanel.add(deleteCoulmnsBtn);
        topPanel.add(buttonUpload); // Upload button is added last to be on the right

        // Header picker
        headerPicker = new JComboBox<>();
        headerPicker.addActionListener((ActionEvent e) -> handleHeaderSelection());
        topPanel.add(new JLabel("Select Header Row:"));
        topPanel.add(headerPicker);

        return topPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Table initialization
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Enable horizontal scrolling
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        return bottomPanel;
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding inside button
        return button;
    }

    private void handleFileUpload() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                fileModel = fileService.loadCSV(file.getAbsolutePath(), "UTF-8", ",", false);
                updateHeaderPicker(fileModel);
                updateTableModel(fileModel, 0); // Default to first row as header
            } catch (IOException | CsvException ex) {
                JOptionPane.showMessageDialog(this, "Gagal membaca file: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleRefresh() {
        if (fileModel != null) {
            int selectedHeaderRow = (int) headerPicker.getSelectedItem();
            updateTableModel(fileModel, selectedHeaderRow);
        }
    }

    private void handleHeaderSelection() {
        if (fileModel != null) {
            int selectedHeaderRow = (int) headerPicker.getSelectedItem();
            updateTableModel(fileModel, selectedHeaderRow);
        }
    }

    private void updateHeaderPicker(FileModel fileModel) {
        headerPicker.removeAllItems();
        for (int i = 0; i < fileModel.getData().size(); i++) {
            headerPicker.addItem(i);
        }
    }

    private void updateTableModel(FileModel fileModel, int headerRowIndex) {
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
        List<String[]> data = new ArrayList<>(fileModel.getData().subList(headerRowIndex + 1, fileModel.getData().size()));

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}