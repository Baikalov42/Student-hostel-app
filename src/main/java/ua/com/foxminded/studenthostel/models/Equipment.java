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
    public String getName(){
        return name;
    }
}
