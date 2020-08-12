package ua.com.foxminded.studenthostel.models;

public enum CourseNumber {
    FIRST(1),
    SECOND(2),
    THIRD(3),
    FOURTH(4),
    FIFTH(5);

    private final int number;

    CourseNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public CourseNumber getByNumber(int value) {
        CourseNumber result = null;

        for (CourseNumber constant : CourseNumber.values()) {
            if (constant.getNumber() == value) {
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
                "number=" + number +
                '}';
    }
}
