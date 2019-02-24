package Helpers;

import Model.StudentModule;
import Model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

// Ideas: password encryption, ...

public class MySQLHandler {

    private Connection Con;

    public MySQLHandler(String Username, String Password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Con = DriverManager.getConnection("jdbc:mysql://sql2.freesqldatabase.com/sql2279737", Username, Password);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void Close() {
        if (Con != null) {
            try {
                Con.close();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public int GetAccountType(String Username) {
        int account = 1;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT acc_type FROM Users WHERE username='%s'", Username);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                if (rs.getString("acc_type").contains("0")) {
                    account = 1;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return account;
    }

    public Boolean RegisterAccount(String AccName, String Username, String Password, String AccountType) {
        Boolean ret = false;
        try {
            String query ="INSERT INTO Users (name, username, password, acc_type, modules)"
                    + " VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setString (1, AccName);
            preparedStmt.setString (2, Username);
            preparedStmt.setString (3, Password);
            preparedStmt.setString (4, AccountType);
            preparedStmt.setString (5, "0");

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public Boolean DeleteAccount(int Id) {
        Boolean ret = false;
        try {
            String query = "DELETE FROM Users WHERE id = ?";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt(1, Id);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public Boolean CheckAccountExists(String Username, String Password) {
        Boolean ret = false;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT COUNT(*) AS N FROM Users WHERE username='%s' and password='%s'", Username, Password);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                if (rs.getInt("N") > 0) {
                    ret = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public String GetUserPassword(String Username) {
        String pass = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT password FROM Users WHERE username='%s'", Username);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                pass = rs.getString("password");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return pass;
    }

    public int GetIdByName(String Username) {
        int id = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT id FROM Users WHERE username='%s'", Username);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                id = rs.getInt("id");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            id = -1;
        }
        return id;
    }

    public String[] GetUserData(String Username) {
        String name = "";
        String email = Username;
        String type = "";
        String modules = "";

        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT name, acc_type, modules FROM Users WHERE username='%s'", Username);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                name = rs.getString("name");
                type = rs.getString("acc_type");
                modules = rs.getString("modules");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new String[] { name, email, type, modules };
    }

    public String[] GetUserModules(int id) {
        String ModuleName = "";
        String LecturerName = "";

        try {
            Statement stmt = Con.createStatement();
            String query = "SELECT name, lecturer FROM Modules WHERE id=" + id;
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                ModuleName = rs.getString("name");
                LecturerName = rs.getString("lecturer");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new String[] { ModuleName, LecturerName };
    }

    public String GetNameById(int id) {
        String res = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT name FROM Users WHERE id=%d", id);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                res = rs.getString("name");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
}
