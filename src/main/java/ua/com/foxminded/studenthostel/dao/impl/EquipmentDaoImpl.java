package ua.com.foxminded.studenthostel.dao.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.exception.DaoException;
import ua.com.foxminded.studenthostel.exception.NotFoundException;
import ua.com.foxminded.studenthostel.models.Equipment;
import ua.com.foxminded.studenthostel.models.mappers.EquipmentMapper;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class EquipmentDaoImpl implements EquipmentDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public BigInteger insert(Equipment equipment) {
        LOGGER.debug("inserting {}", equipment);

        String query = "" +
                "INSERT INTO equipments(equipment_name) " +
                "VALUES ( ? )";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(query, new String[]{"equipment_id"});
                ps.setString(1, equipment.getName());
                return ps;
            }, keyHolder);

            long id = keyHolder.getKey().longValue();
            LOGGER.debug("inserting complete, id = {}", id);
            return BigInteger.valueOf(id);

        } catch (DataAccessException ex) {
            LOGGER.error("insertion error {}", equipment, ex);
            throw new DaoException("Insertion error: " + equipment, ex);
        }
    }

    @Override
    public Equipment getById(BigInteger equipmentId) {
        LOGGER.debug("getting by id {}", equipmentId);

        String query = "" +
                "SELECT * FROM equipments " +
                "WHERE equipment_id = ? ";
        try {
            Equipment equipment = jdbcTemplate.queryForObject(query, new EquipmentMapper(), equipmentId);
            LOGGER.debug("getting complete {}", equipment);
            return equipment;

        } catch (EmptyResultDataAccessException ex) {
            LOGGER.warn("Failed get by id {}", equipmentId, ex);
            throw new NotFoundException("Failed get by id: " + equipmentId, ex);
        }
    }

    @Override
    public List<Equipment> getAll(long limit, long offset) {
        LOGGER.debug("getting all, limit {} , offset {} ", limit, offset);

        String query = "" +
                "SELECT * " +
                "FROM equipments " +
                "ORDER BY equipment_id " +
                "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(query, new EquipmentMapper(), limit, offset);
    }

    @Override
    public List<Equipment> getAllByStudent(BigInteger studentId) {
        LOGGER.debug("getting all by Student id {} ", studentId);

        String query = "" +
                "SELECT * " +
                "FROM students_equipments " +
                "INNER JOIN equipments " +
                "ON students_equipments.equipment_id = equipments.equipment_id " +
                "WHERE student_id = ? ";

        return jdbcTemplate.query(query, new EquipmentMapper(), studentId);
    }

    @Override
    public boolean assignToStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("assigning, student id {}, equipment id {}", studentId, equipmentId);

        String query = "" +
                "INSERT INTO students_equipments (student_id, equipment_id) " +
                "VALUES (? , ?)";
        try {
            return jdbcTemplate.update(query, studentId, equipmentId) == 1;

        } catch (DataAccessException ex) {

            LOGGER.error("failed assigning, student id {}, equipment id {}", studentId, equipmentId, ex);
            String message = "student id =" + studentId + " equipment id =" + equipmentId;
            throw new DaoException(message, ex);
        }
    }

    @Override
    public boolean unassignFromStudent(BigInteger studentId, BigInteger equipmentId) {
        LOGGER.debug("un assigning, student id {}, equipment id {}", studentId, equipmentId);

        String query = "" +
                "DELETE FROM students_equipments " +
                "WHERE student_id = ? " +
                "AND equipment_id = ?";
        try {
            return jdbcTemplate.update(query, studentId, equipmentId) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("failed un assigning, student id {}, equipment id {}", studentId, equipmentId, ex);
            throw new DaoException("student id =" + studentId + " equipment id =" + equipmentId, ex);
        }
    }

    @Override
    public boolean update(Equipment equipment) {
        LOGGER.debug("updating {}", equipment);

        String query = "" +
                "UPDATE equipments " +
                "SET equipment_name = ? " +
                "WHERE equipment_id = ? ";
        try {
            return jdbcTemplate.update(query, equipment.getName(), equipment.getId()) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("updating error {}", equipment, ex);
            throw new DaoException("Updating error: " + equipment, ex);
        }
    }

    @Override
    public boolean deleteById(BigInteger id) {
        LOGGER.debug("deleting by id {}", id);

        String query = "" +
                "DELETE FROM equipments " +
                "WHERE equipment_id = ? ";
        try {
            return jdbcTemplate.update(query, id) == 1;

        } catch (DataAccessException ex) {
            LOGGER.error("deleting error {}", id, ex);
            throw new DaoException("Deleting error: " + id, ex);
        }
    }
}
