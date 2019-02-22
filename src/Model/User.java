package Model;

public class User {
    private int userID;
    private String name;
    private boolean isStudent;
    private String email;
    private String password;

    public User(int userID, String name, boolean isStudent, String email, String password){
        this.userID = userID;
        this.name = name;
        this.isStudent = isStudent;
        this.email = email;
        this.password = password;
    }
}
