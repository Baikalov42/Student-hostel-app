package ua.com.foxminded.studenthostel.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.mappers.EquipmentMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class EquipmentDaoImpl implements EquipmentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Equipment equipment) {

        String query = "" +
                "INSERT INTO equipments(equipment_name) " +
                "VALUES ( ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(query, new String[]{"equipment_id"});
            ps.setString(1, equipment.getName());
            return ps;
        }, keyHolder);

        return BigInteger.valueOf(keyHolder.getKey().longValue());
    }

    @Override
    public boolean update(Equipment equipment) {
        String query = "" +
                "UPDATE equipments " +
                "SET equipment_name = ? " +
                "WHERE equipment_id = ? ";

        return jdbcTemplate.update(query, equipment.getName(), equipment.getId()) == 1;
    }

    @Override
    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        String query = "" +
                "INSERT INTO students_equipments (student_id, equipment_id) " +
                "VALUES (? , ?)";
        return jdbcTemplate.update(query, studentId, equipmentId) == 1;
    }

    @Override
    public boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
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
                "WHERE equipment_id = ? ";
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
                "ORDER BY equipment_id " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, new EquipmentMapper(), limit, offset);
    }

    @Override
    public boolean deleteById(BigInteger id) {
        String query = "" +
                "DELETE FROM equipments " +
                "WHERE equipment_id = ? ";

        return jdbcTemplate.update(query, id) == 1;
    }
}
