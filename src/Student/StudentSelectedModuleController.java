package Student;

import Helpers.AlertHandler;
import Model.Assessment;
import Model.StudentModule;
import Model.StudentUser;
import Model.User;
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

    private StudentModule selectedModule;

    private ObservableList<Assessment> newAssessments;

    private ObservableList<Assessment> completedAssessments;

    private Assessment selectedNewAssessment;

    private Assessment selectedCompletedAssessment;

    // New assessments: These are the assessments that the user has not taken yet and their deadline hasn't passed yet
    // Completed assessments:   - These are the assessments that the user took.
    //                          - If the user didn't finalise a formative assessment(multiple attempts) and its deadline is not past yet then the user can re-take it.

    // Check answers: Only possible for summative assessments if their deadline has passed. Only possible for formative assessments if user finalised it(meaning cannot retake it) but deadline doesn't need to be passed for this type of assessment.

    private StudentUser user;

    @FXML
    public void initialize(){
        setupTableView();
    }

    public void initData(StudentUser user){
        this.user = user;

        // use selectedModule
        moduleLabel.setText("Maths");
        lecturerLabel.setText("By " + "Brittney Senna");
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
                completedTableView.getSelectionModel().clearSelection();
            }
        });

        // Completed Table View
        completedNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        completedDueColumn.setCellValueFactory(cellData -> cellData.getValue().getDueDateProperty());
        completedTypeColumn.setCellValueFactory(cellData -> cellData.getValue().getTypeStringPropoerty());
        completedResultColumn.setCellValueFactory(cellData -> cellData.getValue().getResultStringProperty());
        completedNameColumn.setStyle("-fx-alignment: CENTER");
        completedDueColumn.setStyle("-fx-alignment: CENTER");
        completedTypeColumn.setStyle("-fx-alignment: CENTER");
        completedResultColumn.setStyle("-fx-alignment: CENTER");
        completedTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Assessment>() {
            @Override
            public void changed(ObservableValue<? extends Assessment> observable, Assessment oldValue, Assessment newValue) {
                selectedCompletedAssessment = newValue;
                newTableView.getSelectionModel().clearSelection();
            }
        });

    }

    public void loadNewAssessments(){
        newAssessments = FXCollections.observableArrayList();
        newAssessments.add(new Assessment(1,"Derivation Test", "01/03/2019", "12/03/2019", 0));
        newAssessments.add(new Assessment(2,"Integration Test", "02/03/2019", "22/03/2019", 1));

        newTableView.setItems(newAssessments);
        newTableView.refresh();
    }

    public void loadCompletedAssessments(){
        completedAssessments = FXCollections.observableArrayList();
        completedAssessments.add(new Assessment(1,"Geometry Test", "11/02/2019", "12/02/2019", 0, 98));
        completedAssessments.add(new Assessment(2,"Statistics Test", "12/02/2019", "22/02/2019", 1, 78));

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
            System.out.println(selectedNewAssessment.getNameProperty().get());
            // Load assessment...
        }
    }

    public void onCheckAnswers(){
        if (selectedCompletedAssessment == null){
            AlertHandler.showErrorAlert("Error", "Select an assessment first!", "Select an assessment from the list first and then try again");
        }else{
            System.out.println(selectedCompletedAssessment.getNameProperty().get());
            if (selectedCompletedAssessment.getType() == 0){
                // User selected a formative assessment
                // we need to check if the user finalised it, otherwise we cannot show the answers
                // To finalise it, the user needs to take the test and click on finalise it before submitting it so the user can no longer have new attempts
                // If the user didn't finalise it yet, then we cannot show the answers yet

                // But we can also ask the user to finalise it when user clicks on this "check answers" button, if user didn't finalise it yet

            }else if (selectedCompletedAssessment.getType() == 1){
                // User selected a summative assessment
                // We need to check if the assessment's deadline has passed, if yes then user can check the answers
            }
        }
    }

    public void onRetakeFormative(){
        if (selectedCompletedAssessment == null){
            AlertHandler.showErrorAlert("Error", "Select an assessment first!", "Select an assessment from the list first and then try again");
        }else{
            System.out.println(selectedCompletedAssessment.getNameProperty().get());
            // User wants to have another attempt on a formative assessment
            // We need to check if the user finalised this assessment already
            // If user already finalised it then user cannot retake it, otherwise user can retake it(new attempt)
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
