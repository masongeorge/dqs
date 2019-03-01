package Student;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
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

import java.util.List;

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

    private Model.User user;

    private MySQLHandler SqlHandler;

    @FXML
    public void initialize(){
        setupTableView();
        try{
            SqlHandler = new MySQLHandler("sql2279737", "fE6!aZ7*");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(int userID, String name, boolean isStudent, String email) {
        user = new Model.User(userID, name, isStudent, email);
        loadUser();
        loadModules();
    }

    public void loadUser(){
        welcomeLabel.setText("Welcome " + user.getName());
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

        List<Integer> StudentModules = SqlHandler.GetUserModules(user.getId());

        for (int i : StudentModules) {
            modules.add(new StudentModule(SqlHandler.GetModuleNameById(i), SqlHandler.GetLecturerByModuleId(i)));
        }

        moduleTableView.setItems(modules);
        moduleTableView.refresh();
    }

    public void onOpenModule(){
        if (selectedModule == null){
            AlertHandler.showErrorAlert("Error", "Select a module first!", "Select a module from the list first and then click on open module");
        }else{
            loadModuleScreen();
        }
    }

    public void onSignOut(){
        SqlHandler.Close();
        loadLogin();
    }

    public void loadModuleScreen(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentSelectedModuleUI.fxml"));
            loader.load();
            StudentSelectedModuleController controller = loader.getController();
            controller.initData();

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Maths");
            stage.setScene(new Scene(parent, 734, 643));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
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
