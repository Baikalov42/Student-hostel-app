package ua.com.foxminded.studenthostel.models;

import java.math.BigInteger;

public class Room {

    private String name;
    private BigInteger id;
    private BigInteger floorId;

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

    public BigInteger getFloorId() {
        return floorId;
    }

    public void setFloorId(BigInteger floorId) {
        this.floorId = floorId;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (!name.equals(room.name)) return false;
        if (!id.equals(room.id)) return false;
        return floorId.equals(room.floorId);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + floorId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", floorId=" + floorId +
                '}';
    }
}
