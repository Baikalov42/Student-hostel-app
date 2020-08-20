package ua.com.foxminded.studenthostel.models;

public enum CourseNumber {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5);

    private final int id;

    CourseNumber(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public CourseNumber getByNumber(int id) {
        CourseNumber result = null;

        for (CourseNumber constant : CourseNumber.values()) {
            if (constant.getId() == id) {
                result = constant;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("incorrect name");
        }
        return result;
    }

    @Override
    public String toString() {
        return "CourseNumber{" +
                "number=" + id +
                '}';
    }
}
