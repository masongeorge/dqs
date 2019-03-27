package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LecturerModule {
    private int moduleID;
    private StringProperty moduleName;

    public LecturerModule(int moduleID, String moduleName){
        this.moduleID = moduleID;
        this.moduleName = new SimpleStringProperty(moduleName);
    }

    public int getModuleID(){
        return moduleID;
    }

    public StringProperty getModuleNameProperty(){
        return moduleName;
    }

    public String getModuleName(){
        return moduleName.get();
    }

    public void setModuleName(String moduleName){
        this.moduleName = new SimpleStringProperty(moduleName);
    }
}
