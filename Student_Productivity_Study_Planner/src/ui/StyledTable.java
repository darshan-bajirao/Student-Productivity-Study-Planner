package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class StyledTable extends JTable {
    public StyledTable() {
        setFillsViewportHeight(true);
        setRowHeight(44);
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 1));
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(AppTheme.SURFACE);
        setForeground(AppTheme.TEXT);
        setSelectionBackground(new Color(49, 57, 98));
        setSelectionForeground(AppTheme.TEXT);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setBackground(AppTheme.SURFACE_3);
        header.setForeground(AppTheme.TEXT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(0, 42));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                l.setBackground(AppTheme.SURFACE_3);
                l.setForeground(AppTheme.MUTED);
                l.setFont(new Font("Segoe UI", Font.BOLD, 11));
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return l;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                l.setOpaque(true);
                l.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                l.setBackground(isSelected ? new Color(49, 57, 98)
                        : (row % 2 == 0 ? AppTheme.SURFACE : new Color(17, 23, 35)));
                l.setForeground(AppTheme.TEXT);
                return l;
            }
        });
    }

    public JScrollPane scroll() {
        JScrollPane s = new JScrollPane(this);
        s.setBorder(BorderFactory.createLineBorder(AppTheme.LINE));
        s.getViewport().setBackground(AppTheme.SURFACE);
        s.setBackground(AppTheme.SURFACE);
        s.getVerticalScrollBar().setUnitIncrement(18);
        s.getHorizontalScrollBar().setUnitIncrement(18);
        return s;
    }
}
