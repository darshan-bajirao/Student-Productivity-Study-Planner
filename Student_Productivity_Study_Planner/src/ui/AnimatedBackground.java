package ui;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AnimatedBackground extends JPanel {
    private static class Orb {float x,y,r,dx,dy,alpha; Color c; Orb(float x,float y,float r,float dx,float dy,float alpha,Color c){this.x=x;this.y=y;this.r=r;this.dx=dx;this.dy=dy;this.alpha=alpha;this.c=c;}}
    private final List<Orb> orbs=new ArrayList<>();
    private final javax.swing.Timer timer;
    public AnimatedBackground(){
        setOpaque(false);Random r=new Random(7);
        for(int i=0;i<18;i++)orbs.add(new Orb(r.nextFloat(),r.nextFloat(),8+r.nextFloat()*32,(r.nextFloat()-.5f)*.0007f,(r.nextFloat()-.5f)*.0007f,.12f+r.nextFloat()*.12f,(i%2==0?AppTheme.ACCENT:AppTheme.ACCENT_2)));
        timer=new javax.swing.Timer(30,e->{for(Orb o:orbs){o.x+=o.dx;o.y+=o.dy;if(o.x<0||o.x>1)o.dx=-o.dx;if(o.y<0||o.y>1)o.dy=-o.dy;}repaint();});timer.start();
    }
    protected void paintComponent(Graphics g){
        super.paintComponent(g);Graphics2D x=(Graphics2D)g.create();x.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        x.setColor(AppTheme.BG);x.fillRect(0,0,getWidth(),getHeight());
        for(Orb o:orbs){int px=(int)(o.x*getWidth()),py=(int)(o.y*getHeight()),d=(int)o.r*2;Paint p=new RadialGradientPaint(px,py,d/2f,new float[]{0f,1f},new Color[]{new Color(o.c.getRed(),o.c.getGreen(),o.c.getBlue(),(int)(255*o.alpha)),new Color(o.c.getRed(),o.c.getGreen(),o.c.getBlue(),0)});x.setPaint(p);x.fillOval(px-(int)o.r,py-(int)o.r,d,d);}
        x.setColor(new Color(255,255,255,7));
        for(int i=0;i<getWidth();i+=40)x.drawLine(i,0,i,getHeight());
        for(int j=0;j<getHeight();j+=40)x.drawLine(0,j,getWidth(),j);
        x.dispose();
    }
}
