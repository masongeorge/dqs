package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LecturerAddsTextController {

    @FXML
    private Label counterLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField correctField;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private Question questions[];

    private int currentQuestionIndex;

    private Assessment assessment;

    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(Lecturer lecturer, LecturerModule selectedModule, Question questions[], int currentQuestionIndex, Assessment assessment){
        this.lecturer = lecturer;
        this.selectedModule = selectedModule;
        this.questions = questions;
        this.currentQuestionIndex = currentQuestionIndex;
        this.assessment = assessment;

        counterLabel.setText("Question " + (currentQuestionIndex + 1));

        if(questions[currentQuestionIndex] != null){
            loadQuestionData();
        }
    }

    public void loadQuestionData(){
        TextQuestion question = (TextQuestion) questions[currentQuestionIndex];
        titleField.setText(question.getTitle());
        correctField.setText(question.getCorrectAnswer());
    }

    public void onCancel(){
        loadCreateAssessmentScreen();
    }

    public void onSave(){
        if(titleField.getText().length() > 0 && correctField.getText().length() > 0){
            questions[currentQuestionIndex] = new TextQuestion(titleField.getText(), "n", correctField.getText());
            loadCreateAssessmentScreen();
        }else{
            AlertHandler.showShortMessage("Error", "Please fill out every field!");

        }
    }

    public void loadCreateAssessmentScreen(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerCreatesAssessmentUI.fxml"));
            loader.load();
            LecturerCreatesAssessmentController controller = loader.getController();
            controller.initData(lecturer, selectedModule, questions, assessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Create Assessment");
            stage.setScene(new Scene(parent, 786, 312));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)counterLabel.getScene().getWindow();
        oldStage.close();
    }


}
