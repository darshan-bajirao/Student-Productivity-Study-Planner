package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

public class WindowBar extends JPanel {
    private Point dragPoint;

    public WindowBar(JFrame frame, String title, boolean showMaximize) {
        setOpaque(false);
        setPreferredSize(new Dimension(0, 48));
        setLayout(new BorderLayout());

        JLabel label = AppTheme.label(title, 11, AppTheme.MUTED);
        label.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        add(label, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 8));
        controls.setOpaque(false);

        WindowControl min = new WindowControl(WindowControl.MINIMIZE);
        min.addActionListener(e -> frame.setState(Frame.ICONIFIED));
        controls.add(min);

        if (showMaximize) {
            WindowControl max = new WindowControl(WindowControl.MAXIMIZE);
            max.addActionListener(e -> {
                if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) {
                    frame.setExtendedState(Frame.NORMAL);
                } else {
                    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            });
            controls.add(max);
        }

        WindowControl close = new WindowControl(WindowControl.CLOSE);
        close.addActionListener(e -> frame.dispatchEvent(
                new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)));
        controls.add(close);
        add(controls, BorderLayout.EAST);

        MouseAdapter dragger = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                dragPoint = e.getPoint();
            }
            @Override public void mouseDragged(MouseEvent e) {
                if (frame.getExtendedState() == Frame.MAXIMIZED_BOTH) return;
                Point p = e.getLocationOnScreen();
                frame.setLocation(p.x - dragPoint.x, p.y - dragPoint.y);
            }
        };
        addMouseListener(dragger);
        addMouseMotionListener(dragger);
    }

    static class WindowControl extends JButton {
        static final int MINIMIZE = 1, MAXIMIZE = 2, CLOSE = 3;
        private final int type;

        WindowControl(int type) {
            this.type = type;
            setPreferredSize(new Dimension(38, 30));
            setMinimumSize(new Dimension(38, 30));
            setMaximumSize(new Dimension(38, 30));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override protected void paintComponent(Graphics g) {
            Graphics2D x = (Graphics2D) g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isRollover()) {
                if (type == CLOSE) x.setColor(new Color(210, 65, 85, 120));
                else x.setColor(new Color(255, 255, 255, 12));
                x.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 9, 9);
            }

            x.setColor(type == CLOSE && getModel().isRollover()
                    ? Color.WHITE : AppTheme.MUTED);
            x.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int cx = getWidth() / 2, cy = getHeight() / 2;
            if (type == MINIMIZE) {
                x.drawLine(cx - 6, cy + 4, cx + 6, cy + 4);
            } else if (type == MAXIMIZE) {
                x.drawRect(cx - 6, cy - 6, 12, 12);
            } else {
                x.draw(new Line2D.Float(cx - 6, cy - 6, cx + 6, cy + 6));
                x.draw(new Line2D.Float(cx + 6, cy - 6, cx - 6, cy + 6));
            }
            x.dispose();
        }
    }
}
