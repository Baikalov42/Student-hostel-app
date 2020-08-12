package ua.com.foxminded.studenthostel.models;

import java.util.List;

public class Student {

    private String firstName;
    private String lastName;
    private int hoursDebt;
    private Group group;
    private List<Equipment> equipments;
    private Room room;
    private List<Task> tasks;

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
}
