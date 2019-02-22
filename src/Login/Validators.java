package Login;

import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {


    public static boolean isValidEmail(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidPassword(String password){
        return password.length() >= 1;
    }

    public static boolean isValidName(String name){
        return name.length() >= 1;
    }

    public static void validationErrorAlert(boolean isLogin){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Invalid format!");
        if (isLogin){
            alert.setTitle("Login failed!");
            alert.setContentText("Make sure you enter a valid email address and a password with a length greater than zero!");
        }else{
            alert.setTitle("Sign up failed!");
            alert.setContentText("Make sure you enter your name, valid email address and a password with a length greater than zero!");
        }

        alert.showAndWait();
    }

}
