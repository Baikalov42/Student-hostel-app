package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.mappers.EquipmentMapper;

@Component
public class EquipmentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EquipmentMapper equipmentMapper;

    public void checkAndUpdate(Equipment equipment) {
        String query = "" +
                "INSERT INTO equipments(equipments_id, equipments_name) " +
                "VALUES (? , ? ) " +
                "ON CONFLICT ON CONSTRAINT equipments_pkey DO UPDATE " +
                "SET equipments_id = excluded.equipments_id, equipments_name = excluded.equipments_name;";

        jdbcTemplate.update(query, equipment.getId(), equipment.getName());
    }

    public void setToStudent(int studentId, int equipmentId) {
        String query = "" +
                "INSERT INTO students_equipments (student_id, equipment_id) " +
                "VALUES (? , ?)";
        jdbcTemplate.update(query, studentId, equipmentId);
    }

    public void removeFromStudent(int studentId, int equipmentId) {
        String query = "" +
                "DELETE FROM students_equipments " +
                "WHERE student_id = ? " +
                "AND equipment_id = ?";
        jdbcTemplate.update(query, studentId, equipmentId);
    }

    public Equipment getById(int equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "WHERE equipments_id = ? ";

        return jdbcTemplate.queryForObject(query, equipmentMapper, equipmentId);
    }
}
