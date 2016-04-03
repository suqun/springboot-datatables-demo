package com.larry.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Value("${spring.datasource.c3p0.driver-class-name}")
    String driverClassName;

    @Value("${spring.datasource.c3p0.jdbc-url}")
    String jdbcUrl;

    @Value("${spring.datasource.c3p0.username}")
    String username;

    @Value("${spring.datasource.c3p0.password}")
    String password;

    @Value("${spring.datasource.c3p0.min-evictable-idle-time-millis}")
    int idleTime;
    @Bean
    public ComboPooledDataSource dataSource() throws Exception
    {
        ComboPooledDataSource dataSource =  new ComboPooledDataSource();
        dataSource.setDriverClass(driverClassName);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setMaxIdleTime(idleTime);

        return dataSource;
    }
}
