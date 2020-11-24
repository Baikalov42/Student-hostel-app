package ua.com.foxminded.studenthostel.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Objects;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

        CourseNumber that = (CourseNumber) o;

        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
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
