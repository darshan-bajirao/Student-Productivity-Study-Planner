package ui;

import model.Task;
import model.Workspace;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class RemindersPanel extends JPanel {
    public RemindersPanel(MainFrame frame, Workspace workspace) {
        setLayout(new BorderLayout(16,16));
        setBorder(BorderFactory.createEmptyBorder(24,26,26,26));
        setBackground(AppTheme.BG);

        JPanel h = new JPanel(new BorderLayout());
        h.setOpaque(false);
        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy,BoxLayout.Y_AXIS));
        copy.add(AppTheme.title("Reminders & Deadlines"));
        copy.add(Box.createVerticalStrut(5));
        copy.add(AppTheme.label("Upcoming and overdue academic tasks.",13,AppTheme.MUTED));
        h.add(copy, BorderLayout.WEST);

        LocalDate today = LocalDate.now();
        long overdue = workspace.tasks.stream().filter(t ->
                t.getStatus()!=Task.Status.COMPLETED && t.getDueDate().isBefore(today)).count();
        JLabel badge = AppTheme.label(overdue>0 ? "⚠ " + overdue + " OVERDUE" : "●  ON TRACK",
                11, overdue>0?AppTheme.DANGER:AppTheme.ACCENT_2);
        h.add(badge, BorderLayout.EAST);
        add(h, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"TASK","SUBJECT","DUE DATE","PRIORITY","STATUS"},0);
        StyledTable table = new StyledTable();
        table.setModel(model);

        for (Task t : workspace.tasks) {
            if (t.getStatus()!=Task.Status.COMPLETED &&
                    !t.getDueDate().isAfter(today.plusDays(7))) {
                model.addRow(new Object[]{
                        t.getTitle(), t.getSubject(), t.getDueDate(),
                        t.getPriority().toString().replace('_',' '),
                        t.getStatus().toString().replace('_',' ')
                });
            }
        }
        add(table.scroll(), BorderLayout.CENTER);

        JPanel foot = new JPanel(new BorderLayout());
        foot.setOpaque(false);
        JLabel tip = AppTheme.label(
                overdue>0 ? "Review overdue work and update deadlines in Task Manager."
                        : "You're on track. Keep checking deadlines before each study session.",
                12, overdue>0?AppTheme.DANGER:AppTheme.MUTED);
        foot.add(tip, BorderLayout.WEST);
        ModernButton tasks = new ModernButton("OPEN TASK MANAGER  →", AppTheme.SURFACE_3);
        tasks.addActionListener(e -> frame.showPage("Tasks"));
        foot.add(tasks, BorderLayout.EAST);
        add(foot, BorderLayout.SOUTH);
    }
}
