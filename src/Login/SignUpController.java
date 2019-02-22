package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.*;

public class SignUpController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private RadioButton studentRadio;

    @FXML
    private RadioButton lecturerRadio;

    public void onStudent(){
        lecturerRadio.setSelected(false);
    }

    public void onLecturer(){
        studentRadio.setSelected(false);
    }

    public void onSignUp(){
        if (checkValidation()){
            // Formatting is correct
            // Check if user doesn't exist yet in the database...
        }else{
            Validators.validationErrorAlert(false);
        }
    }

    public boolean checkValidation(){
        boolean isNameValid = Validators.isValidName(nameTextField.getText());
        boolean isEmailValid = Validators.isValidEmail(emailTextField.getText());
        boolean isPassValid = Validators.isValidPassword(passwordField.getText());

        return isNameValid && isEmailValid && isPassValid;
    }

    public void onLogin(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("LoginUI.fxml"));
            loader.load();

            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(parent, 492, 388));
            stage.setResizable(false);
            stage.show();

            Stage oldStage = (Stage)emailTextField.getScene().getWindow();
            oldStage.close();
        }catch (Exception e){
            System.out.println("Error: " + e);
        }
    }


}
