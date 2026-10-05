package ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.plaf.ColorUIResource;

public final class AppTheme {
    public static final Color BG = new Color(7, 10, 16);
    public static final Color SIDEBAR = new Color(10, 14, 22);
    public static final Color SURFACE = new Color(15, 20, 31);
    public static final Color SURFACE_2 = new Color(19, 26, 40);
    public static final Color SURFACE_3 = new Color(25, 33, 51);
    public static final Color LINE = new Color(43, 55, 80);
    public static final Color TEXT = new Color(240, 244, 251);
    public static final Color MUTED = new Color(137, 153, 180);
    public static final Color ACCENT = new Color(103, 92, 255);
    public static final Color ACCENT_2 = new Color(42, 220, 194);
    public static final Color WARN = new Color(255, 193, 71);
    public static final Color DANGER = new Color(255, 98, 123);
    public static final Color SUCCESS = new Color(57, 219, 151);

    private AppTheme() {}

    public static void install() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("Panel.background", BG);
        UIManager.put("Viewport.background", BG);
        UIManager.put("ScrollPane.background", BG);
        UIManager.put("Label.foreground", TEXT);

        UIManager.put("OptionPane.background", new ColorUIResource(SURFACE));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXT));
        UIManager.put("OptionPane.buttonBackground", new ColorUIResource(SURFACE_3));
        UIManager.put("OptionPane.buttonForeground", new ColorUIResource(TEXT));

        UIManager.put("ComboBox.background", new ColorUIResource(SURFACE));
        UIManager.put("ComboBox.foreground", new ColorUIResource(TEXT));
        UIManager.put("ComboBox.selectionBackground", new ColorUIResource(SURFACE_3));
        UIManager.put("ComboBox.selectionForeground", new ColorUIResource(TEXT));

        UIManager.put("Button.background", new ColorUIResource(SURFACE_3));
        UIManager.put("Button.foreground", new ColorUIResource(TEXT));
        UIManager.put("OptionPane.buttonBackground", new ColorUIResource(SURFACE_3));
        UIManager.put("OptionPane.buttonForeground", new ColorUIResource(TEXT));
        UIManager.put("Button.select", new ColorUIResource(new Color(63, 72, 104)));

        UIManager.put("TextField.background", new ColorUIResource(SURFACE));
        UIManager.put("TextField.foreground", new ColorUIResource(TEXT));
        UIManager.put("PasswordField.background", new ColorUIResource(SURFACE));
        UIManager.put("PasswordField.foreground", new ColorUIResource(TEXT));

        UIManager.put("TextArea.background", new ColorUIResource(SURFACE));
        UIManager.put("TextArea.foreground", new ColorUIResource(TEXT));

        UIManager.put("Table.background", new ColorUIResource(SURFACE));
        UIManager.put("Table.foreground", new ColorUIResource(TEXT));
        UIManager.put("Table.selectionBackground", new ColorUIResource(new Color(49, 57, 98)));
        UIManager.put("Table.selectionForeground", new ColorUIResource(TEXT));
        UIManager.put("TableHeader.background", new ColorUIResource(SURFACE_3));
        UIManager.put("TableHeader.foreground", new ColorUIResource(TEXT));
    }

    public static JLabel label(String text, int size, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, size));
        l.setForeground(color);
        return l;
    }

    public static JLabel title(String text) {
        JLabel l = label(text, 27, TEXT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 27));
        return l;
    }

    public static JLabel section(String text) {
        JLabel l = label(text, 13, TEXT);
        l.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return l;
    }
}
