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

    private EmailHandler EmailSender;

    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void onStudent(){
        lecturerRadio.setSelected(false);
    }

    public void onLecturer(){
        studentRadio.setSelected(false);
    }

    public void onSignUp(){
        if (checkValidation()){
            int type = lecturerRadio.isSelected() ? 1 : 0;

            try {
                if (SqlHandler.GetUserPassword(emailTextField.getText()).isEmpty()) {
                    if (SqlHandler.RegisterAccount(nameTextField.getText(), emailTextField.getText(),
                            passwordField.getText(), Integer.toString(type))) {
                        // Sign up Successful
                        AlertHandler.showShortMessage("Success","Account created successfully!");
                        // Send Email
                        EmailSender = new EmailHandler();
                        EmailSender.SendRegisterInfo(nameTextField.getText(), emailTextField.getText(), passwordField.getText());
                        // Load new scene
                        loadLogin();
                    }
                } else {
                    // Sign up failed, account exists
                    AlertHandler.showErrorAlert("Sign up failed", "Account already exists!", "Please choose another email address");
                }
            } catch (Exception e) {
                throw e;
            }
        } else {
            AlertHandler.showErrorAlert("Sign up failed", "Invalid format!", "Make sure you enter your name, valid email address and a password with a length greater than zero!");
        }

    }

    public boolean checkValidation(){
        boolean isNameValid = Validators.isValidName(nameTextField.getText());
        boolean isEmailValid = Validators.isValidEmail(emailTextField.getText());
        boolean isPassValid = Validators.isValidPassword(passwordField.getText());

        return isNameValid && isEmailValid && isPassValid;
    }

    public void onLogin(){
        loadLogin();
    }

    public void loadLogin(){
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
