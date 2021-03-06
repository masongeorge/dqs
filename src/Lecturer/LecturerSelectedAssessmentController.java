package Lecturer;

import Helpers.MySQLHandler;
import Model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class LecturerSelectedAssessmentController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label assignedLabel;

    @FXML
    private Label dueLabel;

    private LecturerModule selectedModule;

    private Lecturer lecturer;

    private MySQLHandler SqlHandler;

    private Assessment selectedAssessment;

    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(Lecturer lecturer, LecturerModule selectedModule, Assessment selectedAssessment){
        this.lecturer = lecturer;
        this.selectedModule = selectedModule;
        this.selectedAssessment = selectedAssessment;

        loadAssessment();
    }

    public void loadAssessment(){
        titleLabel.setText(selectedAssessment.getName());
        assignedLabel.setText(selectedAssessment.getAssignedDateProperty().get());
        dueLabel.setText(selectedAssessment.getDueDateProperty().get());
    }

    public void onView(){
        if(selectedAssessment.getType() == 0){
            loadStatsForFormative();
        }else{
            loadStatsForSummative();
        }
    }

    public void loadStatsForFormative(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerViewsStatsFormativeUI.fxml"));
            loader.load();
            LecturerViewsStatsFormativeController controller = loader.getController();
            controller.initData(lecturer, selectedModule, selectedAssessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Statistics");
            stage.setScene(new Scene(parent, 504, 250));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void loadStatsForSummative(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerViewsStatsSummativeUI.fxml"));
            loader.load();

            LecturerViewsStatsSummative controller = loader.getController();
            controller.initData(lecturer, selectedModule, selectedAssessment);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Statistics");
            stage.setScene(new Scene(parent, 600, 331));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void onBack(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerAssessmentsUI.fxml"));
            loader.load();
            LecturerAssessmentsController controller = loader.getController();
            controller.initData(lecturer, selectedModule);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Create Assessment");
            stage.setScene(new Scene(parent, 600, 312));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void onEdit(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerCreatesAssessmentUI.fxml"));
            loader.load();
            LecturerCreatesAssessmentController controller = loader.getController();

            int id = Integer.parseInt(SqlHandler.GetIdByAssessmentName(selectedAssessment.getName()));
            List<String> indexes1 = SqlHandler.GetAssessmentIndexes(id);

            Question questions[] = new Question[5];
            int counter = 0;
            for (String in : indexes1) {
                String[] dt = SqlHandler.GetQAndA(Integer.parseInt(in));
                if (dt[0].equals("m")) {
                    questions[counter] = new MultipleQuestion(dt[1], dt[0], dt[2], dt[3], dt[4], dt[5]);
                } else {
                    questions[counter] = new TextQuestion(dt[1], dt[0], dt[5]);
                }
                counter++;
            }

            controller.initData(lecturer, selectedModule, questions, selectedAssessment, false, id);

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Edit Assessment");
            stage.setScene(new Scene(parent, 786, 312));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)titleLabel.getScene().getWindow();
        oldStage.close();
    }
}
