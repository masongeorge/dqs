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

    public int GetAssessmentByTitle(String Title) {
        int id = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT assessment_id FROM dqs_assessments WHERE title='%s'", Title);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                id = rs.getInt("assessment_id");
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
        String atype = "";
        Timestamp timestamp = null;
        Timestamp timestamp1 = null;

        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT moduleID, title, assignedDate, dueDate, type FROM dqs_assessments WHERE assessment_id=%d", assessmentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                moduleID = rs.getString("moduleID");
                title = rs.getString("title");
                timestamp = rs.getTimestamp("assignedDate");
                timestamp1 = rs.getTimestamp("dueDate");
                atype = String.valueOf(rs.getInt("type"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String assignedDate = String.valueOf(timestamp);
        String dueDate = String.valueOf(timestamp1);

        return new String[] {moduleID, title, assignedDate, dueDate, atype};
    }


    public List<Integer> GetModuleAssessments(int moduleID) {
        List<Integer> list = new ArrayList<>();
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT assessment_id from dqs_assessments WHERE moduleID = %d", moduleID);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                list.add(rs.getInt("assessment_id"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
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

    public Boolean UpdateStatistics(int studentID, int assessmentID, double score, int correctAnswers, int wrongAnswers) {
        Boolean ret = false;
        try {
            String query ="INSERT INTO dqs_statistics (studentID, assesmentID, score, correct, wrong)"
                    + " VALUES (?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt (1, studentID);
            preparedStmt.setInt (2, assessmentID);
            preparedStmt.setString (3, String.valueOf(score));
            preparedStmt.setString   (4, String.valueOf(correctAnswers));
            preparedStmt.setString   (5, String.valueOf(wrongAnswers));

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public Boolean UpdateUserStatistics(int assessmentID, String resultScore) {
        Boolean ret = false;
        try {
            String query ="UPDATE dqs_studentsassessments SET completed = '1' , result = ? WHERE assessmentID = ?";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setString (1, resultScore);
            preparedStmt.setInt (2, assessmentID);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }


    public Boolean IsAssessmentCompleted(int AssessmentId) {
        String result = "";
        Boolean res = true;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT completed from dqs_studentsassessments WHERE assessmentID = %d", AssessmentId);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                result = rs.getString("completed");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (Integer.parseInt(result) == 0) {
            res = false;
        }
        return res;
    }

    public Map<Integer ,String> GetLecturerModules(int lecturerId) {
        Map<Integer, String> map = new HashMap<Integer, String>();

        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT module_id, module_name FROM dqs_modulesdata WHERE lecturer_id=%d", lecturerId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                map.put(rs.getInt("module_id"), rs.getString("module_name"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    public List<Integer> GetAllStudents() {
        String result = "";
        List<Integer> students = new ArrayList<Integer>();
        try {
            Statement stmt = Con.createStatement();
            String query = "SELECT id from dqs_users WHERE acc_type = '0'";
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                students.add(rs.getInt("id"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    public String GetNameById(int id) {
        String name = "";
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT name FROM dqs_users WHERE id=%d", id);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                name = rs.getString("name");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            id = -1;
        }
        return name;
    }

    public Boolean IsStudentEnrolled(int StudentId, int ModuleId) {
        Boolean enrolled = false;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT COUNT(*) AS SCOUNT FROM dqs_modules " +
                    "WHERE student_id = %d AND student_module_id=%d", StudentId, ModuleId);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                if (rs.getInt("SCOUNT") > 0) {
                    enrolled = true;
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return enrolled;
    }

    public Boolean EnrollStudent(int StudentId, int ModuleId) {
        Boolean ret = false;
        if (!IsStudentEnrolled(StudentId, ModuleId)) {
            try {
                String query = "INSERT INTO dqs_modules (student_module_id, student_id)"
                        + " VALUES (?, ?)";

                PreparedStatement preparedStmt = Con.prepareStatement(query);
                preparedStmt.setInt(1, ModuleId);
                preparedStmt.setInt(2, StudentId);

                preparedStmt.execute();
                ret = true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return ret;
    }

    public Boolean DelModuleStudent(int StudentId, int ModuleId) {
        Boolean ret = false;
        try {
            String query = String.format("DELETE FROM dqs_modules WHERE student_id = ? AND student_module_id = ?");

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt (1, StudentId);
            preparedStmt.setInt (2, ModuleId);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public Boolean CreateNewModule(String ModuleName, int LecturerId) {
        Boolean ret = false;
        try {
            String query = "INSERT INTO dqs_modulesdata (module_id, module_name, lecturer_id) " +
                    "VALUES ((select * from (SELECT MAX(module_id) from dqs_modulesdata) t) + 1, ?, ?)";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setString(1, ModuleName);
            preparedStmt.setInt(2, LecturerId);

            preparedStmt.execute();
            ret = true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public int GetLastModuleId() {
        int id = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = "SELECT MAX(module_id) as MCOUNT FROM dqs_modulesdata";
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                id = rs.getInt("MCOUNT");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public Boolean DeleteModule(int ModuleId) {
        Boolean ret = false;
        try {
            String query = String.format("DELETE FROM dqs_modulesdata WHERE module_id = ?");

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt (1, ModuleId);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }
    public Boolean DeleteModuleStudents(int ModuleId) {
        Boolean ret = false;
        try {
            String query = String.format("DELETE FROM dqs_modules WHERE student_module_id = ?");

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt (1, ModuleId);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public List<Integer> GetAllStudentsByModule(int ModuleId) {
        String result = "";
        List<Integer> students = new ArrayList<Integer>();
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT student_id from dqs_modules WHERE student_module_id = %d", ModuleId);
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                students.add(rs.getInt("student_id"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return students;
    }

    public int GetNewAssessmentId() {
        int id = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = "SELECT COUNT(*) AS RCOUNT FROM dqs_assessments";
            //System.out.println(query);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                id = rs.getInt("RCOUNT");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public Boolean CreateAssessment(int ModuleId, String title, String AssignedDate, String DueDate, String Type, String Questions) {
        Boolean ret = false;
        try {
            String query ="INSERT INTO dqs_assessments (assessment_id, moduleID, title, assignedDate, dueDate, type, indexes)"
                    + " VALUES ((select * from (SELECT MAX(assessment_id) from dqs_assessments) t) + 1, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt (1, ModuleId);
            preparedStmt.setString (2, title);
            preparedStmt.setString   (3, AssignedDate);
            preparedStmt.setString   (4, DueDate);
            preparedStmt.setString   (5, Type);
            preparedStmt.setString   (6, Questions);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }
    // Getting index of questions start
    public int GetNewIndex() {
        int count = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = "SELECT COUNT(qora) AS REGULAR_COUNT FROM dqs_qanda WHERE LENGTH(content) < 2";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                count = rs.getInt("REGULAR_COUNT");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public Boolean FillAssessmentQuestions(String query) {
        Boolean ret = false;
        try {
            Statement stmt = Con.createStatement();
            stmt.execute(query);
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public Boolean AddUsersToAssessment(int StudentId, int AssessmentId) {
        Boolean ret = false;
        try {
            String query ="INSERT INTO dqs_studentsassessments (studentID, assessmentID, completed, result) VALUES (?, ?, '0', '-1')";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setInt (1, StudentId);
            preparedStmt.setInt (2, AssessmentId);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    public int StudentHasNewAssessments(int StudentId) {
        int count = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT COUNT(assessmentID) AS REGULAR_COUNT " +
                    "FROM dqs_studentsassessments WHERE completed='0' AND studentID=%d", StudentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                count = rs.getInt("REGULAR_COUNT");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    // Statistics

    public double GetAvgAssessment(int AssessmentId) {
        double res = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT ROUND(AVG(score), 2) AS RES FROM dqs_statistics WHERE assesmentID=%d", AssessmentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                res = rs.getFloat("RES");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public double GetMinAssessment(int AssessmentId) {
        double res = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT ROUND(MIN(score), 2) AS RES FROM dqs_statistics WHERE assesmentID=%d", AssessmentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                res = rs.getFloat("RES");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public double GetMaxAssessment(int AssessmentId) {
        double res = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT ROUND(MAX(score), 2) AS RES FROM dqs_statistics WHERE assesmentID=%d", AssessmentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                res = rs.getFloat("RES");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public int GetPerfecAssessmentR(int AssessmentId) {
        int res = 0;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT COUNT(score) AS RCOUNT FROM dqs_statistics WHERE score='100.0' and assesmentID=%d", AssessmentId);
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                res = rs.getInt("RCOUNT");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res;
    }
}