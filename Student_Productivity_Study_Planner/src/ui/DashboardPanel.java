package ui;

import model.Task;
import model.Workspace;
import service.ProductivityService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DashboardPanel extends JPanel {
    public DashboardPanel(MainFrame frame, Workspace workspace, String user) {
        setLayout(new BorderLayout(18,18));
        setBorder(BorderFactory.createEmptyBorder(24,28,28,28));
        setBackground(AppTheme.BG);

        JPanel header=new JPanel(new BorderLayout(14,10));header.setOpaque(false);
        JPanel copy=new JPanel();copy.setOpaque(false);copy.setLayout(new BoxLayout(copy,BoxLayout.Y_AXIS));
        JLabel hi=AppTheme.label("Good to see you, "+user+".",29,AppTheme.TEXT);
        hi.setFont(new Font("Segoe UI",Font.BOLD,29));
        copy.add(hi);copy.add(Box.createVerticalStrut(5));
        copy.add(AppTheme.label("Your study workspace  •  "+LocalDate.now(),13,AppTheme.MUTED));
        header.add(copy,BorderLayout.WEST);

        AnimatedProgress progress=new AnimatedProgress(ProductivityService.productivity(workspace));
        progress.setPreferredSize(new Dimension(210,90));
        header.add(progress,BorderLayout.EAST);
        add(header,BorderLayout.NORTH);

        JPanel cards=new JPanel(new GridLayout(1,4,14,14));cards.setOpaque(false);
        cards.add(stat("TASKS COMPLETED",""+ProductivityService.completed(workspace),AppTheme.ACCENT));
        cards.add(stat("PENDING TASKS",""+ProductivityService.pending(workspace),AppTheme.WARN));
        cards.add(stat("STUDY HOURS",String.format("%.1f",ProductivityService.minutes(workspace)/60.0),AppTheme.ACCENT_2));
        cards.add(stat("PRODUCTIVITY",ProductivityService.productivity(workspace)+"%",AppTheme.DANGER));

        JPanel quick=new RoundedPanel(18,AppTheme.SURFACE,AppTheme.LINE);
        quick.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        quick.setLayout(new BorderLayout(10,10));
        quick.add(AppTheme.section("QUICK ACTIONS"),BorderLayout.NORTH);
        JPanel q=new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));q.setOpaque(false);
        ModernButton add=new ModernButton("+  ADD TASK");add.addActionListener(e->frame.showPage("Tasks"));
        ModernButton planner=new ModernButton("OPEN PLANNER  →",AppTheme.SURFACE_3);planner.addActionListener(e->frame.showPage("Study Planner"));
        q.add(add);q.add(planner);
        quick.add(q,BorderLayout.CENTER);
        quick.add(AppTheme.label("Keep tasks small, plan ahead, and log each focused session.",12,AppTheme.MUTED),BorderLayout.SOUTH);

        JPanel focus=new RoundedPanel(18,AppTheme.SURFACE,AppTheme.LINE);
        focus.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        focus.setLayout(new BorderLayout(10,10));
        focus.add(AppTheme.section("TODAY'S FOCUS"),BorderLayout.NORTH);
        JPanel list=new JPanel();list.setOpaque(false);list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));
        int shown=0;
        for(Task t:workspace.tasks){
            if(t.getStatus()!=Task.Status.COMPLETED){
                list.add(task(t));list.add(Box.createVerticalStrut(8));
                if(++shown==5)break;
            }
        }
        if(shown==0)list.add(AppTheme.label("No pending tasks. Your board is clear.",13,AppTheme.ACCENT_2));
        focus.add(list,BorderLayout.CENTER);

        JPanel body=new JPanel(new BorderLayout(0,16));body.setOpaque(false);
        body.add(cards,BorderLayout.NORTH);
        JPanel two=new JPanel(new GridLayout(1,2,14,14));two.setOpaque(false);two.add(quick);two.add(focus);
        body.add(two,BorderLayout.CENTER);
        add(body,BorderLayout.CENTER);
    }

    private JPanel stat(String a,String b,Color accent){
        RoundedPanel p=new RoundedPanel(16,AppTheme.SURFACE,AppTheme.LINE);
        p.setLayout(new BorderLayout(5,5));p.setBorder(BorderFactory.createEmptyBorder(14,15,14,15));
        JPanel bar=new JPanel();bar.setPreferredSize(new Dimension(4,0));bar.setBackground(accent);p.add(bar,BorderLayout.WEST);
        JPanel c=new JPanel();c.setOpaque(false);c.setLayout(new BoxLayout(c,BoxLayout.Y_AXIS));
        c.add(AppTheme.label(a,10,AppTheme.MUTED));
        JLabel v=AppTheme.label(b,28,AppTheme.TEXT);v.setFont(new Font("Segoe UI",Font.BOLD,28));
        c.add(v);p.add(c,BorderLayout.CENTER);return p;
    }

    private JPanel task(Task t){
        JPanel p=new JPanel(new BorderLayout());p.setOpaque(true);p.setBackground(AppTheme.SURFACE_2);
        p.setBorder(BorderFactory.createEmptyBorder(10,12,10,12));
        JLabel a=AppTheme.label(t.getTitle(),13,AppTheme.TEXT);
        JLabel b=AppTheme.label(t.getDueDate().toString(),11,
                t.getDueDate().isBefore(LocalDate.now())?AppTheme.DANGER:AppTheme.MUTED);
        p.add(a,BorderLayout.CENTER);p.add(b,BorderLayout.EAST);return p;
    }

    static class AnimatedProgress extends JPanel {
        int target,value=0;
        AnimatedProgress(int target){this.target=Math.max(0,Math.min(100,target));setOpaque(false);
            new Timer(18,e->{if(value<target)value++;repaint();}).start();}
        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g);Graphics2D x=(Graphics2D)g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            int cx=getWidth()/2-16,cy=39,r=27;x.setStroke(new BasicStroke(7));
            x.setColor(AppTheme.LINE);x.drawArc(cx-r,cy-r,2*r,2*r,-90,360);
            x.setColor(AppTheme.ACCENT_2);x.drawArc(cx-r,cy-r,2*r,2*r,-90,(int)(360*value/100.0));
            x.setColor(AppTheme.TEXT);x.setFont(new Font("Segoe UI",Font.BOLD,16));x.drawString(value+"%",cx-12,cy+6);
            x.setColor(AppTheme.MUTED);x.setFont(new Font("Segoe UI",Font.PLAIN,9));x.drawString("LIVE PRODUCTIVITY",cx-45,cy+42);
            x.dispose();
        }
    }
}
