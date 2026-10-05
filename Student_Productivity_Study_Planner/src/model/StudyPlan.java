package model;
import java.io.Serializable;

public class StudyPlan implements Serializable {
    private static final long serialVersionUID=1L;
    private String day,time,subject,topic;
    public StudyPlan(String day,String time,String subject,String topic){this.day=day;this.time=time;this.subject=subject;this.topic=topic;}
    public String getDay(){return day;} public String getTime(){return time;} public String getSubject(){return subject;} public String getTopic(){return topic;}
}
