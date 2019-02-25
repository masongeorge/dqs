package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StudentModule {
    private StringProperty moduleName;
    private StringProperty lecturerName;

    public StudentModule(String moduleName, String lecturerName){
        this.moduleName = new SimpleStringProperty(moduleName);
        this.lecturerName = new SimpleStringProperty(lecturerName);
    }

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
}
