package ua.com.foxminded.studenthostel.models;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NotNull
@Entity
@Table(name = "students", uniqueConstraints =
@UniqueConstraint(columnNames = {"first_name", "last_name"}))
public class Student {

    private static final String NAME_PATTERN = "[A-Z][a-z]{1,29}";

    private BigInteger id;

    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = NAME_PATTERN)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = NAME_PATTERN)
    private String lastName;

    @NotNull
    @Min(value = 0)
    @Max(value = 40)
    private int hoursDebt;

    @NotNull
    private Group group;

    @NotNull
    private Room room;

    private Set<Equipment> equipments = new HashSet<>();
    private Set<Task> tasks = new HashSet<>();


    public Student() {
    }

    public Student(BigInteger id, String firstName,
                   String lastName, int hoursDebt, Group group, Room room) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hoursDebt = hoursDebt;
        this.group = group;
        this.room = room;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "student_id")
    public BigInteger getId() {
        return id;
    }

    @Column(name = "first_name", nullable = false, length = 30)
    public String getFirstName() {
        return firstName;
    }

    @Column(name = "last_name", nullable = false, length = 30)
    public String getLastName() {
        return lastName;
    }

    @Column(name = "hours_debt", nullable = false)
    public int getHoursDebt() {
        return hoursDebt;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "group_id")
    public Group getGroup() {
        return group;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    public Room getRoom() {
        return room;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "students_equipments",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "equipment_id")}
    )
    public Set<Equipment> getEquipments() {
        return equipments;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "students_tasks",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "task_id")}
    )
    public Set<Task> getTasks() {
        return tasks;
    }

    public void setEquipments(Set<Equipment> equipments) {
        this.equipments = equipments;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHoursDebt(int hoursDebt) {
        this.hoursDebt = hoursDebt;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (!Objects.equals(id, student.id)) return false;
        if (!Objects.equals(firstName, student.firstName)) return false;
        return Objects.equals(lastName, student.lastName);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", hoursDebt=" + hoursDebt +
                ", group=" + group +
                ", room=" + room +
                ", equipments=" + equipments +
                ", tasks=" + tasks +
                '}';
    }
}
