package ua.com.foxminded.studenthostel.models;

public class Task {
    private int id;
    private String name;
    private String description;
    private int costInHours;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCostInHours() {
        return costInHours;
    }

    public void setCostInHours(int costInHours) {
        this.costInHours = costInHours;
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
