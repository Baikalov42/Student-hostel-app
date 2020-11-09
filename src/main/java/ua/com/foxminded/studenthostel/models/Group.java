package ua.com.foxminded.studenthostel.models;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.Objects;

@NamedQuery(name = "Group.getAll", query = "SELECT m FROM Group m")
@Entity
@Table(name = "groups")
@NotNull
public class Group {

    private static final String NAME_PATTERN = "[A-Z]{3}[-][0-9]{4}";

    private BigInteger id;

    @NotNull
    private Faculty faculty;

    @NotNull
    private CourseNumber courseNumber;

    @NotNull
    @Size(min = 8, max = 8)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Column(name = "group_name", nullable = false, unique = true, length = 30)
    public String getName() {
        return name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "group_id",nullable = false, unique = true)
    public BigInteger getId() {
        return id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", referencedColumnName = "faculty_id")
    public Faculty getFaculty() {
        return faculty;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_number_id", referencedColumnName = "course_number_id")
    public CourseNumber getCourseNumber() {
        return courseNumber;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseNumber(CourseNumber courseNumber) {
        this.courseNumber = courseNumber;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (!Objects.equals(id, group.id)) return false;
        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", faculty=" + faculty +
                ", courseNumber=" + courseNumber +
                '}';
    }
}
