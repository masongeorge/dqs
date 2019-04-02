package Login;

import Lecturer.LecturerHomeController;
import Model.Lecturer;
import Model.StudentUser;
import Student.StudentHomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import Helpers.*;

import javafx.scene.input.KeyEvent;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

public class LoginController {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox rememberMe;

    private Alert alert;
    private MySQLHandler SqlHandler;


    @FXML
    public void initialize(){
        try{
            SqlHandler = new MySQLHandler("c1841485", "6Z=q]K~GXKzcjW=d");
            CheckRememberMe();
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
                    loadUserHomeScreen();
                }else {
                    // Login Failed
                    AlertHandler.showErrorAlert("Login failed!", "Unable to login!", "Your username or password is incorrect");
                }
                getRemember();
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
            StudentHomeController controller = loader.getController();
            StudentUser studentUser = new StudentUser(Integer.parseInt(Data[0]), Data[1], emailTextField.getText());
            controller.initData(studentUser);

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

    public void loadLecturerHome(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/Lecturer/LecturerHomeUI.fxml"));
            loader.load();

            String[] Data = SqlHandler.GetUserData(emailTextField.getText());
            LecturerHomeController controller = loader.getController();

            Lecturer lecturer = new Lecturer(Integer.parseInt(Data[0]), Data[1], emailTextField.getText());
            controller.initData(lecturer);

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

    public void loadUserHomeScreen(){
        String[] Data = SqlHandler.GetUserData(emailTextField.getText());
        boolean at = Data[2].equals("1");
        if (at){
            // Load Lecturer Home Screen
            loadLecturerHome();
        }else{
            // Load Student Home Screen
            loadStudentHome();
        }
    }

    public boolean checkValidation(){
        boolean isEmailValid = Validators.isValidEmail(emailTextField.getText());
        boolean isPassValid = Validators.isValidPassword(passwordField.getText());

        return isEmailValid && isPassValid;
    }

    @FXML
    public void onEnter(ActionEvent ae){
        onLogin();
    }

    private void CheckRememberMe() {
        File tempFile = new File("temp/tempdata.txt");
        boolean exists = tempFile.exists();

        if (exists) {
            rememberMe.setSelected(true);
            String fileName = "temp/tempdata.txt";
            String line = null;
            String tempHolder = "";

            try {
                FileReader fileReader =
                        new FileReader(fileName);

                BufferedReader bufferedReader =
                        new BufferedReader(fileReader);

                while((line = bufferedReader.readLine()) != null) {
                    tempHolder += line + "{{>!";
                }

                // Always close files.
                bufferedReader.close();
            }
            catch(FileNotFoundException ex) {
                System.out.println(
                        "Unable to open file '" +
                                fileName + "'");
            }
            catch(IOException ex) {
                System.out.println(
                        "Error reading file '"
                                + fileName + "'");
            }
            String[] parts = tempHolder.split("\\{\\{>!");
            try {
                emailTextField.setText(EncDecTool.decrypt(parts[0]));
                passwordField.setText(EncDecTool.decrypt(parts[1]));
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void getRemember(){
        if (rememberMe.isSelected()) {
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("temp/tempdata.txt"), "utf-8"))) {
                writer.write(EncDecTool.encrypt(emailTextField.getText()));
                ((BufferedWriter) writer).newLine();
                writer.write(EncDecTool.encrypt(passwordField.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Files.deleteIfExists(Paths.get("temp/tempdata.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
