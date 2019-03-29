package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentResult {
    private StringProperty studentName;
    private StringProperty result;

    public StudentResult(String studentName, String result) {
        this.studentName = new SimpleStringProperty(studentName);
        this.result = new SimpleStringProperty(result);
    }

    public String getStudentName() {
        return studentName.get();
    }

    public StringProperty getStudentNameProperty() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName.set(studentName);
    }

    public String getResult() {
        return result.get();
    }

    public StringProperty getResultProperty() {
        return result;
    }

    public void setResult(String result) {
        this.result.set(result);
    }
}
