package Model;

import java.util.ArrayList;

public class Lecturer extends User{
    public ArrayList<LecturerModule> modules;

    public Lecturer(int id, String name, String email) {
        super(id, name, email);
        modules = new ArrayList<LecturerModule>();
    }
}
