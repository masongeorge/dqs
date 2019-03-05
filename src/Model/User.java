package Model;

import javafx.collections.ObservableList;

public class User {

    private int UserId;
    private String Name;
    private String Email;

    public User(int id, String name, String email){
        this.UserId = id;
        this.Name = name;
        this.Email = email;
    }

    public String getName() {
        return this.Name;
    }

    public int getId() {
        return this.UserId;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setName(String n) {this.Name = n;}
    public void setEmail(String e) {this.Email = e;}


}
