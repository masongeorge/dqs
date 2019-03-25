package Student;

import Helpers.AlertHandler;
import Helpers.Extra;
import Helpers.MySQLHandler;
import Model.*;
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

import java.util.List;
import java.util.Map;

public class StudentSelectedModuleController {

    @FXML
    private Label moduleLabel;

    @FXML
    private Label lecturerLabel;

    @FXML
    private TableView newTableView;

    @FXML
    private TableColumn<Assessment, String> newNameColumn;

    @FXML
    private TableColumn<Assessment, String> newAssignedColumn;

    @FXML
    private TableColumn<Assessment, String> newDueCOlumn;

    @FXML
    private TableColumn<Assessment, String> newTypeColumn;

    @FXML
    private TableView completedTableView;

    @FXML
    private TableColumn<Assessment, String> completedNameColumn;

    @FXML
    private TableColumn<Assessment, String> completedDueColumn;

    @FXML
    private TableColumn<Assessment, String> completedTypeColumn;

    @FXML
    private TableColumn<Assessment, String> completedResultColumn;

    @FXML
    private TableColumn<Assessment, String> completedRetakeColumn;

    private StudentModule selectedModule;

    private ObservableList<Assessment> newAssessments;

    private ObservableList<Assessment> completedAssessments;

    private Assessment selectedNewAssessment;

    private Assessment selectedCompletedAssessment;

    private MySQLHandler SqlHandler;

    private List<Integer> StudentAssessments;

    private List<Integer> StudentCompletedAssessments;

    /*
    Summative = 1 attempt only
    Formative = Multiple attempts

    New assessments:
        - Load all the assessments which have a completed value of 0 and result of -1 and due date isn't passed yet

    Completed assessments:
        - Load all the assessments which have a completed value of 1
        - Load all the assessments which have a completed value of 0 but result is not -1 (these are the formative assessments that the user took but didn't mark it completed yet so user can re-take them)

    Logic if user can retake the test:
        - if assessment == formative and assessment.completed is 0. If yes user can re-take it.

    Logic if user can check the answers:
        - if assessment == Summative: check if due date is passed if yes user can check it.
        - if assessment == Formative: check if assessment.completed is 1. If yes user can check it. (When student takes a formative test, user can choose to complete it and not have more attempts so user can check the answers or not complete it yet so user can have more attempts)

    */
    private StudentUser user;

    @FXML
    public void initialize(){

        setupTableView();
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(StudentUser user, StudentModule selectedModule){
        this.user = user;
        this.selectedModule = selectedModule;

        moduleLabel.setText(selectedModule.getModuleName());
        lecturerLabel.setText("By " + selectedModule.getLecturerName());
        StudentAssessments = SqlHandler.GetStudentAssessments(user.getId());
        StudentCompletedAssessments = SqlHandler.GetCompletedAssessments(user.getId());
        loadNewAssessments();
        loadCompletedAssessments();
    }

    public void setupTableView(){
        // New Table View
        newNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        newAssignedColumn.setCellValueFactory(cellData -> cellData.getValue().getAssignedDateProperty());
        newDueCOlumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
        newTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeStringPropoerty());
        newNameColumn.setStyle("-fx-alignment: CENTER");
        newAssignedColumn.setStyle("-fx-alignment: CENTER");
        newDueCOlumn.setStyle("-fx-alignment: CENTER");
        newTypeColumn.setStyle("-fx-alignment: CENTER");

        newTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Assessment>() {
            @Override
            public void changed(ObservableValue<? extends Assessment> observable, Assessment oldValue, Assessment newValue) {
                selectedNewAssessment = newValue;
            }
        });

