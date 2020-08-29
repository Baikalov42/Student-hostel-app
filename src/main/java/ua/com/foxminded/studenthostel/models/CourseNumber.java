package ua.com.foxminded.studenthostel.models;

import java.math.BigInteger;

public enum CourseNumber {
    FIRST(BigInteger.valueOf(1), "first"),
    SECOND(BigInteger.valueOf(2), "second"),
    THIRD(BigInteger.valueOf(3), "third"),
    FOURTH(BigInteger.valueOf(4), "fourth"),
    FIFTH(BigInteger.valueOf(5), "fifth");

    private final String name;
    private final BigInteger id;

    CourseNumber(BigInteger id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigInteger getId() {
        return id;
    }

    public static CourseNumber getByName(String name) {
        CourseNumber result = null;
        for (CourseNumber courseNumber : CourseNumber.values()) {
            if (courseNumber.name.equals(name)) {
                result = courseNumber;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("incorrect value");
        }
        return result;
    }

    @Override
    public String toString() {
        return "CourseNumber{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
