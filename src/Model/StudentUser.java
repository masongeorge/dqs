package Model;

import java.util.ArrayList;

public class StudentUser extends User {

    public ArrayList<StudentModule> studentModules;

    public StudentUser(int id, String name, String email){
        super(id,name,email);
        studentModules = new ArrayList<StudentModule>();
    }



}
