package ua.com.foxminded.studenthostel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ua.com.foxminded.studenthostel.dao.*;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:db.properties")
public class SpringConfig {

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.driverClassName}")
    private String driverClassName;


    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public TaskDao taskDao() {
        return new TaskDao(dataSource());
    }

    @Bean
    public StudentDao studentDao() {
        return new StudentDao(dataSource());
    }

    @Bean
    public RoomDao roomDao() {
        return new RoomDao(dataSource());
    }

    @Bean
    public FloorDao floorDao() {
        return new FloorDao(dataSource());
    }

    @Bean
    public EquipmentDao equipmentDao() {
        return new EquipmentDao(dataSource());
    }

}
