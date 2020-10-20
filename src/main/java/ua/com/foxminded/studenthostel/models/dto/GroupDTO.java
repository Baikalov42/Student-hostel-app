package ua.com.foxminded.studenthostel.models.dto;

import ua.com.foxminded.studenthostel.models.CourseNumber;
import ua.com.foxminded.studenthostel.models.Faculty;

import java.math.BigInteger;

public class GroupDTO {

    private String name;
    private BigInteger id;
    private Faculty faculty;
    private CourseNumber courseNumber;

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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public CourseNumber getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(CourseNumber courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupDTO groupDTO = (GroupDTO) o;

        if (!name.equals(groupDTO.name)) return false;
        if (!id.equals(groupDTO.id)) return false;
        if (!faculty.equals(groupDTO.faculty)) return false;
        return courseNumber.equals(groupDTO.courseNumber);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + faculty.hashCode();
        result = 31 * result + courseNumber.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GroupDTO{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", faculty=" + faculty +
                ", courseNumber=" + courseNumber +
                '}';
    }
}
