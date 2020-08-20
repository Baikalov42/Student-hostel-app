package ua.com.foxminded.studenthostel.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class EquipmentDao {
    private JdbcTemplate jdbcTemplate;

    public EquipmentDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
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
                "WHERE student_id = ? AND equipment_id = ?";
        jdbcTemplate.update(query, studentId, equipmentId);
    }
}
