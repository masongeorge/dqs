package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.Lecturer;
import Model.LecturerModule;
import Model.StudentStatus;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class LecturerSelectedModuleController {
    @FXML
    private Label moduleLabel;

    @FXML
    private TableView studentsTable;

    @FXML
    private TableColumn<StudentStatus, String> nameColumn;

    @FXML
    private TableColumn<StudentStatus, String> statusColumn;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private ObservableList<StudentStatus> studentStatuses;

    private StudentStatus selectedStudentStatus;

    private int selectedIndex;

    @FXML
    public void initialize(){
        setupTableView();
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(Lecturer lecturer, LecturerModule selectedModule){
        this.lecturer = lecturer;
        this.selectedModule = selectedModule;
        loadModule();
        loadStudentStatuses();
    }

    public void loadModule(){
        moduleLabel.setText(selectedModule.getModuleName());
    }

    public void loadStudentStatuses(){
        studentStatuses = FXCollections.observableArrayList();
        StudentStatus student1 = new StudentStatus(1, "Jack Jeniffer", "Added");
        StudentStatus student2 = new StudentStatus(1, "Peter Jeniffer", "Removed");

        studentStatuses.add(student1);
        studentStatuses.add(student2);

        studentsTable.setItems(studentStatuses);
        studentsTable.refresh();
    }

    public void setupTableView(){
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentNameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().getStudentStatusProperty());
        nameColumn.setStyle("-fx-alignment: CENTER");
        statusColumn.setStyle("-fx-alignment: CENTER");

        studentsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentStatus>() {
            @Override
            public void changed(ObservableValue<? extends StudentStatus> observable, StudentStatus oldValue, StudentStatus newValue) {
                selectedStudentStatus = newValue;
                selectedIndex = studentStatuses.indexOf(newValue);
            }
        });
    }

    public void onCreate(){

    }

    public void onView(){

    }

    public void onAdd(){
        if(selectedStudentStatus == null){
            AlertHandler.showShortMessage("Error", "Make sure to select a student first!");
        }else{
            // Add user to the module and update status
            selectedStudentStatus.setStudentStatus("Added");
            studentStatuses.set(selectedIndex, selectedStudentStatus);
        }
    }

    public void onRemove(){
        if(selectedStudentStatus == null){
            AlertHandler.showShortMessage("Error", "Make sure to select a student first!");
        }else{
            // Remove user from the module and update status
            selectedStudentStatus.setStudentStatus("Removed");
            studentStatuses.set(selectedIndex, selectedStudentStatus);
        }
    }

    public void onBack(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerHomeUI.fxml"));
            loader.load();
            LecturerHomeController controller = loader.getController();
            controller.initData(lecturer);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Modules");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)studentsTable.getScene().getWindow();
        oldStage.close();
    }
}
