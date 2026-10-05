package model;
import java.io.Serializable;

public class Subject implements Serializable {
    private static final long serialVersionUID=1L;
    private String name,code,teacher;
    private int targetHours;
    public Subject(String name,String code,String teacher,int targetHours){this.name=name;this.code=code;this.teacher=teacher;this.targetHours=targetHours;}
    public String getName(){return name;} public String getCode(){return code;} public String getTeacher(){return teacher;} public int getTargetHours(){return targetHours;}
    public void setName(String v){name=v;} public void setCode(String v){code=v;} public void setTeacher(String v){teacher=v;} public void setTargetHours(int v){targetHours=v;}
}
