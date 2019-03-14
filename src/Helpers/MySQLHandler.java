package Helpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

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
            String query ="INSERT INTO Users (name, username, password, acc_type)"
                    + " VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setString (1, AccName);
            preparedStmt.setString (2, Username);
            preparedStmt.setString (3, Password);
            preparedStmt.setString   (4, AccountType);

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

    public List<Integer> GetUserModules(int Id) {
        List<Integer> Modules = new ArrayList<>();
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT student_module_id FROM Modules WHERE student_id='%d'", Id);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                Modules.add(rs.getInt("student_module_id"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Modules;
    }

    public String[] GetUserData(String Username)
    {
        String id = "";
        String name = "";
        String type = "";

        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT id, name, acc_type FROM Users WHERE username='%s'", Username);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                id = String.valueOf(rs.getInt("id"));
                name = rs.getString("name");
                type = rs.getString("acc_type");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new String[] {id, name, type};
    }

    public String GetModuleNameById(int id) {
        String module = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT module_name FROM ModulesData WHERE module_id='%d'", id);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                module = rs.getString("module_name");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return module;
    }

    public String GetLecturerByModuleId(int id) {
        String lecturer = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT name from Users WHERE id = (SELECT lecturer_id FROM ModulesData WHERE module_id='%d')", id);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                lecturer = rs.getString("name");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return lecturer;
    }

    public String[] GetAssessmentData(int assessmentId) {
        String moduleID = "";
        String title = "";
        String assignedDate = "";
        String dueDate = "";
        String atype = "";

        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT moduleID, title, assignedDate, dueDate, type FROM Assessments WHERE assessment_id=%d", assessmentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                moduleID = rs.getString("moduleID");
                title = rs.getString("title");
                assignedDate = String.valueOf(rs.getDate("assignedDate"));
                dueDate = String.valueOf(rs.getDate("dueDate"));
                atype = String.valueOf(rs.getInt("type"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new String[] {moduleID, title, assignedDate, dueDate, atype};
    }


    public List<Integer> GetStudentAssessments(int studentId) {
        List<Integer> list = new ArrayList<>();
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT assessmentID from StudentsAssessments WHERE studentID = %d", studentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                list.add(rs.getInt("assessmentID"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public List<Integer> GetCompletedAssessments(int studentId) {
        List<Integer> list = new ArrayList<>();
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT assessmentID from StudentsAssessments WHERE studentID = %d AND completed = '1'", studentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                list.add(rs.getInt("assessmentID"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    public String GetAssessmentResult(int AssessmentId) {
        String result = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT result from StudentsAssessments WHERE assessmentID = %d", AssessmentId);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                result = rs.getString("result");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
