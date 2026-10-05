package model;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Note implements Serializable {
    private static final long serialVersionUID=1L;
    private String title,subject,content;
    private LocalDateTime updatedAt;
    public Note(String title,String subject,String content){this.title=title;this.subject=subject;this.content=content;this.updatedAt=LocalDateTime.now();}
    public String getTitle(){return title;} public String getSubject(){return subject;} public String getContent(){return content;} public LocalDateTime getUpdatedAt(){return updatedAt;}
    public void setTitle(String v){title=v;touch();} public void setSubject(String v){subject=v;touch();} public void setContent(String v){content=v;touch();}
    private void touch(){updatedAt=LocalDateTime.now();}
}
