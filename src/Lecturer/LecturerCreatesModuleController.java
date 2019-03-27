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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LecturerCreatesModuleController {

    @FXML
    private TextField nameField;

    @FXML
    private TableView studentsTable;

    @FXML
    private TableColumn<StudentStatus, String> nameColumn;

    @FXML
    private TableColumn<StudentStatus, String> statusColumn;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private ObservableList<StudentStatus> studentStatuses;

    private StudentStatus selectedStudentStatus;

    private int selectedIndex;

    private List<Integer> StudentsToEnroll = new ArrayList<>();

    @FXML
    public void initialize(){
        setupTableView();
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(Lecturer lecturer){
        this.lecturer = lecturer;
        loadStudentStatuses();
    }

    public void loadStudentStatuses(){
        studentStatuses = FXCollections.observableArrayList();
        List<Integer> Students = SqlHandler.GetAllStudents();
        for (int st : Students) {
            StudentStatus student = new StudentStatus(st, SqlHandler.GetNameById(st), "-");
            studentStatuses.add(student);
        }

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

    public void onAdd(){
        if(selectedStudentStatus == null){
            AlertHandler.showShortMessage("Error", "Make sure to select a student first!");
        }else{
            if (StudentsToEnroll.contains(selectedStudentStatus.getUserID())) {
                AlertHandler.showShortMessage("Error",
                        String.format("You have already added %s to the module!", selectedStudentStatus.getStudentName()));
            } else {
                StudentsToEnroll.add(selectedStudentStatus.getUserID());
                selectedStudentStatus.setStudentStatus("Added");
                studentStatuses.set(selectedIndex, selectedStudentStatus);
                AlertHandler.showSuccessAlert("Student added successfully",
                        String.format(selectedStudentStatus.getStudentName() + " has been successfully added to the module!"));
            }
        }
    }

    public void onRemove(){
        if(selectedStudentStatus == null){
            AlertHandler.showShortMessage("Error", "Make sure to select a student first!");
        }else{
            if (StudentsToEnroll.contains(selectedStudentStatus.getUserID())) {
                // Remove user from the module and update status
                selectedStudentStatus.setStudentStatus("Removed");
                studentStatuses.set(selectedIndex, selectedStudentStatus);
                StudentsToEnroll.remove(new Integer(selectedStudentStatus.getUserID()));
                AlertHandler.showSuccessAlert("Student removed successfully",
                        String.format(selectedStudentStatus.getStudentName() + " has been successfully removed from the module!"));

            } else {
                AlertHandler.showShortMessage("Error",
                        String.format("You have already removed %s from the module!", selectedStudentStatus.getStudentName()));
            }
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)studentsTable.getScene().getWindow();
        oldStage.close();
    }

    public void onSave(){
        // Create the module
        if (SqlHandler.CreateNewModule(nameField.getText(), lecturer.getId())) {
            for (int st : StudentsToEnroll) {
                SqlHandler.EnrollStudent(st, SqlHandler.GetLastModuleId());
            }
            AlertHandler.showErrorAlert("Module created successfully",
                    String.format("Module %s has been created successfully.", nameField.getText()),
                    "All the students have been added to the module successfully.");
            StudentsToEnroll.clear();
            onBack();
        } else {
            AlertHandler.showShortMessage("Error", "Error occured while creating a module!");
        }
    }

}
