package Student;

import Helpers.AlertHandler;
import Helpers.MySQLHandler;
import Model.StudentModule;
import Model.User;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

    private Model.User user;

    private MySQLHandler SqlHandler;

    @FXML
    public void initialize(){
        setupTableView();

        try {
            SqlHandler = new MySQLHandler("sql2279737", "fE6!aZ7*");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void initData(String Email) {
        String[] UserDetails = SqlHandler.GetUserData(Email);

        Boolean IsStudent = false;
        if (UserDetails[2] == "0")
            IsStudent = true;

        user = new Model.User(SqlHandler.GetIdByName(UserDetails[0]),
                UserDetails[0],
                IsStudent,
                UserDetails[1],
                UserDetails[3]);
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

        String UserModule = user.getModules();
        if (UserModule != "0") {
            try {
                String[] UserModules = UserModule.split(";");
                for (String s : UserModules) {
                    int ModuleId = Integer.parseInt(s);
                    String[] ModuleData = SqlHandler.GetUserModules(ModuleId);
                    modules.add(new StudentModule(ModuleId,
                            ModuleData[0],
                            SqlHandler.GetNameById(Integer.parseInt(ModuleData[1]))));
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                AlertHandler.showErrorAlert(3, "Error", "Error found!", e.toString());
            }
        }
        else {
            // no modules
        }

        moduleTableView.setItems(modules);
        moduleTableView.refresh();
    }

    public void onOpenModule(){
        if (selectedModule == null){
            AlertHandler.showErrorAlert(2,"Error", "Select a module first!", "Select a module from the list first and then click on open module");
        }else{
            System.out.println("Selected: " + selectedModule.getModuleNameProperty().get() + ", " + selectedModule.getLecturerNameProperty().get());
        }
    }

    public void onSignOut(){
        SqlHandler.Close();
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
