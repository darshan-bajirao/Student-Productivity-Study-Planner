package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NavButton extends JButton {
    private boolean active;
    private boolean hovered;

    public NavButton(String text) {
        super(text.toUpperCase());

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setForeground(AppTheme.MUTED);
        setFont(new Font("Segoe UI", Font.BOLD, 13));
        setHorizontalAlignment(SwingConstants.LEFT);
        setBorder(BorderFactory.createEmptyBorder(0, 28, 0, 12));
        setMaximumSize(new Dimension(218, 50));
        setPreferredSize(new Dimension(218, 50));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    public void setActive(boolean active) {
        this.active = active;
        setForeground(active ? AppTheme.TEXT : AppTheme.MUTED);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D x = (Graphics2D) g.create();
        x.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int inset = 3;
        int w = Math.max(1, getWidth() - 6);
        int h = Math.max(1, getHeight() - 6);

        if (active || hovered) {
            int alpha = active ? 38 : 20;
            x.setColor(new Color(
                    AppTheme.ACCENT.getRed(),
                    AppTheme.ACCENT.getGreen(),
                    AppTheme.ACCENT.getBlue(),
                    alpha));
            x.fillRoundRect(inset, inset, w, h, 13, 13);

            x.setColor(new Color(
                    AppTheme.ACCENT.getRed(),
                    AppTheme.ACCENT.getGreen(),
                    AppTheme.ACCENT.getBlue(),
                    active ? 55 : 28));
            x.drawRoundRect(inset, inset, w, h, 13, 13);
        }

        // Static active/hover indicators. There is deliberately no transition, timer,
        // interpolation, or expanding/sliding animation in the workspace navigation.
        if (active) {
            x.setColor(AppTheme.ACCENT);
            x.fillRoundRect(0, 11, 4, getHeight() - 22, 5, 5);
        } else if (hovered) {
            x.setColor(new Color(
                    AppTheme.ACCENT.getRed(),
                    AppTheme.ACCENT.getGreen(),
                    AppTheme.ACCENT.getBlue(),
                    210));
            x.fillRoundRect(0, 19, 3, getHeight() - 38, 5, 5);
        }

        x.dispose();
        super.paintComponent(g);
    }
}
