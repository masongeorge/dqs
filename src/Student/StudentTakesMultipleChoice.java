package Student;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.stage.Stage;

import java.util.ArrayList;

public class StudentTakesMultipleChoice {

    @FXML
    private Label counterLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private RadioButton answer1Radio;

    @FXML
    private RadioButton answer2Radio;

    @FXML
    private RadioButton answer3Radio;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    private StudentModule selectedModule;

    private MultipleQuestion question;

    private StudentUser user;

    private Test test;

    private int currentQuestionIndex;

    @FXML
    public void initialize(){

    }

    public void initData(StudentUser user, Test test, int currentQuestionIndex, StudentModule selectedModule){
        this.user = user;
        this.test = test;
        this.currentQuestionIndex = currentQuestionIndex;
        this.selectedModule = selectedModule;

        loadQuestion();
    }

    public void loadQuestion(){
        question = (MultipleQuestion) test.questions[currentQuestionIndex];
        updateQuestionUI();
    }

    public void updateQuestionUI(){
        answer1Radio.setSelected(false);
        answer2Radio.setSelected(false);
        answer3Radio.setSelected(false);

        String userAnswer = test.answers[currentQuestionIndex];
        if(userAnswer.equals(question.getAnswer1())){
            answer1Radio.setSelected(true);
        }else if(userAnswer.equals(question.getAnswer2())){
            answer2Radio.setSelected(true);
        }else if(userAnswer.equals(question.getAnswer3())){
            answer3Radio.setSelected(true);
        }

        counterLabel.setText("#" + (currentQuestionIndex+1));
        questionLabel.setText(question.getTitle());
        answer1Radio.setText(question.getAnswer1());
        answer2Radio.setText(question.getAnswer2());
        answer3Radio.setText(question.getAnswer3());

        if(currentQuestionIndex == 0){
            previousButton.setVisible(false);
        }else{
            previousButton.setVisible(true);
        }

        if(currentQuestionIndex == 4){
            nextButton.setText("Finish");
        }else{
            nextButton.setText("Next");
            nextButton.setVisible(true);
        }
    }

    public void onExit(){
        // Going back to student selected module screen
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentSelectedModuleUI.fxml"));
            loader.load();
            StudentSelectedModuleController controller = loader.getController();
            controller.initData(user, selectedModule);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Module");
            stage.setScene(new Scene(parent, 734, 643));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void saveSelectedAnswer(){
        if(answer1Radio.isSelected()){
            test.answers[currentQuestionIndex] = question.getAnswer1();
        }else if(answer2Radio.isSelected()){
            test.answers[currentQuestionIndex] = question.getAnswer2();
        }else if(answer3Radio.isSelected()){
            test.answers[currentQuestionIndex] = question.getAnswer3();
        }
    }

    public void loadNewQuestion(){
        if(test.isQuestionMultiple(currentQuestionIndex)){
            // Load multiple
            loadQuestion();
        }else{
            // Load text
            loadTextQuestion();
        }
    }

    public void loadTextQuestion(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentTakesTextUI.fxml"));
            loader.load();
            StudentTakesText controller = loader.getController();
            controller.initData(user, test, currentQuestionIndex, selectedModule);

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

    public void closeScreen(){
        Stage oldStage = (Stage)counterLabel.getScene().getWindow();
        oldStage.close();
    }

    public void onPrevious(){
        // save user's response
        saveSelectedAnswer();
        currentQuestionIndex--;
        loadNewQuestion();
    }

    public void onNext(){
        // save user's response
        saveSelectedAnswer();
        if(currentQuestionIndex == 4){
            // Submit test...
            submitTest();
        }else{
            // Load next question
            currentQuestionIndex++;
            loadNewQuestion();
        }
    }

    public void submitTest(){
        // User's score
        int userScore = test.calculateScore();
        double percent = userScore/5*100;
        int correctAnswers = test.correctAnswers();
        int wrongAnswers = test.wrongAnswers();

        System.out.println(userScore);
        System.out.println(percent);
        System.out.println(correctAnswers);
        System.out.println(wrongAnswers);

        // NEED TO UPLOAD THOSE

        // Going back to module selected screen
        onExit();
    }


    public void onAnswer1Radio(){
        answer1Radio.setSelected(true);
        answer2Radio.setSelected(false);
        answer3Radio.setSelected(false);
    }

    public void onAnswer2Radio(){
        answer1Radio.setSelected(false);
        answer2Radio.setSelected(true);
        answer3Radio.setSelected(false);
    }

    public void onAnswer3Radio(){
        answer1Radio.setSelected(false);
        answer2Radio.setSelected(false);
        answer3Radio.setSelected(true);
    }

}
