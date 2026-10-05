package ui;

import model.StudyPlan;
import model.StudySession;
import model.Subject;
import model.Workspace;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;
import java.util.concurrent.*;

public class PlannerPanel extends JPanel {
    private final MainFrame frame;
    private final Workspace workspace;
    private final StyledTable table = new StyledTable();
    private final DefaultTableModel model =
            new DefaultTableModel(new Object[]{"DAY","TIME","SUBJECT","TOPIC"},0);
    private final JComboBox<String> subject = new JComboBox<>();
    private final JLabel timerLabel = AppTheme.label("25:00", 48, AppTheme.TEXT);
    private ScheduledExecutorService timer;
    private int seconds=1500;
    private final WeekCards weekCards;

    public PlannerPanel(MainFrame frame, Workspace workspace){
        this.frame=frame;this.workspace=workspace;
        setLayout(new BorderLayout(16,16));
        setBorder(BorderFactory.createEmptyBorder(22,26,26,26));
        setBackground(AppTheme.BG);

        for(Subject s:workspace.subjects)subject.addItem(s.getName());
        DarkDialog.styleCombo(subject);

        JPanel header=new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel copy=new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy,BoxLayout.Y_AXIS));
        copy.add(AppTheme.title("Study Planner"));
        copy.add(Box.createVerticalStrut(5));
        copy.add(AppTheme.label("Plan your week and protect focused study time.",13,AppTheme.MUTED));
        header.add(copy,BorderLayout.WEST);
        add(header,BorderLayout.NORTH);

        for(StudyPlan p:workspace.plans)
            model.addRow(new Object[]{p.getDay(),p.getTime(),p.getSubject(),p.getTopic()});
        table.setModel(model);

        weekCards=new WeekCards(workspace);
        weekCards.setPreferredSize(new Dimension(0,190));

        JPanel schedule = new RoundedPanel(18,AppTheme.SURFACE,AppTheme.LINE);
        schedule.setLayout(new BorderLayout(10,10));
        schedule.setBorder(BorderFactory.createEmptyBorder(14,14,14,14));
        schedule.add(AppTheme.section("WEEKLY PLAN"),BorderLayout.NORTH);
        schedule.add(weekCards,BorderLayout.CENTER);

        JPanel scheduleBottom=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));
        scheduleBottom.setOpaque(false);
        ModernButton add=new ModernButton("+  ADD STUDY SLOT");
        ModernButton del=new ModernButton("DELETE SLOT",AppTheme.SURFACE_3);
        add.addActionListener(e->addSlot());
        del.addActionListener(e->{int r=table.getSelectedRow();if(r>=0){workspace.plans.remove(r);frame.save();reload();}});
        scheduleBottom.add(add);scheduleBottom.add(del);
        schedule.add(scheduleBottom,BorderLayout.SOUTH);

        JPanel pom=new RoundedPanel(18,AppTheme.SURFACE,AppTheme.LINE);
        pom.setLayout(new BorderLayout(8,8));
        pom.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        pom.add(AppTheme.section("FOCUS TIMER"),BorderLayout.NORTH);
        JPanel timerCenter=new JPanel(new GridBagLayout());timerCenter.setOpaque(false);
        timerCenter.add(timerLabel);pom.add(timerCenter,BorderLayout.CENTER);

        JPanel timerButtons=new JPanel(new FlowLayout(FlowLayout.CENTER,7,0));timerButtons.setOpaque(false);
        ModernButton start=new ModernButton("START");
        ModernButton pause=new ModernButton("PAUSE",AppTheme.SURFACE_3);
        ModernButton reset=new ModernButton("RESET",AppTheme.SURFACE_3);
        ModernButton log=new ModernButton("LOG 25 MIN",AppTheme.ACCENT_2);
        start.addActionListener(e->startTimer());pause.addActionListener(e->pauseTimer());reset.addActionListener(e->resetTimer());log.addActionListener(e->logSession());
        for(JButton b:new JButton[]{start,pause,reset,log})timerButtons.add(b);
        pom.add(timerButtons,BorderLayout.SOUTH);

        JPanel upper=new JPanel(new GridLayout(1,2,14,14));upper.setOpaque(false);
        upper.add(schedule);upper.add(pom);

        model.setRowCount(0);
        for(StudyPlan p:workspace.plans)
            model.addRow(new Object[]{p.getDay(),p.getTime(),p.getSubject(),p.getTopic()});
        table.setModel(model);

        JScrollPane tableScroll=table.scroll();
        JPanel lower=new JPanel(new BorderLayout());
        lower.setOpaque(false);
        lower.add(tableScroll,BorderLayout.CENTER);

        JPanel all=new JPanel(new BorderLayout(14,14));
        all.setOpaque(false);
        all.add(upper,BorderLayout.NORTH);
        all.add(lower,BorderLayout.CENTER);

        add(all,BorderLayout.CENTER);
    }

    private void reload(){
        model.setRowCount(0);
        for(StudyPlan p:workspace.plans)
            model.addRow(new Object[]{p.getDay(),p.getTime(),p.getSubject(),p.getTopic()});
        weekCards.rebuild();
    }

    private void addSlot(){
        JPanel form=DarkDialog.formPanel();
        JComboBox<String> day=new JComboBox<>(new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"});
        ModernTextField time=new ModernTextField();time.setText("18:00");
        ModernTextField topic=new ModernTextField();
        DarkDialog.styleCombo(day);
        DarkDialog.addField(form,"Day",day);
        DarkDialog.addField(form,"Time (24-hour)",time);
        DarkDialog.addField(form,"Subject",subject);
        DarkDialog.addField(form,"Topic",topic);

        if(DarkDialog.confirm(this,form,"Add Study Slot")==JOptionPane.OK_OPTION){
            String s=(String)subject.getSelectedItem();
            if(s==null) s="General Study";
            workspace.plans.add(new StudyPlan((String)day.getSelectedItem(),time.getText().trim(),s,topic.getText().trim()));
            frame.save();
            reload();
        }
    }

    private void startTimer(){
        if(timer!=null&&!timer.isShutdown())return;
        timer=Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(()->{
            if(seconds>0){
                seconds--;
                SwingUtilities.invokeLater(this::updateTimer);
            }else{
                pauseTimer();
                Toolkit.getDefaultToolkit().beep();
                SwingUtilities.invokeLater(()->DarkDialog.message(this,
                        "Focus session complete. Take a short break.",
                        "Pomodoro"));
            }
        },0,1,TimeUnit.SECONDS);
    }

    private void pauseTimer(){
        if(timer!=null){timer.shutdownNow();timer=null;}
    }

    private void resetTimer(){pauseTimer();seconds=1500;updateTimer();}
    private void updateTimer(){timerLabel.setText(String.format("%02d:%02d",seconds/60,seconds%60));}

    private void logSession(){
        String s=(String)subject.getSelectedItem();if(s==null)s="General Study";
        workspace.sessions.add(new StudySession(s,25));frame.save();
        DarkDialog.message(this,"25-minute study session logged for "+s+".","Study Session");
    }

    static class WeekCards extends JPanel {
        private final Workspace workspace;
        WeekCards(Workspace workspace){this.workspace=workspace;setOpaque(false);setLayout(new GridLayout(1,7,8,0));rebuild();}
        void rebuild(){
            removeAll();
            String[] days={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
            for(String day:days){
                RoundedPanel p=new RoundedPanel(14,AppTheme.SURFACE_2,AppTheme.LINE);
                p.setLayout(new BorderLayout());
                JLabel d=AppTheme.label(day.substring(0,3).toUpperCase(),10,AppTheme.ACCENT_2);
                d.setHorizontalAlignment(SwingConstants.CENTER);
                d.setBorder(BorderFactory.createEmptyBorder(8,2,6,2));
                p.add(d,BorderLayout.NORTH);

                JPanel list=new JPanel();
                list.setOpaque(false);list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));
                list.setBorder(BorderFactory.createEmptyBorder(3,6,4,6));
                boolean found=false;

                for(StudyPlan s:workspace.plans){
                    if(s.getDay().equalsIgnoreCase(day)){
                        JLabel slot=AppTheme.label(
                                s.getTime()+"  "+(s.getSubject().length()>9?s.getSubject().substring(0,9)+"…":s.getSubject()),
                                9,AppTheme.TEXT);
                        list.add(slot);list.add(Box.createVerticalStrut(4));found=true;
                    }
                }
                if(!found)list.add(AppTheme.label("—",10,AppTheme.MUTED));
                p.add(list,BorderLayout.CENTER);
                add(p);
            }
            revalidate();repaint();
        }
    }
}