        // Completed Table View
        completedNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        completedDueColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
        completedTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeStringPropoerty());
        completedResultColumn.setCellValueFactory(cellData -> cellData.getValue().getResultStringProperty());
        completedRetakeColumn.setCellValueFactory(cellData -> cellData.getValue().getIsRetakePossible());
        completedNameColumn.setStyle("-fx-alignment: CENTER");
        completedDueColumn.setStyle("-fx-alignment: CENTER");
        completedTypeColumn.setStyle("-fx-alignment: CENTER");
        completedResultColumn.setStyle("-fx-alignment: CENTER");
        completedRetakeColumn.setStyle("-fx-alignment: CENTER");

        completedTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Assessment>() {
            @Override
            public void changed(ObservableValue<? extends Assessment> observable, Assessment oldValue, Assessment newValue) {
                selectedCompletedAssessment = newValue;
            }
        });

    }

    public void loadNewAssessments(){
        newAssessments = FXCollections.observableArrayList();
        for (int StudentAssessment : StudentAssessments) {
            String[] AssessmentData = SqlHandler.GetAssessmentData(StudentAssessment);
            if (Extra.IsAssessmentNew(AssessmentData[3])) {
                if (Integer.parseInt(AssessmentData[0]) == selectedModule.getmoduleId()) {
                    newAssessments.add(new Assessment(AssessmentData[1], AssessmentData[2],
                            AssessmentData[3] + Extra.GetDifferenceInTime(AssessmentData[2], AssessmentData[3]), Integer.parseInt(AssessmentData[4]), false, -1));
                }
            }
        }
        newTableView.setItems(newAssessments);
        newTableView.refresh();
    }

    public void loadCompletedAssessments(){
        completedAssessments = FXCollections.observableArrayList();
        for (int StudentAssessment : StudentCompletedAssessments) {
            String[] AssessmentData = SqlHandler.GetAssessmentData(StudentAssessment);
            if (Integer.parseInt(AssessmentData[0]) == selectedModule.getmoduleId()) {
                completedAssessments.add(new Assessment(AssessmentData[1], AssessmentData[2],
                        AssessmentData[3], Integer.parseInt(AssessmentData[4]), false,
                        Double.valueOf(SqlHandler.GetAssessmentResult(StudentAssessment))));
            }
        }
        completedTableView.setItems(completedAssessments);
        completedTableView.refresh();
    }


    public void onBack(){
        loadStudentHome();
    }

    public void onStartNew(){
        if (selectedNewAssessment == null){
            AlertHandler.showErrorAlert("Error", "Select an assessment first!", "Select an assessment from the list first and then try again");
        }else{
            String AssessmentTitle = selectedNewAssessment.getNameProperty().get();
            loadTest(AssessmentTitle);
        }
    }

    public void loadMultipleQuestion(Test test){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentTakesMultipleChoiceUI.fxml"));
            loader.load();
            StudentTakesMultipleChoice controller = loader.getController();
            controller.initData(user, test, 0, selectedModule);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Answer the questions");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void loadTextQuestion(Test test){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentTakesTextUI.fxml"));
            loader.load();
            StudentTakesText controller = loader.getController();
            controller.initData(user, test, 0, selectedModule);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Answer the questions");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void loadTest(String AssessmentTitle){
        int AssessmentID = Integer.parseInt(SqlHandler.GetIdByAssessmentName(AssessmentTitle));

        Map<String, String> GetMultipleChoice = SqlHandler.GetMultipleChoiceQ(AssessmentID);
        Map<String, String> GetRegular = SqlHandler.GerRegularQ(AssessmentID);
        int startQuestionIndex = Integer.parseInt(SqlHandler.GetAssessmentIndexes(AssessmentID).get(0));
        Test test = new Test(GetMultipleChoice, GetRegular, startQuestionIndex);

        if(test.isQuestionMultiple(0)){
            // First Question is multiple choice type
            loadMultipleQuestion(test);
        }else{
            // First Question is text type
            loadTextQuestion(test);
        }
    }

    public void onRetakeFormative(){
        if (selectedCompletedAssessment == null){
            AlertHandler.showErrorAlert("Error", "Select an assessment first!", "Select an assessment from the list first and then try again");
        }else{
            if (selectedCompletedAssessment.isRetakePossible()){
                // Load re-take
                String AssessmentTitle = selectedCompletedAssessment.getNameProperty().get();
                loadTest(AssessmentTitle);
            }else{
                AlertHandler.showShortMessage("Error!", "You can only retake formative tests which you haven't completed yet");
            }
        }
    }

    public void loadStudentHome(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentHomeUI.fxml"));
            loader.load();
            StudentHomeController controller = loader.getController();
            controller.initData(user);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Home");
            stage.setScene(new Scene(parent, 600, 400));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)moduleLabel.getScene().getWindow();
        oldStage.close();
    }

}
