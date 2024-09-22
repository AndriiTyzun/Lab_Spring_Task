package ua.laboratory.lab_spring_task.Config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import ua.laboratory.lab_spring_task.Model.Trainee;
import ua.laboratory.lab_spring_task.Model.Trainer;
import ua.laboratory.lab_spring_task.Model.Training;
import ua.laboratory.lab_spring_task.Model.User;

import javax.sql.DataSource;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = "ua.laboratory.lab_spring_task")
public class AppConfig {
    @Bean
    public SessionFactory sessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addResource("mappings/User.hbm.xml");
        configuration.addResource("mappings/Trainee.hbm.xml");
        configuration.addResource("mappings/Trainer.hbm.xml");
        configuration.addResource("mappings/Training.hbm.xml");
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Trainee.class);
        configuration.addAnnotatedClass(Trainer.class);
        configuration.addAnnotatedClass(Training.class);

        Properties hibernateProperties = new Properties();
        hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.put("hibernate.show_sql", "true");
        hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
        configuration.setProperties(hibernateProperties);

        return configuration.buildSessionFactory();
    }
}
