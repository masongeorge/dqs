package Student;

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
    private TableView activeTableView;

    @FXML
    private TableColumn activeNameColumn;

    @FXML
    private TableColumn activeAssignedColumn;

    @FXML
    private TableColumn activeDueCOlumn;

    @FXML
    private TableColumn activeTypeColumn;

    @FXML
    private TableView completedTableView;

    @FXML
    private TableColumn completedNameColumn;

    @FXML
    private TableColumn completedDueColumn;

    @FXML
    private TableColumn completedTypeColumn;

    @FXML
    private TableColumn completedResultColumn;


    @FXML
    public void initialize(){

    }

    public void onBack(){
        loadStudentHome();
    }

    public void onStartActive(){

    }

    public void onCheckAnswers(){

    }

    public void onRetakeFormative(){

    }

    public void loadStudentHome(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentHomeUI.fxml"));
            loader.load();

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
