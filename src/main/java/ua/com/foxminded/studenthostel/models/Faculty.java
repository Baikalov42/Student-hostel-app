package ua.com.foxminded.studenthostel.models;

import java.math.BigInteger;

public enum Faculty {
    WEB_DESIGN(BigInteger.valueOf(1), "web design"),
    DATABASE_ARCHITECTURE(BigInteger.valueOf(2), "database architecture"),
    GAME_DEVELOPMENT(BigInteger.valueOf(3), "game development"),
    FRONT_END_DEVELOPMENT(BigInteger.valueOf(4), "front end development"),
    BECK_END_DEVELOPMENT(BigInteger.valueOf(5), "beck end development");


    private final String name;
    private final BigInteger id;

    Faculty(BigInteger id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigInteger getId() {
        return id;
    }

    public static Faculty getByName(String name) {
        Faculty result = null;
        for (Faculty faculty : Faculty.values()) {
            if (faculty.name.equals(name)) {
                result = faculty;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("incorrect value");
        }
        return result;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
