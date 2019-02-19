import java.sql.*;

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

    protected int GetAccountType(String Username) {
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

    protected Boolean RegisterAccount(String Username, String Password, String AccountType) {
        Boolean ret = false;
        try {
            String query = "INSERT INTO Users (username, password, acc_type)"
                    + " VALUES (?, ?, ?)";

            PreparedStatement preparedStmt = Con.prepareStatement(query);
            preparedStmt.setString (1, Username);
            preparedStmt.setString (2, Password);
            preparedStmt.setString   (3, AccountType);

            preparedStmt.execute();
            ret = true;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    protected Boolean DeleteAccount(int Id) {
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

    protected Boolean CheckAccountExists(String Username) {
        Boolean ret = false;
        try {
            Statement stmt = Con.createStatement();
            String query = String.format("SELECT COUNT(*) AS N FROM Users WHERE username='%s'", Username);
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

    protected int GetIdByName(String Username) {
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
}