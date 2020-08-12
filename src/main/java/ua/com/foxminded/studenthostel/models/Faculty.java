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
}
