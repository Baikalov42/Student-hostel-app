package ua.com.foxminded.studenthostel.models;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private String firstName;
    private String lastName;
    private int hoursDebt;
    private Group group;
    private Room room;
    private List<Equipment> equipments = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getHoursDebt() {
        return hoursDebt;
    }

    public void setHoursDebt(int hoursDebt) {
        this.hoursDebt = hoursDebt;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return "Student{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hoursDebt=" + hoursDebt +
                ", group=" + group +
                ", room=" + room +
                ", equipments=" + equipments +
                ", tasks=" + tasks +
                '}';
    }
}
