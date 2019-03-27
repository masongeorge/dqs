package Model;

import java.util.ArrayList;

public class Lecturer extends User{
    public ArrayList<LecturerModule> modules;

    public Lecturer(int id, String name, String email, ArrayList<LecturerModule> modules) {
        super(id, name, email);
        this.modules = modules;
    }
}
