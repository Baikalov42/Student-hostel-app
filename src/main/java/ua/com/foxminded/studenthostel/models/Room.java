package ua.com.foxminded.studenthostel.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@NotNull
public class Room {

    private static final String NAME_PATTERN = "[A-Z]{2}[-][0-9]{4}";

    @NotNull
    @Size(min = 7, max = 7)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    private BigInteger id;

    @NotNull
    @Min(value = 1)
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
