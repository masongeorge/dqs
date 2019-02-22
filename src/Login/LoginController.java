package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    public void onLogin(){
        if (checkValidation()){
            // Formatting is correct
            // Check database for credentials...

        }else{
            Validators.validationErrorAlert(true);
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

            Stage oldStage = (Stage)emailTextField.getScene().getWindow();
            oldStage.close();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }


}
