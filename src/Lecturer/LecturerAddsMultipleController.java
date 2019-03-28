package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.*;
import com.sun.org.apache.xpath.internal.operations.Mult;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LecturerAddsMultipleController {

    @FXML
    private Label counterLabel;

    @FXML
    private TextField titleField;

    @FXML
    private TextField answer1Field;

    @FXML
    private TextField answer2Field;

    @FXML
    private TextField answer3Field;

    @FXML
    private RadioButton answer1Radio;

    @FXML
    private RadioButton answer2Radio;

    @FXML
    private RadioButton answer3Radio;

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
        MultipleQuestion question = (MultipleQuestion) questions[currentQuestionIndex];
        titleField.setText(question.getTitle());
        answer1Field.setText(question.getAnswer1());
        answer2Field.setText(question.getAnswer2());
        answer3Field.setText(question.getAnswer3());

        if(question.getCorrectAnswer().equals(question.getAnswer1())){
            onAnswer1();
        }else if(question.getCorrectAnswer().equals(question.getAnswer2())){
            onAnswer2();
        }else if(question.getCorrectAnswer().equals(question.getAnswer3())){
            onAnswer3();
        }
    }

    public void onCancel(){
        loadCreateAssessmentScreen();
    }

    public void onSave(){
        if(titleField.getText().length() > 0 && answer1Field.getText().length() > 0 && answer2Field.getText().length() > 0 && answer3Field.getText().length() > 0 && (answer1Radio.isSelected() || answer2Radio.isSelected() || answer3Radio.isSelected())){
            String correctAnswer = "";
            if(answer1Radio.isSelected()){
                correctAnswer = answer1Field.getText();
            }else if(answer2Radio.isSelected()){
                correctAnswer = answer2Field.getText();
            }else{
                correctAnswer = answer3Field.getText();
            }
            questions[currentQuestionIndex] = new MultipleQuestion(titleField.getText(), "m", answer1Field.getText(), answer2Field.getText(), answer3Field.getText(), correctAnswer);
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

    public void onAnswer1(){
        answer1Radio.setSelected(true);
        answer2Radio.setSelected(false);
        answer3Radio.setSelected(false);
    }

    public void onAnswer2(){
        answer1Radio.setSelected(false);
        answer2Radio.setSelected(true);
        answer3Radio.setSelected(false);
    }

    public void onAnswer3(){
        answer1Radio.setSelected(false);
        answer2Radio.setSelected(false);
        answer3Radio.setSelected(true);
    }

    public void closeScreen(){
        Stage oldStage = (Stage)answer1Radio.getScene().getWindow();
        oldStage.close();
    }
}
