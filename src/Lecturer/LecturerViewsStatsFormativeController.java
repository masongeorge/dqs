package Lecturer;

import Helpers.MySQLHandler;
import Model.Assessment;
import Model.Lecturer;
import Model.LecturerModule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class LecturerViewsStatsFormativeController {

    @FXML
    private Label titleLabel;

    @FXML
    private Label averageLabel;

    @FXML
    private Label lowestLabel;

    @FXML
    private Label highestLabel;

    @FXML
    private Label perfectLabel;

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

        loadData();
    }

    public void loadData(){
        titleLabel.setText("Statistics for " + selectedAssessment.getName());
        averageLabel.setText("20%");
        lowestLabel.setText("30%");
        highestLabel.setText("40%");
        perfectLabel.setText("12");
    }

    public void onBack(){
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
        Stage oldStage = (Stage)titleLabel.getScene().getWindow();
        oldStage.close();
    }

}
