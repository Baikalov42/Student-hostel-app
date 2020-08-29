package ua.com.foxminded.studenthostel.models.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ua.com.foxminded.studenthostel.models.Equipment;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EquipmentMapper implements RowMapper<Equipment> {

   @Override
    public Equipment mapRow(ResultSet resultSet, int i) throws SQLException {

        String name = resultSet.getString("equipments_name");
        return Equipment.getByName(name);
    }
}
