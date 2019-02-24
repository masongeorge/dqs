package Login;

import Model.StudentModule;
import Model.User;
import Student.StudentHomeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import Helpers.*;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    private MySQLHandler SqlHandler;


    @FXML
    public void initialize(){
        try {
            SqlHandler = new MySQLHandler("sql2279737", "fE6!aZ7*");
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void onLogin(){
        if (checkValidation()){
            try {
                // Check if account exists
                if (SqlHandler.CheckAccountExists(emailTextField.getText(), passwordField.getText())) {
                    // Login Success
                    loadStudentHome();
                }else {
                    // Login Failed
                    AlertHandler.showErrorAlert(3,"Login failed!", "Unable to login!", "Your username or password is incorrect");
                }
            }
            catch (Exception e) {
                throw e;
            }
        }else {
            // Validation is incorrect
            AlertHandler.showErrorAlert(2,"Login failed!", "Invalid format!", "Make sure you enter a valid email address and a password with a length greater than zero!");
        }
    }

    public void loadStudentHome(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentHomeUI.fxml"));
            loader.load();

            StudentHomeController studentHomeController = loader.getController();
            studentHomeController.initData(emailTextField.getText());

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

    public boolean checkValidation(){
        boolean isEmailValid = Validators.isValidEmail(emailTextField.getText());
        boolean isPassValid = Validators.isValidPassword(passwordField.getText());

        return isEmailValid && isPassValid;
    }

    public void onSignUp(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("SignUpUI.fxml"));
            loader.load();

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(parent, 515, 486));
            stage.setResizable(false);
            stage.show();

            closeScreen();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public void closeScreen(){
        SqlHandler.Close();
        Stage oldStage = (Stage)emailTextField.getScene().getWindow();
        oldStage.close();
    }

}
