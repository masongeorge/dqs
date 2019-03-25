package Login;

import Model.StudentUser;
import Student.StudentHomeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Model.User;

import Helpers.*;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    private Alert alert;
    private MySQLHandler SqlHandler;


    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
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
                    AlertHandler.showErrorAlert("Login failed!", "Unable to login!", "Your username or password is incorrect");
                }
            }
            catch (Exception e) {
                throw e;
            }
        }else {
            // Validation is incorrect
            AlertHandler.showErrorAlert("Login failed!", "Invalid format!", "Make sure you enter a valid email address and a password with a length greater than zero!");
        }
    }

    public void loadStudentHome(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Student/StudentHomeUI.fxml"));
            loader.load();

            String[] Data = SqlHandler.GetUserData(emailTextField.getText());
            boolean at = true ? Data[1] == "1" : false;

            StudentHomeController controller = loader.getController();

            if (at){
                // Create and load a Lecturer User

            }else{
                // Create and load a Student User

                StudentUser studentUser = new StudentUser(Integer.parseInt(Data[0]), Data[1], emailTextField.getText());
                controller.initData(studentUser);
            }


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
