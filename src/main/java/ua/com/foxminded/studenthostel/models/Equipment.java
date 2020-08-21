package ua.com.foxminded.studenthostel.models;

public enum Equipment {

    TABLE(1, "table"),
    BEDSIDE_TABLE(2, "bedside table"),
    CHAIR(3, "chair"),
    BED(4, "bed"),
    MATTRESS(5, "mattress"),
    LINEN(6, "linen");

    private final String name;
    private int id;

    Equipment(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Equipment getByName(String name) {
        Equipment result = null;
        for (Equipment equipment : Equipment.values()) {
            if (equipment.name.equals(name)) {
                result = equipment;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("incorrect name");
        }
        return result;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "name='" + name + '\'' +
                '}';
    }
}
