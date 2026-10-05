package ui;
import model.AppData;
import model.User;
import service.DataStore;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final AppData data=DataStore.load();
    private final ModernTextField username=new ModernTextField();
    private final JPasswordField password=new JPasswordField();
    public LoginFrame(){
        setUndecorated(true);setSize(980,610);setMinimumSize(new Dimension(900,560));setLocationRelativeTo(null);setDefaultCloseOperation(EXIT_ON_CLOSE);
        JPanel base=new AnimatedBackground();base.setLayout(new BorderLayout());
        base.add(new WindowBar(this,"STUDENT PLANNER • SECURE ACCESS",false),BorderLayout.NORTH);
        RoundedPanel shell=new RoundedPanel(28,new Color(13,18,29,245),AppTheme.LINE);shell.setLayout(new GridLayout(1,2));shell.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        JPanel intro=new JPanel();intro.setOpaque(false);intro.setBorder(BorderFactory.createEmptyBorder(50,55,45,35));intro.setLayout(new BoxLayout(intro,BoxLayout.Y_AXIS));
        JLabel a=AppTheme.label("STUDENT",42,AppTheme.TEXT);a.setFont(new Font("Segoe UI",Font.BOLD,42));
        JLabel b=AppTheme.label("PLANNER",42,AppTheme.ACCENT);b.setFont(new Font("Segoe UI",Font.BOLD,42));
        intro.add(a);intro.add(b);intro.add(Box.createVerticalStrut(30));
        JLabel line1=AppTheme.label("Plan smarter.",20,AppTheme.MUTED);intro.add(line1);
        JLabel line2=AppTheme.label("Study with focus.",20,AppTheme.MUTED);intro.add(line2);
        JLabel line3=AppTheme.label("Track your progress.",20,AppTheme.MUTED);intro.add(line3);
        intro.add(Box.createVerticalGlue());
        JLabel suite=AppTheme.label("JAVA DESKTOP PRODUCTIVITY SUITE",11,AppTheme.ACCENT_2);suite.setFont(new Font("Segoe UI",Font.BOLD,11));intro.add(suite);
        shell.add(intro);

        RoundedPanel form=new RoundedPanel(0,new Color(17,23,36),null);form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));form.setBorder(BorderFactory.createEmptyBorder(55,70,50,70));
        JLabel wt=AppTheme.label("WELCOME BACK",28,AppTheme.TEXT);wt.setFont(new Font("Segoe UI",Font.BOLD,28));form.add(wt);
        form.add(Box.createVerticalStrut(8));form.add(AppTheme.label("Sign in to continue to your workspace",15,AppTheme.MUTED));form.add(Box.createVerticalStrut(30));
        form.add(AppTheme.label("USERNAME",11,AppTheme.MUTED));form.add(Box.createVerticalStrut(6));username.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));form.add(username);form.add(Box.createVerticalStrut(18));
        form.add(AppTheme.label("PASSWORD",11,AppTheme.MUTED));form.add(Box.createVerticalStrut(6));
        password.setOpaque(false);password.setForeground(AppTheme.TEXT);password.setCaretColor(AppTheme.ACCENT_2);password.setFont(new Font("Segoe UI",Font.PLAIN,14));password.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(AppTheme.LINE),BorderFactory.createEmptyBorder(10,12,10,12)));password.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));form.add(password);form.add(Box.createVerticalStrut(22));
        ModernButton login=new ModernButton("ENTER WORKSPACE   →");login.setMaximumSize(new Dimension(Integer.MAX_VALUE,46));login.addActionListener(e->login());form.add(login);form.add(Box.createVerticalStrut(10));
        JButton register=new JButton("Create a new account");register.setAlignmentX(Component.LEFT_ALIGNMENT);register.setForeground(AppTheme.MUTED);register.setFont(new Font("Segoe UI",Font.PLAIN,13));register.setFocusPainted(false);register.setContentAreaFilled(false);register.setBorder(BorderFactory.createEmptyBorder(8,2,8,2));register.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));register.addActionListener(e->register());form.add(register);
        form.add(Box.createVerticalGlue());form.add(AppTheme.label("LOCAL DATA  •  OFFLINE MODE",11,AppTheme.MUTED));
        shell.add(form);base.add(shell,BorderLayout.CENTER);setContentPane(base);
    }
    private void login(){
        String u=username.getText().trim(),p=new String(password.getPassword());
        if(u.isEmpty()||p.isEmpty()){darkMessage("Please enter username and password.");return;}
        User user=data.users.get(u.toLowerCase());
        if(user!=null && user.getPasswordHash().equals(DataStore.hashPassword(p))){dispose();new MainFrame(data,u).setVisible(true);}
        else darkMessage("Invalid username or password.");
    }
    private void register(){
        String u=username.getText().trim(),p=new String(password.getPassword());
        if(u.length()<3||p.length()<4){darkMessage("Username must be at least 3 characters and password at least 4 characters.");return;}
        String key=u.toLowerCase();
        if(data.users.containsKey(key)){darkMessage("That username already exists.");return;}
        data.users.put(key,new User(u,DataStore.hashPassword(p)));data.workspaceFor(u);DataStore.save(data);darkMessage("Account created. You can now sign in.");
    }
    private void darkMessage(String msg){
        UIManager.put("OptionPane.background",AppTheme.SURFACE);
        UIManager.put("Panel.background",AppTheme.SURFACE);
        UIManager.put("OptionPane.messageForeground",AppTheme.TEXT);
        UIManager.put("OptionPane.buttonBackground",AppTheme.SURFACE_3);
        UIManager.put("OptionPane.buttonForeground",AppTheme.TEXT);
        UIManager.put("Button.background",AppTheme.SURFACE_3);
        UIManager.put("Button.foreground",AppTheme.TEXT);
        JOptionPane.showMessageDialog(this,msg,"Student Planner",JOptionPane.INFORMATION_MESSAGE);
    }
}
