package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Assessment {
    private int id;
    private StringProperty name;
    private StringProperty assignedDate;
    private StringProperty dueDate;
    private int type; // 0 = Formative, 1 = "Summative"
    private double result;

    public Assessment(int id, String name, String assignedDate, String dueDate, int type) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.assignedDate = new SimpleStringProperty(assignedDate);;
        this.dueDate = new SimpleStringProperty(dueDate);;
        this.type = type;
    }

    // With Result
    public Assessment(int id, String name, String assignedDate, String dueDate, int type, double result) {
        this.id = id;
        this.name = new SimpleStringProperty(name);;
        this.assignedDate = new SimpleStringProperty(assignedDate);;
        this.dueDate = new SimpleStringProperty(dueDate);;
        this.type = type;
        this.result = result;
    }

    public String getResultString(){
        return Double.toString(result) + "%";
    }

    public String getTypeString(){
        return type == 0 ? "Formative" : "Summative";
    }

    public StringProperty getTypeStringPropoerty(){
        return new SimpleStringProperty(getTypeString());
    }

    public StringProperty getResultStringProperty(){

        return new SimpleStringProperty(getResultString());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty getAssignedDateProperty() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate.set(assignedDate);
    }

    public StringProperty getDueDateProperty() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
