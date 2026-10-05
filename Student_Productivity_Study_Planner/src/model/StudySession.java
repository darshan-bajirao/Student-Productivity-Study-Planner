package model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class StudySession implements Serializable {
    private static final long serialVersionUID=1L;
    private final String subject;
    private final int minutes;
    private final LocalDateTime startedAt;
    public StudySession(String subject,int minutes){this.subject=subject;this.minutes=minutes;this.startedAt=LocalDateTime.now();}
    public String getSubject(){return subject;} public int getMinutes(){return minutes;} public LocalDateTime getStartedAt(){return startedAt;}
}
