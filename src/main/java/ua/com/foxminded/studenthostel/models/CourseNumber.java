package ua.com.foxminded.studenthostel.models;

public enum CourseNumber {
    FIRST(1,"first"),
    SECOND(2,"second"),
    THIRD(3,"third"),
    FOURTH(4,"fourth"),
    FIFTH(5,"fifth");

    private final String name;
    private final int id;

    CourseNumber(int id,String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
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
