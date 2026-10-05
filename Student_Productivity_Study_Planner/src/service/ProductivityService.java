package service;
import model.*;
import java.time.LocalDate;
import java.util.*;

public final class ProductivityService {
    private ProductivityService(){}
    public static int completed(Workspace w){return (int)w.tasks.stream().filter(t->t.getStatus()==Task.Status.COMPLETED).count();}
    public static int pending(Workspace w){return (int)w.tasks.stream().filter(t->t.getStatus()!=Task.Status.COMPLETED).count();}
    public static int minutes(Workspace w){return w.sessions.stream().mapToInt(StudySession::getMinutes).sum();}
    public static int productivity(Workspace w){return w.tasks.isEmpty()?0:Math.round(completed(w)*100f/w.tasks.size());}
    public static long dueSoon(Workspace w){
        LocalDate today=LocalDate.now();
        return w.tasks.stream().filter(t->t.getStatus()!=Task.Status.COMPLETED &&
          !t.getDueDate().isBefore(today) && !t.getDueDate().isAfter(today.plusDays(3))).count();
    }
    public static Map<String,Integer> subjectMinutes(Workspace w){
        Map<String,Integer> out=new LinkedHashMap<>();
        for(StudySession s:w.sessions) out.merge(s.getSubject(),s.getMinutes(),Integer::sum);
        return out;
    }
}
