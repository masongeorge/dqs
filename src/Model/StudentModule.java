package Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentModule {
    private IntegerProperty moduleId;
    private StringProperty moduleName;
    private StringProperty lecturerName;

    public StudentModule(int moduleId, String moduleName, String lecturerName){
        this.moduleId = new SimpleIntegerProperty(moduleId);
        this.moduleName = new SimpleStringProperty(moduleName);
        this.lecturerName = new SimpleStringProperty(lecturerName);
    }

    public IntegerProperty getModuleIdProperty() { return moduleId; }

    public StringProperty getModuleNameProperty() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName.set(moduleName);
    }

    public StringProperty getLecturerNameProperty() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName.set(lecturerName);
    }

    public String getModuleName(){
        return moduleName.get();
    }

    public String getLecturerName(){
        return lecturerName.get();
    }

    public Integer getmoduleId(){
        return getModuleIdProperty().get();
    }
}
