package ua.com.foxminded.studenthostel.models;

public enum Equipment {

    TABLE("table"),
    BEDSIDE_TABLE("bedside table"),
    CHAIR("chair"),
    BED("bed"),
    MATTRESS("mattress"),
    LINEN("linen");

    private final String name;

    Equipment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Equipment getByValue(String value) {
        Equipment result = null;
        for (Equipment equipment : Equipment.values()) {
            if (equipment.name.equals(value)) {
                result = equipment;
            }
        }
        if (result == null) {
            throw new IllegalArgumentException("incorrect value");
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
