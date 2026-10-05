package model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Workspace implements Serializable {
    private static final long serialVersionUID=1L;
    public final List<Subject> subjects=new ArrayList<>();
    public final List<Task> tasks=new ArrayList<>();
    public final List<StudySession> sessions=new ArrayList<>();
    public final List<Note> notes=new ArrayList<>();
    public final List<StudyPlan> plans=new ArrayList<>();
}
