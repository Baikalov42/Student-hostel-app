package ua.com.foxminded.studenthostel.models;

import java.math.BigInteger;

public class CourseNumber {

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

        CourseNumber that = (CourseNumber) o;

        if (!name.equals(that.name)) return false;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CourseNumber1{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
