package ua.com.foxminded.studenthostel.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.math.BigInteger;

public class Student {

    private static final String NAME_PATTERN = "[A-Z][a-z]{1,29}";

    private BigInteger id;

    @Pattern(regexp = NAME_PATTERN)
    private String firstName;

    @Pattern(regexp = NAME_PATTERN)
    private String lastName;

    @Min(value = 0)
    @Max(value = 40)
    private int hoursDebt;

    @Min(value = 1)
    private BigInteger groupId;

    @Min(value = 1)
    private BigInteger roomId;

    public Student() {
    }

    public Student(BigInteger id, String firstName, String lastName,
                   int hoursDebt, BigInteger groupId, BigInteger roomId) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hoursDebt = hoursDebt;
        this.groupId = groupId;
        this.roomId = roomId;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getHoursDebt() {
        return hoursDebt;
    }

    public void setHoursDebt(int hoursDebt) {
        this.hoursDebt = hoursDebt;
    }

    public BigInteger getGroupId() {
        return groupId;
    }

    public void setGroupId(BigInteger groupId) {
        this.groupId = groupId;
    }

    public BigInteger getRoomId() {
        return roomId;
    }

    public void setRoomId(BigInteger roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (hoursDebt != student.hoursDebt) return false;
        if (!id.equals(student.id)) return false;
        if (!firstName.equals(student.firstName)) return false;
        if (!lastName.equals(student.lastName)) return false;
        if (!groupId.equals(student.groupId)) return false;
        return roomId.equals(student.roomId);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + hoursDebt;
        result = 31 * result + groupId.hashCode();
        result = 31 * result + roomId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hoursDebt=" + hoursDebt +
                ", groupId=" + groupId +
                ", roomId=" + roomId +
                '}';
    }
}
