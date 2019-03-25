package Student;

import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class StudentTakesText {

    @FXML
    private Label counterLabel;

    @FXML
    private Label questionLabel;

    @FXML
    private TextArea answerTextArea;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    private StudentModule selectedModule;

    private TextQuestion question;

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
        question = (TextQuestion) test.questions[currentQuestionIndex];
        updateQuestionUI();
    }

    public void updateQuestionUI(){
        String userAnswer = test.answers[currentQuestionIndex];
        answerTextArea.setText(userAnswer);

        counterLabel.setText("#" + (currentQuestionIndex+1));
        questionLabel.setText(question.getTitle());

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

    public void saveUserAnswer(){
        test.answers[currentQuestionIndex] = answerTextArea.getText();
    }

    public void loadNewQuestion(){
        if(test.isQuestionMultiple(currentQuestionIndex)){
            // Load multiple
            loadMultipleQuestion();
        }else{
            // Load text
            loadQuestion();
        }
    }

    public void loadMultipleQuestion(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentTakesMultipleChoiceUI.fxml"));
            loader.load();
            StudentTakesMultipleChoice controller = loader.getController();
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
        saveUserAnswer();
        currentQuestionIndex --;
        loadNewQuestion();
    }

    public void onNext(){
        // save user's response
        saveUserAnswer();
        if(currentQuestionIndex == 4){
            // submit test..
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

}
