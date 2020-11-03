package ua.com.foxminded.studenthostel.models;

import org.hibernate.annotations.NamedQuery;

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

@NamedQuery(name="Faculty.getAll", query="SELECT m FROM Faculty m")
@Entity
@Table(name = "faculties")
@NotNull
public class Faculty {

    private static final String NAME_PATTERN = "[A-Z](\\s?[a-zA-Z0-9]+)*";

    private BigInteger id;

    @NotNull
    @Size(min = 4, max = 30)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Column(name = "faculty_name", unique = true, nullable = false, length = 30)
    public String getName() {
        return name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "faculty_id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Faculty faculty = (Faculty) o;

        if (!name.equals(faculty.name)) return false;
        return Objects.equals(id, faculty.id);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
