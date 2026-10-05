package ui;

import model.Note;
import model.Workspace;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class NotesPanel extends JPanel {
    private final MainFrame frame;
    private final Workspace workspace;
    private final StyledTable table = new StyledTable();
    private final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"TITLE", "SUBJECT", "UPDATED"}, 0);

    public NotesPanel(MainFrame frame, Workspace workspace) {
        this.frame = frame; this.workspace = workspace;

        setLayout(new BorderLayout(16, 16));
        setBorder(BorderFactory.createEmptyBorder(24, 26, 26, 26));
        setBackground(AppTheme.BG);

        add(header(), BorderLayout.NORTH);
        table.setModel(model);
        add(table.scroll(), BorderLayout.CENTER);
        add(actions(), BorderLayout.SOUTH);
        reload();
    }

    private JPanel header() {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(AppTheme.title("Notes"));
        copy.add(Box.createVerticalStrut(5));
        copy.add(AppTheme.label("Keep revision notes organized by subject.", 13, AppTheme.MUTED));
        p.add(copy, BorderLayout.WEST);
        p.add(AppTheme.label(workspace.notes.size() + " NOTES", 11, AppTheme.ACCENT_2), BorderLayout.EAST);
        return p;
    }

    private JPanel actions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        ModernButton add = new ModernButton("+  NEW NOTE");
        ModernButton edit = new ModernButton("EDIT", AppTheme.SURFACE_3);
        ModernButton view = new ModernButton("VIEW", AppTheme.SURFACE_3);
        ModernButton del = new ModernButton("DELETE", AppTheme.SURFACE_3);

        add.addActionListener(e -> showDialog(-1));
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) showDialog(r);
            else DarkDialog.message(this, "Select a note first.", "Notes");
        });
        view.addActionListener(e -> viewSelected());
        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) {
                workspace.notes.remove(r); frame.save(); reload();
            } else DarkDialog.message(this, "Select a note first.", "Notes");
        });

        for (JButton b : new JButton[]{add, edit, view, del}) p.add(b);
        return p;
    }

    private void reload() {
        model.setRowCount(0);
        for (Note n : workspace.notes)
            model.addRow(new Object[]{n.getTitle(), n.getSubject(), n.getUpdatedAt().toLocalDate()});
    }

    private void viewSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            DarkDialog.message(this, "Select a note first.", "Notes");
            return;
        }
        Note n = workspace.notes.get(r);
        JTextArea area = new JTextArea(n.getContent(), 14, 56);
        area.setLineWrap(true); area.setWrapStyleWord(true); area.setEditable(false);
        area.setBackground(AppTheme.SURFACE); area.setForeground(AppTheme.TEXT);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.SURFACE);
        p.add(new JScrollPane(area));
        DarkDialog.message(this, n.getTitle() + "\n\n" + n.getContent(), "Note Preview");
    }

    private void showDialog(int row) {
        Note old = row >= 0 ? workspace.notes.get(row) : null;
        JPanel form = DarkDialog.formPanel();

        ModernTextField title = new ModernTextField();
        ModernTextField subject = new ModernTextField();
        JTextArea content = new JTextArea(old != null ? old.getContent() : "", 10, 45);
        content.setLineWrap(true); content.setWrapStyleWord(true);
        content.setBackground(AppTheme.SURFACE_2); content.setForeground(AppTheme.TEXT);
        content.setCaretColor(AppTheme.ACCENT_2);
        content.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        content.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.LINE),
                BorderFactory.createEmptyBorder(10,10,10,10)));

        title.setText(old != null ? old.getTitle() : "");
        subject.setText(old != null ? old.getSubject() : "");
        title.setPreferredSize(new Dimension(370,40));
        subject.setPreferredSize(new Dimension(370,40));

        DarkDialog.addField(form, "Note title", title);
        DarkDialog.addField(form, "Subject", subject);

        JLabel contentLabel = AppTheme.label("CONTENT",10,AppTheme.MUTED);
        form.add(contentLabel);
        form.add(Box.createVerticalStrut(5));
        JScrollPane scroll = new JScrollPane(content);
        scroll.setPreferredSize(new Dimension(370,190));
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.LINE));
        form.add(scroll);

        if (DarkDialog.confirm(this, form, row < 0 ? "New Note" : "Edit Note") == JOptionPane.OK_OPTION) {
            String t = title.getText().trim();
            if (t.isEmpty()) {
                DarkDialog.message(this, "Enter a note title.", "Input Error");
                return;
            }

            if (row < 0) workspace.notes.add(new Note(t, subject.getText().trim(), content.getText()));
            else {
                old.setTitle(t);
                old.setSubject(subject.getText().trim());
                old.setContent(content.getText());
            }
            frame.save();
            reload();
        }
    }
}
