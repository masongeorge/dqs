package Model;

public class User {
    private int UserId;
    private String Name;
    private boolean IsStudent;
    private String Email;

    public User(int id, String name, boolean isstudent, String email){
        this.UserId = id;
        this.Name = name;
        this.IsStudent = isstudent;
        this.Email = email;
    }

    public String getName() {
        return this.Name;
    }

    public int getId() {
        return this.UserId;
    }

    public boolean isUserStudent() {
        return IsStudent;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setName(String n) {this.Name = n;}
    public void setEmail(String e) {this.Email = e;}
}
