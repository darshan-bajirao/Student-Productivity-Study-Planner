package model;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class AppData implements Serializable {
    private static final long serialVersionUID=3L;
    public final Map<String,User> users=new LinkedHashMap<>();
    public final Map<String,Workspace> workspaces=new LinkedHashMap<>();

    public Workspace workspaceFor(String username){
        return workspaces.computeIfAbsent(username, k -> new Workspace());
    }
}
