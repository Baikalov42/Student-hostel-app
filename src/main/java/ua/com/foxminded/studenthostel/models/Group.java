package ua.com.foxminded.studenthostel.models;

public class Group {

    private String name;
    private int facultyId;
    private int courseNumberId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getCourseNumberId() {
        return courseNumberId;
    }

    public void setCourseNumberId(int courseNumberId) {
        this.courseNumberId = courseNumberId;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", facultyId=" + facultyId +
                ", courseNumberId=" + courseNumberId +
                '}';
    }
}
