package ua.com.foxminded.studenthostel.models;

import java.math.BigInteger;

public enum Equipment {

    TABLE(BigInteger.valueOf(1), "table"),
    BEDSIDE_TABLE(BigInteger.valueOf(2), "bedside table"),
    CHAIR(BigInteger.valueOf(3), "chair"),
    BED(BigInteger.valueOf(4), "bed"),
    MATTRESS(BigInteger.valueOf(5), "mattress"),
    LINEN(BigInteger.valueOf(6), "linen");

    private final String name;
    private final BigInteger id;

    Equipment(BigInteger id, String name) {
        this.name = name;
        this.id = id;
    }

    public BigInteger getId() {
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
                ", id=" + id +
                '}';
    }
}
