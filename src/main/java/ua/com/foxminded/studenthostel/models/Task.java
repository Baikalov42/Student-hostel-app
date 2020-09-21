package ua.com.foxminded.studenthostel.models;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;

@NotNull
public class Task {

    private static final String NAME_PATTERN = "[A-Z](\\s?[a-zA-Z0-9]+)*";

    private BigInteger id;

    @Size(min = 3, max = 30)
    @NotNull
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Pattern(regexp = NAME_PATTERN)
    @NotNull
    @Size(min = 10, max = 30)
    private String description;

    @Min(1)
    @Max(10)
    @NotNull
    private int costInHours;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (costInHours != task.costInHours) return false;
        if (!id.equals(task.id)) return false;
        if (!name.equals(task.name)) return false;
        return description.equals(task.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + costInHours;
        return result;
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
