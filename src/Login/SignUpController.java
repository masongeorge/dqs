package Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.*;

//
import Helpers.*;

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

    private MySQLHandler SqlHandler;
    private Alert alert;

    public void onStudent(){
        lecturerRadio.setSelected(false);
    }

    public void onLecturer(){
        studentRadio.setSelected(false);
    }

    public void onSignUp(){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign Up");

        if (checkValidation()){
            int type = lecturerRadio.isSelected() ? 1 : 0;

            try {
                SqlHandler = new MySQLHandler("sql2279737", "fE6!aZ7*");
                if (SqlHandler.GetUserPassword(emailTextField.getText()).isEmpty()) {
                    if (SqlHandler.RegisterAccount(nameTextField.getText(), emailTextField.getText(),
                            passwordField.getText(), Integer.toString(type))) {
                        alert.setHeaderText("Account registered successfully!");
                        alert.setContentText("Your account has been successfully registered!");
                        // todo: possible send user info to the specified email
                    }
                } else {
                    alert.setHeaderText("Account register failed");
                    alert.setContentText("The specified account already exists.");
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            Validators.validationErrorAlert(false);
        }
        alert.showAndWait();
        SqlHandler.Close();
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
