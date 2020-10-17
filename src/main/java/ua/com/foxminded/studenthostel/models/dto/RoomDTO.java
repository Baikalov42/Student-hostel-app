package ua.com.foxminded.studenthostel.models.dto;

import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;

public class RoomDTO {

    private String name;
    private BigInteger id;
    private int studentsCount;
    private Floor floor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public int getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(int studentsCount) {
        this.studentsCount = studentsCount;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoomDTO roomDTO = (RoomDTO) o;

        if (studentsCount != roomDTO.studentsCount) return false;
        if (!name.equals(roomDTO.name)) return false;
        if (!id.equals(roomDTO.id)) return false;
        return floor.equals(roomDTO.floor);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + studentsCount;
        result = 31 * result + floor.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "RoomDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", studentsCount=" + studentsCount +
                ", floor=" + floor +
                '}';
    }
}
