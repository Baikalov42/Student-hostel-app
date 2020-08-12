package ua.com.foxminded.studenthostel.models;

import java.util.List;

public class Task {
    private String name;
    private String description;
    private int costInHours;
    private List<Student> executors;

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

    public List<Student> getExecutors() {
        return executors;
    }

    public void setExecutors(List<Student> executors) {
        this.executors = executors;
    }
}
