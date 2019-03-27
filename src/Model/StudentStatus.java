package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentStatus {
    private int userID;
    private StringProperty studentName;
    private StringProperty studentStatus;

    public StudentStatus(int userID, String studentName, String studentStatus){
        this.userID = userID;
        this.studentName = new SimpleStringProperty(studentName);
        this.studentStatus = new SimpleStringProperty(studentStatus);
    }

    public int getUserID(){
        return userID;
    }

    public String getStudentName(){
        return studentName.get();
    }

    public void setStudentName(String studentName){
        this.studentName = new SimpleStringProperty(studentName);
    }

    public StringProperty getStudentNameProperty(){
        return studentName;
    }

    public String getStudentStatus(){
        return studentStatus.get();
    }

    public void setStudentStatus(String studentStatus){
        this.studentStatus = new SimpleStringProperty(studentStatus);
    }

    public StringProperty getStudentStatusProperty(){
        return studentStatus;
    }
}
