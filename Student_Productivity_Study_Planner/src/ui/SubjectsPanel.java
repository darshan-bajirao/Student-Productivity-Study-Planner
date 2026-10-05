package ui;

import model.Subject;
import model.Workspace;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SubjectsPanel extends JPanel {
    private final MainFrame frame;
    private final Workspace workspace;
    private final StyledTable table = new StyledTable();
    private final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"SUBJECT", "CODE", "TEACHER", "TARGET HOURS"}, 0);

    public SubjectsPanel(MainFrame frame, Workspace workspace) {
        this.frame = frame;
        this.workspace = workspace;

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
        copy.add(AppTheme.title("Subject Management"));
        copy.add(Box.createVerticalStrut(5));
        copy.add(AppTheme.label("Build your semester workspace and set study targets.", 13, AppTheme.MUTED));
        p.add(copy, BorderLayout.WEST);
        p.add(AppTheme.label(workspace.subjects.size() + " SUBJECTS", 11, AppTheme.ACCENT_2), BorderLayout.EAST);
        return p;
    }

    private JPanel actions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        ModernButton add = new ModernButton("+  ADD SUBJECT");
        ModernButton edit = new ModernButton("EDIT", AppTheme.SURFACE_3);
        ModernButton del = new ModernButton("DELETE", AppTheme.SURFACE_3);

        add.addActionListener(e -> showDialog(-1));
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) showDialog(row);
            else DarkDialog.message(this, "Select a subject first.", "Subject Management");
        });
        del.addActionListener(e -> deleteSelected());

        p.add(add);
        p.add(edit);
        p.add(del);
        return p;
    }

    private void reload() {
        model.setRowCount(0);
        for (Subject s : workspace.subjects) {
            model.addRow(new Object[]{s.getName(), s.getCode(), s.getTeacher(), s.getTargetHours()});
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            DarkDialog.message(this, "Select a subject first.", "Subject Management");
            return;
        }
        UIManager.put("OptionPane.background", AppTheme.SURFACE);
        UIManager.put("Panel.background", AppTheme.SURFACE);
        UIManager.put("OptionPane.messageForeground", AppTheme.TEXT);
        UIManager.put("OptionPane.buttonBackground", AppTheme.SURFACE_3);
        UIManager.put("OptionPane.buttonForeground", AppTheme.TEXT);
        UIManager.put("Button.background", AppTheme.SURFACE_3);
        UIManager.put("Button.foreground", AppTheme.TEXT);
        if (JOptionPane.showConfirmDialog(this,
                "Delete the selected subject?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            workspace.subjects.remove(row);
            frame.save();
            reload();
            frame.showPage("Subjects");
        }
    }

    private void showDialog(int row) {
        Subject old = row >= 0 ? workspace.subjects.get(row) : null;

        JPanel form = DarkDialog.formPanel();
        ModernTextField name = new ModernTextField();
        ModernTextField code = new ModernTextField();
        ModernTextField teacher = new ModernTextField();
        ModernTextField hours = new ModernTextField();

        name.setText(old != null ? old.getName() : "");
        code.setText(old != null ? old.getCode() : "");
        teacher.setText(old != null ? old.getTeacher() : "");
        hours.setText(old != null ? String.valueOf(old.getTargetHours()) : "20");

        Dimension size = new Dimension(360, 40);
        name.setPreferredSize(size); code.setPreferredSize(size);
        teacher.setPreferredSize(size); hours.setPreferredSize(size);

        DarkDialog.addField(form, "Subject name", name);
        DarkDialog.addField(form, "Subject code", code);
        DarkDialog.addField(form, "Teacher", teacher);
        DarkDialog.addField(form, "Target study hours", hours);

        if (DarkDialog.confirm(this, form, row < 0 ? "Add Subject" : "Edit Subject")
                == JOptionPane.OK_OPTION) {
            try {
                String n = name.getText().trim();
                String c = code.getText().trim();
                String t = teacher.getText().trim();
                int h = Integer.parseInt(hours.getText().trim());
                if (n.isEmpty() || c.isEmpty()) throw new IllegalArgumentException();

                if (row < 0) {
                    workspace.subjects.add(new Subject(n, c, t, h));
                } else {
                    old.setName(n); old.setCode(c); old.setTeacher(t); old.setTargetHours(h);
                }
                frame.save();
                reload();
                frame.showPage("Subjects");
            } catch (Exception ex) {
                DarkDialog.message(this, "Please enter valid subject details.", "Input Error");
            }
        }
    }
}
