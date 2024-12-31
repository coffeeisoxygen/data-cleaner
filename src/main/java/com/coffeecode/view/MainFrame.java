package com.coffeecode.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.coffeecode.controller.MainFrameController;
import com.coffeecode.service.CSVFileService;
import com.coffeecode.service.ExcelFileService;

public class MainFrame extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<Integer> headerPicker;
    private final transient MainFrameController controller;

    public MainFrame() {
        controller = new MainFrameController();
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
        JButton buttonUploadCSV = createModernButton("Upload CSV");
        JButton buttonUploadExcel = createModernButton("Upload Excel");
        JButton refreshBtn = createModernButton("Refresh");
        JButton deleteDataBtn = createModernButton("clear data");

        // Add action listener for file upload
        buttonUploadCSV.addActionListener((ActionEvent e) -> controller.handleFileUpload(new CSVFileService(),
                new JFileChooser(), tableModel, headerPicker));
        buttonUploadExcel.addActionListener((ActionEvent e) -> controller.handleFileUpload(new ExcelFileService(),
                new JFileChooser(), tableModel, headerPicker));
        refreshBtn.addActionListener((ActionEvent e) -> {
            controller.handleRefresh(tableModel, headerPicker);
            autoResizeTableColumns(table);
        });

        // Add buttons to top panel
        topPanel.add(refreshBtn);
        topPanel.add(deleteDataBtn);
        topPanel.add(buttonUploadCSV);
        topPanel.add(buttonUploadExcel);

        // Header picker
        headerPicker = new JComboBox<>();
        headerPicker.addActionListener((ActionEvent e) -> {
            controller.handleHeaderSelection(tableModel, headerPicker);
            autoResizeTableColumns(table);
        });
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
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Add context menu to table
        JPopupMenu contextMenu = createContextMenu();
        table.setComponentPopupMenu(contextMenu);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    table.setRowSelectionInterval(row, row);
                    table.setColumnSelectionInterval(column, column);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    int column = table.columnAtPoint(e.getPoint());
                    table.setRowSelectionInterval(row, row);
                    table.setColumnSelectionInterval(column, column);
                }
            }
        });

        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        return bottomPanel;
    }

    private JPopupMenu createContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();

        JMenuItem fixFormattingItem = new JMenuItem("Fix Formatting");
        fixFormattingItem.addActionListener(e -> controller.handleFixFormatting(tableModel, table));
        contextMenu.add(fixFormattingItem);

        JMenuItem replaceTextItem = new JMenuItem("Replace Text");
        replaceTextItem.addActionListener(e -> controller.handleReplaceText(tableModel, table));
        contextMenu.add(replaceTextItem);

        return contextMenu;
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

    private void autoResizeTableColumns(JTable table) {
        for (int column = 0; column < table.getColumnCount(); column++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                Component c = table.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }

            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}