package ua.com.foxminded.studenthostel.models;

public enum Faculty {
    WEB_DESIGN("web design"),
    DATABASE_ARCHITECTURE("database architecture"),
    GAME_DEVELOPMENT("game development"),
    FRONT_END_DEVELOPMENT("front end development"),
    BECK_END_DEVELOPMENT("beck end development");


    private final String name;

    Faculty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Faculty getByValue(String value) {
        Faculty result = null;
        for (Faculty faculty : Faculty.values()) {
            if (faculty.name.equals(value)) {
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
                '}';
    }
}
