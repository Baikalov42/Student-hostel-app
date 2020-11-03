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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@NamedQuery(name = "Room.getAll", query = "SELECT m FROM Room m")
@Entity
@Table(name = "rooms")
@NotNull
public class Room {

    private static final String NAME_PATTERN = "[A-Z]{2}[-][0-9]{4}";

    private BigInteger id;
    private Set<Student> students = new HashSet<>();

    @NotNull
    private Floor floor;

    @NotNull
    @Size(min = 7, max = 7)
    @Pattern(regexp = NAME_PATTERN)
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "room_id")
    public BigInteger getId() {
        return id;
    }

    @Column(name = "room_name", unique = true, nullable = false, length = 30)
    public String getName() {
        return name;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", referencedColumnName = "floor_id")
    public Floor getFloor() {
        return floor;
    }

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER)
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (!Objects.equals(id, room.id)) return false;
        if (!Objects.equals(floor, room.floor)) return false;
        return Objects.equals(name, room.name);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (floor != null ? floor.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", floor=" + floor +
                ", name='" + name + '\'' +
                '}';
    }
}
