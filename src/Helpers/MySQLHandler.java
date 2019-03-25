package Helpers;

import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class MySQLHandler {

    private Connection Con;

    public MySQLHandler(String Username, String Password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Con = DriverManager.getConnection("jdbc:mysql://csmysql.cs.cf.ac.uk/c1841485", Username, Password);
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
            String query = String.format("SELECT acc_type FROM dqs_users WHERE username='%s'", Username);
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
            String query ="INSERT INTO dqs_users (name, username, password, acc_type)"
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
            String query = "DELETE FROM dqs_users WHERE id = ?";

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
            String query = String.format("SELECT COUNT(*) AS N FROM dqs_users WHERE username='%s' and password='%s'", Username, Password);
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
            String query = String.format("SELECT password FROM dqs_users WHERE username='%s'", Username);
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
            String query = String.format("SELECT id FROM dqs_users WHERE username='%s'", Username);
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
            String query = String.format("SELECT student_module_id FROM dqs_modules WHERE student_id='%d'", Id);
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
            String query = String.format("SELECT id, name, acc_type FROM dqs_users WHERE username='%s'", Username);
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
            String query = String.format("SELECT module_name FROM dqs_modulesdata WHERE module_id='%d'", id);
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
            String query = String.format("SELECT name from dqs_users WHERE id = (SELECT lecturer_id FROM dqs_modulesdata WHERE module_id='%d')", id);
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
            String query = String.format("SELECT moduleID, title, assignedDate, dueDate, type FROM dqs_assessments WHERE assessment_id=%d", assessmentId);
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
            String query = String.format("SELECT assessmentID from dqs_studentsassessments WHERE studentID = %d", studentId);
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
            String query = String.format("SELECT assessmentID from dqs_studentsassessments WHERE studentID = %d AND completed = '1'", studentId);
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
            String query = String.format("SELECT result from dqs_studentsassessments WHERE assessmentID = %d", AssessmentId);
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

    public String GetIdByAssessmentName(String AssessmentName) {
        String result = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT assessment_id from dqs_assessments WHERE title = '%s'", AssessmentName);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                result = rs.getString("assessment_id");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public List<String> GetAssessmentIndexes(int AssessmentId) {
        String result = "";
        List<String> indexes = new ArrayList<String>();
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT indexes from dqs_assessments WHERE assessment_id = %d", AssessmentId);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                result = rs.getString("indexes");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String[] parts = result.split(";");
        for (String part : parts) {
            indexes.add(part);
        }
        return indexes;
    }

    // Get all multiple choice questions and answers
    // Key: question%D
    // Key: question%D_q1
    // Key: question%D_q2
    // Key: question%D_q3
    // Key: question%D_c
    // WHERE: D - question index
    // _c -> correct answer
    public Map<String ,String> GetMultipleChoiceQ(int AssessmentId) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> indexes = GetAssessmentIndexes(AssessmentId);

        for (String index : indexes) {
            try {
                Statement stmt = Con.createStatement();
                String query = String.format("SELECT qora, content FROM dqs_qanda WHERE " +
                        "((SELECT content FROM dqs_qanda WHERE qora = 'question%s_t') = 'm') AND (qora='question%s' OR qora = 'question%s_q1' " +
                        "OR qora = 'question%s_q2' OR qora = 'question%s_q3' or qora = 'question%s_c')", index, index, index, index, index, index);
                //System.out.println(query);
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    map.put(rs.getString("qora"), rs.getString("content"));
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return map;
    }

    // Get all regular questions and answers
    // Key: question%D
    // Key: question%D_c
    // WHERE: D - question index
    // _c -> correct answer
    public Map<String ,String> GerRegularQ(int AssessmentId) {
        Map<String, String> map = new HashMap<String, String>();
        List<String> indexes = GetAssessmentIndexes(AssessmentId);

        for (String index : indexes) {
            try {
                Statement stmt = Con.createStatement();
                String query = String.format("SELECT qora, content FROM dqs_qanda WHERE " +
                        "((SELECT content FROM dqs_qanda WHERE qora = 'question%s_t') = 'n') AND " +
                        "(qora='question%s' OR qora = 'question%s_c')", index, index, index);
                //System.out.println(query);
                ResultSet rs = stmt.executeQuery(query);
                while(rs.next()) {
                    map.put(rs.getString("qora"), rs.getString("content"));
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return map;
    }
}