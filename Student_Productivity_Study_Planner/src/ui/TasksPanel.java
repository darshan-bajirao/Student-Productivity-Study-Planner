package ui;

import model.Task;
import model.Workspace;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class TasksPanel extends JPanel {
    private final MainFrame frame;
    private final Workspace workspace;
    private final StyledTable table = new StyledTable();
    private final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"TASK", "SUBJECT", "PRIORITY", "STATUS", "DUE DATE"}, 0);

    public TasksPanel(MainFrame frame, Workspace workspace) {
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
        copy.add(AppTheme.title("Task Manager"));
        copy.add(Box.createVerticalStrut(5));
        copy.add(AppTheme.label("Assignments, exams and goals — all in one place.", 13, AppTheme.MUTED));
        p.add(copy, BorderLayout.WEST);
        p.add(AppTheme.label(workspace.tasks.size() + " TASKS", 11, AppTheme.ACCENT_2), BorderLayout.EAST);
        return p;
    }

    private JPanel actions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);

        ModernButton add = new ModernButton("+  ADD TASK");
        ModernButton edit = new ModernButton("EDIT", AppTheme.SURFACE_3);
        ModernButton progress = new ModernButton("IN PROGRESS", AppTheme.SURFACE_3);
        ModernButton complete = new ModernButton("COMPLETE", AppTheme.SUCCESS);
        ModernButton del = new ModernButton("DELETE", AppTheme.SURFACE_3);

        add.addActionListener(e -> showDialog(-1));
        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) showDialog(r);
            else DarkDialog.message(this, "Select a task first.", "Task Manager");
        });
        progress.addActionListener(e -> changeStatus(Task.Status.IN_PROGRESS));
        complete.addActionListener(e -> changeStatus(Task.Status.COMPLETED));
        del.addActionListener(e -> deleteSelected());

        for (JButton b : new JButton[]{add, edit, progress, complete, del}) p.add(b);
        return p;
    }

    private void reload() {
        model.setRowCount(0);
        for (Task t : workspace.tasks) {
            model.addRow(new Object[]{
                    t.getTitle(), t.getSubject(),
                    t.getPriority().toString().replace('_',' '),
                    t.getStatus().toString().replace('_',' '),
                    t.getDueDate()
            });
        }
    }

    private void changeStatus(Task.Status status) {
        int r = table.getSelectedRow();
        if (r < 0) {
            DarkDialog.message(this, "Select a task first.", "Task Manager");
            return;
        }
        workspace.tasks.get(r).setStatus(status);
        frame.save();
        reload();
    }

    private void deleteSelected() {
        int r = table.getSelectedRow();
        if (r < 0) {
            DarkDialog.message(this, "Select a task first.", "Task Manager");
            return;
        }
        UIManager.put("OptionPane.background", AppTheme.SURFACE);
        UIManager.put("Panel.background", AppTheme.SURFACE);
        UIManager.put("OptionPane.messageForeground", AppTheme.TEXT);
        UIManager.put("OptionPane.buttonBackground", AppTheme.SURFACE_3);
        UIManager.put("OptionPane.buttonForeground", AppTheme.TEXT);
        UIManager.put("Button.background", AppTheme.SURFACE_3);
        UIManager.put("Button.foreground", AppTheme.TEXT);
        if (JOptionPane.showConfirmDialog(this, "Delete the selected task?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            workspace.tasks.remove(r);
            frame.save();
            reload();
        }
    }

    private void showDialog(int row) {
        Task old = row >= 0 ? workspace.tasks.get(row) : null;

        JPanel form = DarkDialog.formPanel();
        ModernTextField title = new ModernTextField();
        ModernTextField subject = new ModernTextField();
        ModernTextField date = new ModernTextField();
        ModernTextField description = new ModernTextField();
        JComboBox<Task.Priority> priority = new JComboBox<>(Task.Priority.values());

        title.setText(old != null ? old.getTitle() : "");
        subject.setText(old != null ? old.getSubject() : "");
        date.setText(old != null ? old.getDueDate().toString() : LocalDate.now().plusDays(1).toString());
        description.setText(old != null ? old.getDescription() : "");
        if (old != null) priority.setSelectedItem(old.getPriority());
        DarkDialog.styleCombo(priority);

        Dimension size = new Dimension(370, 40);
        title.setPreferredSize(size); subject.setPreferredSize(size);
        date.setPreferredSize(size); description.setPreferredSize(size);

        DarkDialog.addField(form, "Task title", title);
        DarkDialog.addField(form, "Subject", subject);
        DarkDialog.addField(form, "Priority", priority);
        DarkDialog.addField(form, "Due date (YYYY-MM-DD)", date);
        DarkDialog.addField(form, "Description", description);

        if (DarkDialog.confirm(this, form, row < 0 ? "Add Task" : "Edit Task") == JOptionPane.OK_OPTION) {
            try {
                String n = title.getText().trim();
                LocalDate due = LocalDate.parse(date.getText().trim());
                if (n.isEmpty()) throw new IllegalArgumentException();

                if (row < 0) {
                    workspace.tasks.add(new Task(n, subject.getText().trim(),
                            (Task.Priority) priority.getSelectedItem(), due,
                            description.getText().trim()));
                } else {
                    old.setTitle(n);
                    old.setSubject(subject.getText().trim());
                    old.setPriority((Task.Priority) priority.getSelectedItem());
                    old.setDueDate(due);
                    old.setDescription(description.getText().trim());
                }
                frame.save();
                reload();
            } catch (Exception ex) {
                DarkDialog.message(this,
                        "Please check the title and use YYYY-MM-DD for the due date.",
                        "Input Error");
            }
        }
    }
}
