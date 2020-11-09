package ua.com.foxminded.studenthostel.models;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NamedQuery(name = "Task.getAll", query = "SELECT m FROM Task m")
@NotNull
@Entity
@Table(name = "tasks")
public class Task {

    private static final String NAME_PATTERN = "[A-Z](\\s?[a-zA-Z0-9]+)*";

    private BigInteger id;

    @ManyToMany(mappedBy = "tasks")
    private Set<Student> students = new HashSet<>();

    @NotNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @NotNull
    @Pattern(regexp = NAME_PATTERN)
    @Size(min = 10, max = 30)
    private String description;

    @Min(1)
    @Max(10)
    @NotNull
    private int costInHours;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "task_id")
    public BigInteger getId() {
        return id;
    }

    @Column(name = "cost", nullable = false)
    public int getCostInHours() {
        return costInHours;
    }

    @Column(name = "task_description", unique = true, nullable = false, length = 30)
    public String getDescription() {
        return description;
    }

    @Column(name = "task_name", unique = true, nullable = false, length = 30)
    public String getName() {
        return name;
    }

    @ManyToMany(mappedBy = "tasks")
    public Set<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
        student.getTasks().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getTasks().remove(this);
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCostInHours(int costInHours) {
        this.costInHours = costInHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (costInHours != task.costInHours) return false;
        if (!Objects.equals(id, task.id)) return false;
        if (!name.equals(task.name)) return false;
        return description.equals(task.description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + costInHours;
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", costInHours=" + costInHours +
                '}';
    }
}
