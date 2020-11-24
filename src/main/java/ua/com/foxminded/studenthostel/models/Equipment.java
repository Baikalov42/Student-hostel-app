package ua.com.foxminded.studenthostel.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NotNull
@Entity
@Table(name = "equipments")
public class Equipment {

    private static final String NAME_PATTERN = "[A-Z](\\s?[a-zA-Z0-9]+)*";

    private BigInteger id;

    private Set<Student> students = new HashSet<>();

    @NotNull
    @Size(min = 4, max = 30)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "equipment_id", nullable = false, unique = true)
    public BigInteger getId() {
        return id;
    }

    @Column(name = "equipment_name", unique = true, nullable = false, length = 30)
    public String getName() {
        return name;
    }

    @ManyToMany(mappedBy = "equipments")
    public Set<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.getEquipments().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getEquipments().remove(this);
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Equipment equipment = (Equipment) o;

        if (!name.equals(equipment.name)) return false;
        return Objects.equals(id, equipment.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
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
