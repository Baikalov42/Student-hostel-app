package ua.com.foxminded.studenthostel.models;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Objects;

@NamedQuery(name = "CourseNumber.getAll", query = "SELECT m FROM CourseNumber m")
@SequenceGenerator(name = "course_seq", initialValue = 10)
@NotNull
@Entity
@Table(name = "course_numbers")
public class CourseNumber {

    private static final String NAME_PATTERN = "[A-Z][a-z]{3,29}";

    private BigInteger id;

    @NotNull
    @Size(min = 4, max = 30)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Column(name = "course_number_name", unique = true, nullable = false, length = 30)
    public String getName() {
        return name;
    }

    @Id
    @Column(name = "course_number_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_seq")
    public BigInteger getId() {
        return id;
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

        CourseNumber courseNumber = (CourseNumber) o;

        if (!name.equals(courseNumber.name)) return false;
        return Objects.equals(id, courseNumber.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CourseNumber1{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
