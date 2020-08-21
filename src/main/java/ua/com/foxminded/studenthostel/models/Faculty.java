package ua.com.foxminded.studenthostel.models;

public enum Faculty {
    WEB_DESIGN(1,"web design"),
    DATABASE_ARCHITECTURE(2,"database architecture"),
    GAME_DEVELOPMENT(3,"game development"),
    FRONT_END_DEVELOPMENT(4,"front end development"),
    BECK_END_DEVELOPMENT(5,"beck end development");


    private final String name;
    private final int id;

    Faculty(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
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
                '}';
    }
}
