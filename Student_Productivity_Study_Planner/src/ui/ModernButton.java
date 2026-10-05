package ui;
import javax.swing.*;
import java.awt.*;

public class ModernButton extends JButton {
    private float hover=0f;
    private final Timer anim;
    private final Color normal,over;
    public ModernButton(String text){this(text,AppTheme.ACCENT);}
    public ModernButton(String text,Color base){
        super(text);normal=base;over=base.brighter();setForeground(Color.WHITE);setFont(new Font("Segoe UI",Font.BOLD,13));setFocusPainted(false);setBorderPainted(false);setContentAreaFilled(false);setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));setBorder(BorderFactory.createEmptyBorder(11,16,11,16));
        anim=new Timer(16,e->{float target=getModel().isRollover()?1f:0f;hover += (target-hover)*0.18f;repaint();});
        anim.start();
    }
    protected void paintComponent(Graphics g){
        Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Color c=blend(normal,over,hover);
        if(!getModel().isEnabled())c=new Color(51,58,75);
        x.setColor(c);x.fillRoundRect(0,0,getWidth(),getHeight(),14,14);
        if(getModel().isPressed()){x.setColor(new Color(255,255,255,25));x.fillRoundRect(0,0,getWidth(),getHeight(),14,14);}
        x.dispose();super.paintComponent(g);
    }
    private Color blend(Color a,Color b,float t){return new Color((int)(a.getRed()*(1-t)+b.getRed()*t),(int)(a.getGreen()*(1-t)+b.getGreen()*t),(int)(a.getBlue()*(1-t)+b.getBlue()*t));}
}
