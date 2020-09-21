package ua.com.foxminded.studenthostel.models.dto;

import ua.com.foxminded.studenthostel.models.Floor;

import java.math.BigInteger;

public class RoomDTO {

    private String name;
    private BigInteger id;
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

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }
}
