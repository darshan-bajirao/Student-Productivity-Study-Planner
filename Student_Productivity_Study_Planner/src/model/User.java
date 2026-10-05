package model;
import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String passwordHash;
    public User(String username, String passwordHash){this.username=username;this.passwordHash=passwordHash;}
    public String getUsername(){return username;}
    public String getPasswordHash(){return passwordHash;}
}
