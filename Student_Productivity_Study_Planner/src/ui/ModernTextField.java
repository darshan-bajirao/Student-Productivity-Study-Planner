package ui;
import javax.swing.*;
import java.awt.*;

public class ModernTextField extends JTextField {
    public ModernTextField(){setOpaque(false);setForeground(AppTheme.TEXT);setCaretColor(AppTheme.ACCENT_2);setFont(new Font("Segoe UI",Font.PLAIN,14));setBorder(BorderFactory.createEmptyBorder(10,12,10,12));}
    protected void paintComponent(Graphics g){
        Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        x.setColor(AppTheme.SURFACE);x.fillRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
        x.setColor(hasFocus()?AppTheme.ACCENT:AppTheme.LINE);x.drawRoundRect(0,0,getWidth()-1,getHeight()-1,12,12);
        x.dispose();super.paintComponent(g);
    }
}
