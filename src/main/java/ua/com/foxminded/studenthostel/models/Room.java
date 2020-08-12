package ua.com.foxminded.studenthostel.models;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private String name;
    private List<Student> students = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
