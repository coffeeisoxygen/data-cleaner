package com.coffeecode;

import com.coffeecode.view.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;

public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FlatDarkLaf.setup();
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
