package model;
import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long serialVersionUID=1L;
    public enum Priority { HIGH, MEDIUM, LOW }
    public enum Status { PENDING, IN_PROGRESS, COMPLETED }
    private String title,subject,description;
    private Priority priority;
    private Status status;
    private LocalDate dueDate;
    public Task(String title,String subject,Priority priority,LocalDate dueDate,String description){
        this.title=title;this.subject=subject;this.priority=priority;this.dueDate=dueDate;this.description=description;this.status=Status.PENDING;
    }
    public String getTitle(){return title;} public String getSubject(){return subject;} public String getDescription(){return description;}
    public Priority getPriority(){return priority;} public Status getStatus(){return status;} public LocalDate getDueDate(){return dueDate;}
    public void setTitle(String v){title=v;} public void setSubject(String v){subject=v;} public void setDescription(String v){description=v;}
    public void setPriority(Priority v){priority=v;} public void setStatus(Status v){status=v;} public void setDueDate(LocalDate v){dueDate=v;}
}
