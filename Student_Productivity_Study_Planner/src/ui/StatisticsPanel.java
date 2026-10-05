package ui;

import model.Workspace;
import service.ProductivityService;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StatisticsPanel extends JPanel {
    public StatisticsPanel(MainFrame frame, Workspace workspace) {
        setLayout(new BorderLayout(16,16));
        setBorder(BorderFactory.createEmptyBorder(24,26,26,26));
        setBackground(AppTheme.BG);
        add(AppTheme.title("Productivity Analytics"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(14,14));
        body.setOpaque(false);

        JPanel cards = new JPanel(new GridLayout(1,4,12,12));
        cards.setOpaque(false);
        cards.add(card("TOTAL TASKS",String.valueOf(workspace.tasks.size())));
        cards.add(card("COMPLETED",String.valueOf(ProductivityService.completed(workspace))));
        cards.add(card("STUDY HOURS",String.format("%.1f",ProductivityService.minutes(workspace)/60.0)));
        cards.add(card("PRODUCTIVITY",ProductivityService.productivity(workspace)+"%"));

        JPanel chart = new StudyChart(workspace);

        JPanel info = new RoundedPanel(18,AppTheme.SURFACE,AppTheme.LINE);
        info.setLayout(new BorderLayout(10,10));
        info.setBorder(BorderFactory.createEmptyBorder(16,18,16,18));
        info.add(AppTheme.section("HOW PRODUCTIVITY IS CALCULATED"),BorderLayout.NORTH);
        info.add(AppTheme.label("Completed tasks ÷ total tasks × 100. Study hours come from logged focus sessions.",
                12,AppTheme.MUTED),BorderLayout.CENTER);

        JPanel center = new JPanel(new BorderLayout(14,14));
        center.setOpaque(false);
        center.add(cards,BorderLayout.NORTH);
        center.add(chart,BorderLayout.CENTER);
        center.add(info,BorderLayout.SOUTH);
        body.add(center,BorderLayout.CENTER);

        add(body,BorderLayout.CENTER);
    }

    private JPanel card(String a,String b){
        RoundedPanel p=new RoundedPanel(16,AppTheme.SURFACE,AppTheme.LINE);
        p.setLayout(new BorderLayout(4,4));
        p.setBorder(BorderFactory.createEmptyBorder(14,16,14,16));
        p.add(AppTheme.label(a,10,AppTheme.MUTED),BorderLayout.NORTH);
        JLabel v=AppTheme.label(b,27,AppTheme.TEXT);
        v.setFont(new Font("Segoe UI",Font.BOLD,27));
        p.add(v,BorderLayout.CENTER);
        return p;
    }

    static class StudyChart extends JPanel {
        private final Workspace workspace;
        StudyChart(Workspace workspace){
            this.workspace=workspace;
            setOpaque(true);
            setBackground(AppTheme.SURFACE);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(AppTheme.LINE),
                    BorderFactory.createEmptyBorder(18,18,18,18)));
            setPreferredSize(new Dimension(0,330));
        }

        @Override protected void paintComponent(Graphics g){
            super.paintComponent(g);
            Graphics2D x=(Graphics2D)g.create();
            x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);

            x.setColor(AppTheme.TEXT);
            x.setFont(new Font("Segoe UI",Font.BOLD,14));
            x.drawString("SUBJECT-WISE STUDY TIME",18,24);

            Map<String,Integer> data=ProductivityService.subjectMinutes(workspace);
            if(data.isEmpty()){
                x.setColor(AppTheme.MUTED);
                x.setFont(new Font("Segoe UI",Font.PLAIN,13));
                x.drawString("Log study sessions to populate the chart.",18,55);
                x.dispose();
                return;
            }

            int max=1;
            for(int v:data.values()) max=Math.max(max,v);

            int y=58;
            int labelWidth=Math.min(180,Math.max(120,getWidth()/5));
            int trackWidth=Math.max(220,getWidth()-labelWidth-75);
            int rowHeight=48;

            int i=0;
            for(Map.Entry<String,Integer> e:data.entrySet()){
                if(y>getHeight()-25) break;

                String name=e.getKey();
                if(name.length()>20) name=name.substring(0,20)+"…";

                x.setColor(AppTheme.TEXT);
                x.setFont(new Font("Segoe UI",Font.PLAIN,12));
                x.drawString(name,18,y+15);

                int trackX=labelWidth;
                int trackY=y;
                x.setColor(AppTheme.SURFACE_3);
                x.fillRoundRect(trackX,trackY,trackWidth,20,10,10);

                int barWidth=Math.max(8,(int)(trackWidth*(e.getValue()/(double)max)));
                x.setColor(i%2==0?AppTheme.ACCENT:AppTheme.ACCENT_2);
                x.fillRoundRect(trackX,trackY,barWidth,20,10,10);

                x.setColor(AppTheme.TEXT);
                x.setFont(new Font("Segoe UI",Font.BOLD,11));
                String val=String.format("%.1f h",e.getValue()/60.0);
                x.drawString(val,Math.min(trackX+barWidth+8,getWidth()-55),y+15);

                y+=rowHeight;
                i++;
            }
            x.dispose();
        }
    }
}
