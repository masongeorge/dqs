package Student;

import Helpers.AlertHandler;
import Model.StudentModule;
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

public class StudentHomeController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<StudentModule> moduleTableView;

    @FXML
    private TableColumn<StudentModule, String> moduleColumn;

    @FXML
    private TableColumn<StudentModule, String> lecturerColumn;

    private ObservableList<StudentModule> modules;

    private StudentModule selectedModule;

    @FXML
    public void initialize(){
        setupTableView();
        loadModules();
        loadUser();
    }

    public void loadUser(){
        welcomeLabel.setText("Welcome " + "Marton");
    }

    public void setupTableView(){
        moduleColumn.setCellValueFactory(cellData -> cellData.getValue().getModuleNameProperty());
        lecturerColumn.setCellValueFactory(cellData -> cellData.getValue().getLecturerNameProperty());

        moduleColumn.setStyle("-fx-alignment: CENTER");
        lecturerColumn.setStyle("-fx-alignment: CENTER");

        moduleTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentModule>() {
            @Override
            public void changed(ObservableValue<? extends StudentModule> observable, StudentModule oldValue, StudentModule newValue) {
                selectedModule = newValue;
            }
        });
    }

    public void loadModules(){
        modules = FXCollections.observableArrayList();
        modules.add(new StudentModule("Maths", "Maths teacher"));
        modules.add(new StudentModule("Physics", "Physics teacher"));
        modules.add(new StudentModule("Business", "Business teacher"));

        moduleTableView.setItems(modules);
        moduleTableView.refresh();
    }

    public void onOpenModule(){
        if (selectedModule == null){
            AlertHandler.showErrorAlert("Error", "Select a module first!", "Select a module from the list first and then click on open module");
        }else{
            System.out.println("Selected: " + selectedModule.getModuleNameProperty().get() + ", " + selectedModule.getLecturerNameProperty().get());
        }
    }

    public void onSignOut(){
        loadLogin();
    }

    public void loadLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Login/LoginUI.fxml"));
            loader.load();

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(parent, 492, 388));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        Stage oldStage = (Stage)welcomeLabel.getScene().getWindow();
        oldStage.close();
    }
}
