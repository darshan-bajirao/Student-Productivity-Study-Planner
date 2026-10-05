package ui;

import javax.swing.*;
import java.awt.*;

public final class DarkDialog {
    private DarkDialog() {}

    public static JPanel formPanel() {
        JPanel p = new JPanel();
        p.setBackground(AppTheme.SURFACE);
        p.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    public static void addField(JPanel p, String label, JComponent field) {
        JLabel l = AppTheme.label(label.toUpperCase(), 10, AppTheme.MUTED);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(5));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(field);
        p.add(Box.createVerticalStrut(13));
    }

    public static void styleCombo(JComboBox<?> box) {
        box.setBackground(AppTheme.SURFACE_2);
        box.setForeground(AppTheme.TEXT);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        box.setBorder(BorderFactory.createLineBorder(AppTheme.LINE));
        box.setPreferredSize(new Dimension(260, 40));
    }

    private static void applyDialogTheme() {
        UIManager.put("OptionPane.background", AppTheme.SURFACE);
        UIManager.put("OptionPane.messageForeground", AppTheme.TEXT);
        UIManager.put("OptionPane.buttonBackground", AppTheme.SURFACE_3);
        UIManager.put("OptionPane.buttonForeground", AppTheme.TEXT);
        UIManager.put("Button.background", AppTheme.SURFACE_3);
        UIManager.put("Button.foreground", AppTheme.TEXT);
        UIManager.put("Button.select", new Color(63, 72, 104));
        UIManager.put("Panel.background", AppTheme.SURFACE);
    }

    public static int confirm(Component parent, JComponent content, String title) {
        applyDialogTheme();
        return JOptionPane.showConfirmDialog(parent, content, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

    public static void message(Component parent, String msg, String title) {
        applyDialogTheme();
        JOptionPane.showMessageDialog(parent, msg, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
