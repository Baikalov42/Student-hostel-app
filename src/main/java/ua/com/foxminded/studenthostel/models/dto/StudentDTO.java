package ua.com.foxminded.studenthostel.models.dto;

import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.Task;

import java.math.BigInteger;
import java.util.List;

public class StudentDTO {
    private BigInteger id;
    private String firstName;
    private String lastName;
    private int hoursDebt;
    private GroupDTO groupDTO;
    private RoomDTO roomDTO;
    private List<Task> tasks;
    private List<Equipment> equipments;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

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

    public GroupDTO getGroupDTO() {
        return groupDTO;
    }

    public void setGroupDTO(GroupDTO groupDTO) {
        this.groupDTO = groupDTO;
    }

    public RoomDTO getRoomDTO() {
        return roomDTO;
    }

    public void setRoomDTO(RoomDTO roomDTO) {
        this.roomDTO = roomDTO;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Equipment> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<Equipment> equipments) {
        this.equipments = equipments;
    }
}
