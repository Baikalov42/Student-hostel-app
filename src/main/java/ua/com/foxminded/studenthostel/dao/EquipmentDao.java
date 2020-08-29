package ua.com.foxminded.studenthostel.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.mappers.EquipmentMapper;

import java.math.BigInteger;

@Component
public class EquipmentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(Equipment equipment) {
        String query = "" +
                "INSERT INTO equipments(equipments_id, equipments_name) " +
                "VALUES (? , ? ) ";

        jdbcTemplate.update(query, equipment.getId(), equipment.getName());
    }

    public void setToStudent(BigInteger studentId, BigInteger equipmentId) {
        String query = "" +
                "INSERT INTO students_equipments (student_id, equipment_id) " +
                "VALUES (? , ?)";
        jdbcTemplate.update(query, studentId, equipmentId);
    }

    public void removeFromStudent(BigInteger studentId, BigInteger equipmentId) {
        String query = "" +
                "DELETE FROM students_equipments " +
                "WHERE student_id = ? " +
                "AND equipment_id = ?";
        jdbcTemplate.update(query, studentId, equipmentId);
    }

    public Equipment getById(BigInteger equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "WHERE equipments_id = ? ";

        return jdbcTemplate.queryForObject(query, new EquipmentMapper(), equipmentId);
    }
}
