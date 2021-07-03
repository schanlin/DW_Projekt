package backend;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@EnableScheduling
public class SpringJdbcConfig {
    @Bean
    public DataSource mysqlDataSource(DatabaseProperties datenbank) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
        dataSource.setUrl(datenbank.url);
        dataSource.setUsername(datenbank.user);
        dataSource.setPassword(datenbank.password);

        return dataSource;
    }

    @Bean
    public JdbcTemplate template(DataSource source) {
        return new JdbcTemplate(source);
    }
}
