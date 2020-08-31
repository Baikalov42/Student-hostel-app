package ua.com.foxminded.studenthostel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.studenthostel")
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
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    public SimpleJdbcInsert floorJdbcInsert() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate());

        jdbcInsert.withTableName("floors");
        jdbcInsert.usingGeneratedKeyColumns("floor_id");

        return jdbcInsert;
    }
    @Bean
    public SimpleJdbcInsert groupJdbcInsert() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate());
        jdbcInsert.withTableName("groups");
        jdbcInsert.usingGeneratedKeyColumns("group_id");

        return jdbcInsert;
    }
    @Bean
    public SimpleJdbcInsert roomJdbcInsert() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate());
        jdbcInsert.withTableName("rooms");
        jdbcInsert.usingGeneratedKeyColumns("room_id");

        return jdbcInsert;
    }
    @Bean
    public SimpleJdbcInsert studentJdbcInsert() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate());
        jdbcInsert.withTableName("students");
        jdbcInsert.usingGeneratedKeyColumns("student_id");

        return jdbcInsert;
    }
    @Bean
    public SimpleJdbcInsert taskJdbcInsert() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate());
        jdbcInsert.withTableName("tasks");
        jdbcInsert.usingGeneratedKeyColumns("task_id");

        return jdbcInsert;
    }
}
