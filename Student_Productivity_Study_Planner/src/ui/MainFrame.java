package ui;

import model.AppData;
import model.Workspace;
import service.DataStore;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private final AppData data;
    private final String username;
    private final Workspace workspace;
    private final JPanel content = new JPanel(new BorderLayout());
    private final JLabel pageTitle = AppTheme.label("DASHBOARD", 12, AppTheme.MUTED);
    private final JLabel clock = AppTheme.label("", 12, AppTheme.MUTED);
    private final JLabel status = AppTheme.label("● ONLINE", 12, AppTheme.ACCENT_2);
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private String currentPage = "Dashboard";

    public MainFrame(AppData data, String username) {
        this.data = data;
        this.username = username;
        this.workspace = data.workspaceFor(username);

        setUndecorated(true);
        setSize(1280, 820);
        setMinimumSize(new Dimension(1050, 700));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        buildUI();
        showPage("Dashboard");

        new Timer(1000, e -> clock.setText(LocalTime.now().withNano(0).toString())).start();

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                save();
                dispose();
            }
        });
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BG);

        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBackground(AppTheme.SIDEBAR);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppTheme.LINE));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(BorderFactory.createEmptyBorder(30, 28, 25, 20));

        JLabel a = AppTheme.label("STUDENT", 26, AppTheme.TEXT);
        a.setFont(new Font("Segoe UI", Font.BOLD, 26));
        JLabel b = AppTheme.label("PLANNER", 26, AppTheme.ACCENT);
        b.setFont(new Font("Segoe UI", Font.BOLD, 26));
        brand.add(a);
        brand.add(b);
        sidebar.add(brand);

        JLabel workspaceLabel = AppTheme.label("WORKSPACE", 11, AppTheme.MUTED);
        workspaceLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        workspaceLabel.setBorder(BorderFactory.createEmptyBorder(0, 28, 10, 0));
        sidebar.add(workspaceLabel);

        for (String n : new String[]{"Dashboard","Subjects","Tasks","Study Planner","Notes","Statistics","Reminders"}) {
            sidebar.add(createNavButton(n));
        }

        sidebar.add(Box.createVerticalGlue());

        JLabel user = AppTheme.label("●  " + username, 13, AppTheme.ACCENT_2);
        user.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 10));
        sidebar.add(user);

        ModernButton logout = new ModernButton("SIGN OUT", AppTheme.SURFACE_3);
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setMaximumSize(new Dimension(205, 42));
        logout.addActionListener(e -> {
            save();
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logout);
        sidebar.add(Box.createVerticalStrut(18));

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(AppTheme.BG);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(11, 15, 24));
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.LINE));

        WindowBar windowBar = new WindowBar(this, "STUDENT PLANNER  •  WORKSPACE", true);
        topBar.add(windowBar, BorderLayout.NORTH);

        JPanel context = new JPanel(new BorderLayout());
        context.setOpaque(false);
        context.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 18));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(pageTitle);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);
        right.add(clock);
        right.add(status);

        context.add(left, BorderLayout.WEST);
        context.add(right, BorderLayout.EAST);
        topBar.add(context, BorderLayout.CENTER);

        main.add(topBar, BorderLayout.NORTH);

        content.setBackground(AppTheme.BG);
        main.add(content, BorderLayout.CENTER);

        root.add(sidebar, BorderLayout.WEST);
        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JButton createNavButton(String text) {
        NavButton b = new NavButton(text);
        navButtons.put(text, b);
        b.addActionListener(e -> showPage(text));
        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setForeground(AppTheme.TEXT); }
            @Override public void mouseExited(MouseEvent e) {
                if (!text.equals(currentPage)) b.setForeground(AppTheme.MUTED);
            }
        });
        return b;
    }

    private void updateNavStyles() {
        for (Map.Entry<String, JButton> e : navButtons.entrySet()) {
            boolean active = e.getKey().equals(currentPage);
            if (e.getValue() instanceof NavButton nb) {
                nb.setActive(active);
            } else {
                e.getValue().setForeground(active ? AppTheme.TEXT : AppTheme.MUTED);
            }
        }
    }

    public void showPage(String page) {
        currentPage = page;
        pageTitle.setText(page.toUpperCase());
        updateNavStyles();

        JPanel panel = switch (page) {
            case "Subjects" -> new SubjectsPanel(this, workspace);
            case "Tasks" -> new TasksPanel(this, workspace);
            case "Study Planner" -> new PlannerPanel(this, workspace);
            case "Notes" -> new NotesPanel(this, workspace);
            case "Statistics" -> new StatisticsPanel(this, workspace);
            case "Reminders" -> new RemindersPanel(this, workspace);
            default -> new DashboardPanel(this, workspace, username);
        };

        content.removeAll();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    public void save() {
        DataStore.save(data);
    }

    public void refreshCurrent() {
        save();
        showPage(currentPage);
    }

    public Workspace workspace() {
        return workspace;
    }
}
