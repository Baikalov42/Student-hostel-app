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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentDTO that = (StudentDTO) o;

        if (hoursDebt != that.hoursDebt) return false;
        if (!id.equals(that.id)) return false;
        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        if (!groupDTO.equals(that.groupDTO)) return false;
        if (!roomDTO.equals(that.roomDTO)) return false;
        if (!tasks.equals(that.tasks)) return false;
        return equipments.equals(that.equipments);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + hoursDebt;
        result = 31 * result + groupDTO.hashCode();
        result = 31 * result + roomDTO.hashCode();
        result = 31 * result + tasks.hashCode();
        result = 31 * result + equipments.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StudentDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hoursDebt=" + hoursDebt +
                ", groupDTO=" + groupDTO +
                ", roomDTO=" + roomDTO +
                ", tasks=" + tasks +
                ", equipments=" + equipments +
                '}';
    }
}
