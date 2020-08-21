package ua.com.foxminded.studenthostel;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.studenthostel.config.SpringConfig;
import ua.com.foxminded.studenthostel.dao.EquipmentDao;
import ua.com.foxminded.studenthostel.models.Equipment;

public class StudentHostelApp {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        EquipmentDao equipmentDao = context.getBean(EquipmentDao.class);
        for(Equipment equipment: Equipment.values()){
            equipmentDao.checkAndUpdate(equipment);
        }
    }
}
