package Lecturer;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Optional;

public class LecturerCreatesAssessmentController {

    @FXML
    private TextField titleField;

    @FXML
    private DatePicker assignedDate;

    @FXML
    private DatePicker dueDate;

    @FXML
    private RadioButton summativeRadio;

    @FXML
    private RadioButton formativeRadio;

    @FXML
    private Button addQ1Button;

    @FXML
    private Button addQ2Button;

    @FXML
    private Button addQ3Button;

    @FXML
    private Button addQ4Button;

    @FXML
    private Button addQ5Button;

    @FXML
    private ComboBox<Integer> assignedHourCombo;

    @FXML
    private ComboBox<Integer> assignedMinuteCombo;

    @FXML
    private ComboBox<Integer> dueHourCombo;

    @FXML
    private ComboBox<Integer> dueMinuteCombo;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private Question[] questions;

    private Assessment assessment;

    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }

        setupCombos();
    }

    public void setupCombos(){
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<Integer> minutes = FXCollections.observableArrayList();

        for (int i = 0; i <= 23; i++) {
            hours.add(i);
        }
        for (int i = 0; i <= 59 ; i++) {
            minutes.add(i);
        }

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = rightNow.get(Calendar.MINUTE);

        assignedHourCombo.setItems(hours);
        assignedHourCombo.getSelectionModel().select(currentHour);

        assignedMinuteCombo.setItems(minutes);
        assignedMinuteCombo.getSelectionModel().select(currentMinute);

        dueHourCombo.setItems(hours);
        dueHourCombo.getSelectionModel().select(currentHour);

        dueMinuteCombo.setItems(minutes);
        dueMinuteCombo.getSelectionModel().select(currentMinute);

        assignedDate.setValue(LocalDate.now());
    }

    public void initData(Lecturer lecturer, LecturerModule selectedModule, Question questions[], Assessment assessment){
        this.lecturer = lecturer;
        this.selectedModule = selectedModule;
        this.questions = questions;
        this.assessment = assessment;
        setupQuestionButtons();

        loadAssessmentData();
    }

    public void loadAssessmentData(){
        if(assessment.getNameProperty() != null){
            titleField.setText(assessment.getName());
        }

        if(assessment.getAssignedDateProperty() != null){
            String dateString = assessment.getAssignedDateProperty().get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            assignedDate.setValue(localDate);

            LocalTime localTime = LocalTime.parse(dateString, formatter);
            int hour = localTime.getHour();
            int minute = localTime.getMinute();
            assignedHourCombo.getSelectionModel().select(hour);
            assignedMinuteCombo.getSelectionModel().select(minute);
        }

        if(assessment.getDueDateProperty() != null){
            String dateString = assessment.getDueDateProperty().get();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            dueDate.setValue(localDate);

            LocalTime localTime = LocalTime.parse(dateString, formatter);
            int hour = localTime.getHour();
            int minute = localTime.getMinute();
            dueHourCombo.getSelectionModel().select(hour);
            dueMinuteCombo.getSelectionModel().select(minute);
        }

        if(assessment.getType() == 0){
            onFormative();
        }else{
            onSummative();
        }


    }

    public void saveAssessmentInfo(){
        if(titleField.getText().length() > 0){
            assessment.setName(titleField.getText());
        }

        if(assignedDate.getValue() != null){
            String dateStr = assignedDate.getValue().toString();
            String hourStr = String.valueOf(assignedHourCombo.getSelectionModel().getSelectedItem());
            String minuteStr = String.valueOf(assignedMinuteCombo.getSelectionModel().getSelectedIndex());
            if(hourStr.length() == 1){
                hourStr = "0" + hourStr;
            }
            if(minuteStr.length() == 1){
                minuteStr = "0" + minuteStr;
            }
            String fullDateStr = dateStr + " " + hourStr + ":" + minuteStr + ":00";
            assessment.setAssignedDate(fullDateStr);
        }

        if(dueDate.getValue() != null){
            String dateStr = dueDate.getValue().toString();
            String hourStr = String.valueOf(dueHourCombo.getSelectionModel().getSelectedItem());
            String minuteStr = String.valueOf(dueMinuteCombo.getSelectionModel().getSelectedIndex());
            if(hourStr.length() == 1){
                hourStr = "0" + hourStr;
            }
            if(minuteStr.length() == 1){
                minuteStr = "0" + minuteStr;
            }
            String fullDateStr = dateStr + " " + hourStr + ":" + minuteStr + ":00";
            assessment.setDueDate(fullDateStr);
        }

        if(summativeRadio.isSelected()){
            assessment.setType(1);
        }

        if(formativeRadio.isSelected()){
            assessment.setType(0);
        }

    }

    public void setupQuestionButtons(){
        if(questions[0] == null){
            addQ1Button.setText("Add Question #1");
        }else{
            addQ1Button.setText("Edit Question #1");
        }

        if(questions[1] == null){
            addQ2Button.setText("Add Question #2");
        }else{
            addQ2Button.setText("Edit Question #3");
        }

        if(questions[2] == null){
            addQ3Button.setText("Add Question #3");
        }else{
            addQ3Button.setText("Edit Question #3");
        }

        if(questions[3] == null){
            addQ4Button.setText("Add Question #4");
        }else{
            addQ4Button.setText("Edit Question #4");
        }

        if(questions[4] == null){
            addQ5Button.setText("Add Question #5");
        }else{
            addQ5Button.setText("Edit Question #5");
        }
    }

    public void onBack(){
        loadLecturerSelectedModule();
    }

    public void loadLecturerSelectedModule(){
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

    public void onSave(){
        saveAssessmentInfo();
        System.out.println(assignedHourCombo.getSelectionModel().getSelectedItem());

        if (titleField.getText().length() > 0 && assignedDate.getValue() != null && dueDate.getValue() != null && assignedHourCombo.getSelectionModel().getSelectedItem() != null && assignedMinuteCombo.getSelectionModel().getSelectedItem() != null && dueHourCombo.getSelectionModel().getSelectedItem() != null && dueMinuteCombo.getSelectionModel().getSelectedItem() != null &&(summativeRadio.isSelected() || formativeRadio.isSelected()) && questions[0] != null && questions[1] != null && questions[2] != null && questions[3] != null && questions[4] != null) {
            // Can you please check if assigned dates and due dates are valid?

            System.out.println("Title: " + titleField.getText());
            System.out.println("Assigned Date: " + assignedDate.getValue().toString());
            System.out.println("Due Date: " + dueDate.getValue().toString());
            String type = summativeRadio.isSelected() ? "Summative" : "Formative";
            System.out.println("Type: " + type);

            // Questions are stored in the questions array
            String query = "";
            int newIndex = SqlHandler.GetNewIndex();
            newIndex++;
            for (Question question : questions) {
                if (question.getType().equals("m")) {
                    MultipleQuestion mQuestion = (MultipleQuestion) question;
                    // DB VALUES
                    String newQuestion = "question" + String.valueOf(newIndex);
                    String newQuestionIndex1 = "question" + String.valueOf(newIndex) + "_q1";
                    String newQuestionIndex2 = "question" + String.valueOf(newIndex) + "_q2";
                    String newQuestionIndex3 = "question" + String.valueOf(newIndex) + "_q3";
                    String newQuestionCorrect = "question" + String.valueOf(newIndex) + "_c";
                    String newQuestionType = "question" + String.valueOf(newIndex) + "_t";

                    // APPENDING TO QUERY
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestion, mQuestion.getTitle());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionIndex1, mQuestion.getAnswer1());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionIndex2, mQuestion.getAnswer2());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionIndex3, mQuestion.getAnswer3());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionCorrect, mQuestion.getCorrectAnswer());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionType, "m");

                    // INCREASING THE COUNTER
                    newIndex++;
                    AlertHandler.showShortMessage("Multiple question", String.format("Question title: %s. Answer 1: %s, Answer 2: %s, Answer 3: %s, Correct %s",
                            mQuestion.getTitle(), mQuestion.getAnswer1(), mQuestion.getAnswer2(), mQuestion.getAnswer3(), mQuestion.getCorrectAnswer()));
                } else {
                    TextQuestion tQuestion = (TextQuestion) question;
                    // DB VALUES
                    String newQuestion = "question" + String.valueOf(newIndex);
                    String newQuestionCorrect = "question" + String.valueOf(newIndex) + "_c";
                    String newQuestionType = "question" + String.valueOf(newIndex) + "_t";

                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestion, tQuestion.getTitle());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionCorrect, tQuestion.getCorrectAnswer());
                    query += String.format("INSERT INTO dqs_qanda (qora, content) VALUES ('%s', '%s');", newQuestionType, "t");

                    newIndex++;
                    AlertHandler.showShortMessage("Regular question", String.format("Question title: %s. Correct %s",
                            tQuestion.getTitle(), tQuestion.getCorrectAnswer()));
                }
            }

            // IF EVERYTHING IS SAVED, LOAD lecturer selected Module SCREEN AGAIN:
            loadLecturerSelectedModule();
        }else{
            AlertHandler.showShortMessage("Error", "Please fill out every information to create an assessment");
        }
    }

    public void onSummative(){
        summativeRadio.setSelected(true);
        formativeRadio.setSelected(false);
    }

    public void onFormative(){
        summativeRadio.setSelected(false);
        formativeRadio.setSelected(true);
    }

    public void showQuestionDialog(int questionIndex){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getDialogPane().setPrefWidth(500);
        alert.setTitle("Creating a new question");
        alert.setHeaderText("What type of question would you like to add?");
        alert.setContentText("Choose your option.");

        ButtonType buttonTypeOne = new ButtonType("Multiple Choice");
        ButtonType buttonTypeTwo = new ButtonType("Regular");

        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            // Create Multiple Choice Question
            loadMultipleQuestion(questionIndex);
        } else if (result.get() == buttonTypeTwo) {
            // Create Regular Question
            loadRegularQuestion(questionIndex);
        }
    }

    public void loadMultipleQuestion(int questionIndex){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerAddsMultipleUI.fxml"));
            loader.load();
            LecturerAddsMultipleController controller = loader.getController();
            controller.initData(lecturer, selectedModule, questions, questionIndex, assessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Multiple Choice Question");
            stage.setScene(new Scene(parent, 498, 313));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void loadRegularQuestion(int questionIndex){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerAddsTextUI.fxml"));
            loader.load();
            LecturerAddsTextController controller = loader.getController();
            controller.initData(lecturer, selectedModule, questions, questionIndex, assessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Regular Question");
            stage.setScene(new Scene(parent, 581, 136));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }


    public void onAddQ1(){
        saveAssessmentInfo();
        if(questions[0] == null){
            showQuestionDialog(0);
        }else{
            if(questions[0].getType() == "m"){
                loadMultipleQuestion(0);
            }else{
                loadRegularQuestion(0);
            }
        }
    }

    public void onAddQ2(){
        saveAssessmentInfo();
        if(questions[1] == null){
            showQuestionDialog(1);
        }else{
            if(questions[1].getType() == "m"){
                loadMultipleQuestion(1);
            }else{
                loadRegularQuestion(1);
            }
        }
    }

    public void onAddQ3(){
        saveAssessmentInfo();
        if(questions[2] == null){
            showQuestionDialog(2);
        }else{
            if(questions[2].getType() == "m"){
                loadMultipleQuestion(2);
            }else{
                loadRegularQuestion(2);
            }
        }
    }

    public void onAddQ4(){
        saveAssessmentInfo();
        if(questions[3] == null){
            showQuestionDialog(3);
        }else{
            if(questions[3].getType() == "m"){
                loadMultipleQuestion(3);
            }else{
                loadRegularQuestion(3);
            }
        }
    }

    public void onAddQ5(){
        saveAssessmentInfo();
        if(questions[4] == null){
            showQuestionDialog(4);
        }else{
            if(questions[4].getType() == "m"){
                loadMultipleQuestion(4);
            }else{
                loadRegularQuestion(4);
            }
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)titleField.getScene().getWindow();
        oldStage.close();
    }


}
