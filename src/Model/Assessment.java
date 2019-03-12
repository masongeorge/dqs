package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Assessment {
    private StringProperty name;
    private StringProperty assignedDate;
    private StringProperty dueDate;
    private int type; // 0 = Formative, 1 = "Summative"
    private boolean completed;
    private double result;

    public Assessment(String name, String assignedDate, String dueDate, int type, boolean completed, double result) {
        this.name = new SimpleStringProperty(name);;
        this.assignedDate = new SimpleStringProperty(assignedDate);;
        this.dueDate = new SimpleStringProperty(dueDate);;
        this.type = type;
        this.result = result;
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean isRetakePossible(){
        return false;
    }

    public boolean isCheckAnswersPossible(){
        return false;
    }

    public StringProperty getIsRetakePossible(){
        // if due date is not passed and type is formative and completed is false then YES
        if (isRetakePossible()){
            return new SimpleStringProperty( "Yes");
        }else{
            return new SimpleStringProperty( "No");
        }

    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
