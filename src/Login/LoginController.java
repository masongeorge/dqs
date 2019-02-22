package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import Helpers.*;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    //
    private Alert alert;
    private MySQLHandler SqlHandler;

    public void onLogin(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Authenticating");
        // Validate text fields
        if (checkValidation()){
            try {
                // Initialize new instance of SqlHandler
                SqlHandler = new MySQLHandler("sql2279737", "fE6!aZ7*");
                // Check if account exists
                if (SqlHandler.CheckAccountExists(emailTextField.getText(), passwordField.getText())) {
                    alert.setHeaderText("Login success!");
                    alert.setContentText(String.format("You have successfully logged in as %s", emailTextField.getText()));
                } else {
                    alert.setHeaderText("Login failed!");
                    alert.setContentText("Unable to login. Either username or password is incorrect.");
                }
            }
            catch (Exception e) {
                throw e;
            }
        } else {
            Validators.validationErrorAlert(true);
        }
        alert.showAndWait();
        SqlHandler.Close();
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

            Stage oldStage = (Stage)emailTextField.getScene().getWindow();
            oldStage.close();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }


}
