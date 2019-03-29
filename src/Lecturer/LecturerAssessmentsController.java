package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.Assessment;
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
import javafx.stage.Stage;


public class LecturerAssessmentsController {

    @FXML
    private TableView assessmentsTable;

    @FXML
    private TableColumn<Assessment, String> titleColumn;

    @FXML
    private TableColumn<Assessment, String> typeColumn;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private ObservableList<Assessment> assessments;

    private Assessment selectedAssessment;

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
        loadAssessments();
    }

    public void loadAssessments(){
        assessments = FXCollections.observableArrayList();

        Assessment a1 = new Assessment("Assignment 1", "2019-03-30 00:00:00", "2019-03-30 00:00:00", 0);
        Assessment a2 = new Assessment("Assignment 2", "2019-03-30 00:00:00", "2019-03-30 00:00:00", 1);

        assessments.add(a1);
        assessments.add(a2);

        assessmentsTable.setItems(assessments);
        assessmentsTable.refresh();
    }

    public void setupTableView(){
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeStringPropoerty());
        titleColumn.setStyle("-fx-alignment: CENTER");
        typeColumn.setStyle("-fx-alignment: CENTER");

        assessmentsTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Assessment>() {
            @Override
            public void changed(ObservableValue<? extends Assessment> observable, Assessment oldValue, Assessment newValue) {
                selectedAssessment = newValue;
            }
        });
    }

    public void onBack(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerSelectedModuleUI.fxml"));
            loader.load();
            LecturerSelectedModuleController controller = loader.getController();
            controller.initData(lecturer, selectedModule);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Module");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void onSelect(){
        if(selectedAssessment == null){
            AlertHandler.showShortMessage("Error", "Make sure to select an assessment first!");

        }else{
            loadAssessment();
        }
    }

    public void loadAssessment(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerSelectedAssessmentUI.fxml"));
            loader.load();
            LecturerSelectedAssessmentController controller = loader.getController();
            controller.initData(lecturer, selectedModule, selectedAssessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Your Assessment");
            stage.setScene(new Scene(parent, 600, 245));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)assessmentsTable.getScene().getWindow();
        oldStage.close();
    }

}
