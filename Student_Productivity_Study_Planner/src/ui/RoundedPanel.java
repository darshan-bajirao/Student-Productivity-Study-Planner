package ui;
import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private final int radius;
    private final Color fill;
    private final Color border;
    public RoundedPanel(int radius,Color fill,Color border){this.radius=radius;this.fill=fill;this.border=border;setOpaque(false);}
    protected void paintComponent(Graphics g){
        Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        x.setColor(fill);x.fillRoundRect(0,0,getWidth()-1,getHeight()-1,radius,radius);
        if(border!=null){x.setColor(border);x.drawRoundRect(0,0,getWidth()-1,getHeight()-1,radius,radius);}
        x.dispose();super.paintComponent(g);
    }
}
