package com.coffeecode.util;

import javax.swing.table.DefaultTableModel;

public class TableUtils {
    private TableUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void fixColumnFormatting(DefaultTableModel tableModel, int columnIndex) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Object value = tableModel.getValueAt(row, columnIndex);
            if (value instanceof String stringValue && stringValue.endsWith(".00")) {
                tableModel.setValueAt(stringValue.substring(0, stringValue.length() - 3), row, columnIndex);
            }

        }
    }

    public static void replaceTextInColumn(DefaultTableModel tableModel, int columnIndex, String textToReplace,
            String replacementText) {
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            Object value = tableModel.getValueAt(row, columnIndex);
            if (value instanceof String stringValue) {
                tableModel.setValueAt(stringValue.replace(textToReplace, replacementText), row, columnIndex);
            }
        }
    }
}
