package ua.com.foxminded.studenthostel.models;

public class Group {

    private String name;
    private Faculty faculty;
    private CourseNumber courseNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' + ", faculty=" + faculty + ", courseNumber=" + courseNumber + '}';
    }
}
