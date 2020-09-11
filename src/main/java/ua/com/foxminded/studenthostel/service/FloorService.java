package ua.com.foxminded.studenthostel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.foxminded.studenthostel.dao.FloorDao;
import ua.com.foxminded.studenthostel.exception.ValidationException;
import ua.com.foxminded.studenthostel.models.Floor;
import ua.com.foxminded.studenthostel.service.utils.ValidationsUtils;

import java.math.BigInteger;
import java.util.List;

@Service
public class FloorService {

    private static final String NAME_PATTERN = "[A-Z][a-z\\s]{1,29}";

    @Autowired
    FloorDao floorDao;


    public BigInteger insert(Floor floor) throws ValidationException {

        ValidationsUtils.validateName(floor.getName(), NAME_PATTERN);
        return floorDao.insert(floor);
    }

    public Floor getById(BigInteger id) throws ValidationException {

        ValidationsUtils.validateId(id);
        return floorDao.getById(id);
    }


    public List<Floor> getAll(long limit, long offset) {
        return floorDao.getAll(limit, offset);
    }

    public boolean update(Floor floor) throws ValidationException {
        ValidationsUtils.validateName(floor.getName(), NAME_PATTERN);
        return floorDao.update(floor);
    }


    public boolean deleteById(BigInteger id) throws ValidationException {
        return floorDao.deleteById(id);
    }
}
