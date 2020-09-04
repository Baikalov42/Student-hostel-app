package ua.com.foxminded.studenthostel.models;

import java.math.BigInteger;

public class Floor {

    private String name;
    private BigInteger id;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Floor floor = (Floor) o;

        if (!name.equals(floor.name)) return false;
        return id.equals(floor.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
