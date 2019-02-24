package Model;

public class User {
    private int UserId;
    private String Name;
    private boolean IsStudent;
    private String Email;
    private String Modules;

    public User(int userId, String name, boolean isStudent, String email, String modules){
        this.UserId = userId;
        this.Name = name;
        this.IsStudent = isStudent;
        this.Email = email;
        this.Modules = modules;
    }

    public int getId() {
        return this.UserId;
    }

    public String getName() {
        return this.Name;
    }

    public Boolean isStudent() {
        return this.IsStudent;
    }

    public String getEmail() {
        return this.Email;
    }

    public String getModules() {
        return this.Modules;
    }

    public void setId(int id) {this.UserId = id;}
    public void setName(String name) {this.Name = name;}
    public void setType(int type) {if (type > 0) {this.IsStudent = false;} else {this.IsStudent = true;}}
    public void setEmail(String email) {this.Email = email;}
    public void setModules(String modules) {this.Modules = modules;}
}
