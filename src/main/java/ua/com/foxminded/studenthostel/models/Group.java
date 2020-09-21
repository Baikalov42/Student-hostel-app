package ua.com.foxminded.studenthostel.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@NotNull
public class Group {

    private static final String NAME_PATTERN = "[A-Z]{3}[-][0-9]{4}";

    @NotNull
    @Size(min = 8, max = 8)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    private BigInteger id;

    @NotNull
    @Min(value = 1)
    private BigInteger facultyId;

    @NotNull
    @Min(value = 1)
    private BigInteger courseNumberId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public BigInteger getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(BigInteger facultyId) {
        this.facultyId = facultyId;
    }

    public BigInteger getCourseNumberId() {
        return courseNumberId;
    }

    public void setCourseNumberId(BigInteger courseNumberId) {
        this.courseNumberId = courseNumberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (!name.equals(group.name)) return false;
        if (!id.equals(group.id)) return false;
        if (!facultyId.equals(group.facultyId)) return false;
        return courseNumberId.equals(group.courseNumberId);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + facultyId.hashCode();
        result = 31 * result + courseNumberId.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", facultyId=" + facultyId +
                ", courseNumberId=" + courseNumberId +
                '}';
    }
}
