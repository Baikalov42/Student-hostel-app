package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.mappers.EquipmentMapper;

import java.math.BigInteger;
import java.util.List;

@Repository
public class EquipmentDaoImpl implements EquipmentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void save(Equipment equipment) {
        String query = "" +
                "INSERT INTO equipments(equipments_id, equipments_name) " +
                "VALUES (? , ? ) " +
                "ON CONFLICT (equipments_id) DO NOTHING";

        jdbcTemplate.update(query, equipment.getId(), equipment.getName());
    }

    @Override
    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        String query = "" +
                "INSERT INTO students_equipments (student_id, equipment_id) " +
                "VALUES (? , ?)";
        return jdbcTemplate.update(query, studentId, equipmentId) == 1;
    }

    @Override
    public boolean removeFromStudent(BigInteger studentId, BigInteger equipmentId) {
        String query = "" +
                "DELETE FROM students_equipments " +
                "WHERE student_id = ? " +
                "AND equipment_id = ?";
        return jdbcTemplate.update(query, studentId, equipmentId) == 1;
    }

    @Override
    public Equipment getById(BigInteger equipmentId) {
        String query = "" +
                "SELECT * FROM equipments " +
                "WHERE equipments_id = ? ";
        try {
            return jdbcTemplate.queryForObject(query, new EquipmentMapper(), equipmentId);
        } catch (EmptyResultDataAccessException e) {
            throw new DaoException("failed to get object");
        }
    }

    @Override
    public List<Equipment> getAll(long limit, long offset) {
        String query = "" +
                "SELECT * " +
                "FROM equipments " +
                "ORDER BY equipments_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new EquipmentMapper(), limit, offset);
    }
}
