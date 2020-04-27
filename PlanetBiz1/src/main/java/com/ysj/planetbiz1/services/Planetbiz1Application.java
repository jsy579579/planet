package com.ysj.planetbiz1.services;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
@EnableDiscoveryClient
public class Planetbiz1Application {

    public static void main(String[] args) {
        if (args.length == 0) args = new String[] { "--spring.profiles.active=dev" };
        SpringApplication.run(Planetbiz1Application.class, args);
    }
    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() throws Exception {
        String dataBaseName = env.getProperty("mysql.dataBaseName");
        Properties props = new Properties();
        props.put("driverClassName", env.getProperty("spring.datasource.driver-class-name"));
        props.put("url", env.getProperty("spring.datasource.url")+"/"+dataBaseName+"?useUnicode=true&characterEncoding=UTF-8");
        props.put("username", env.getProperty("spring.datasource.username"));
        props.put("password", env.getProperty("spring.datasource.password"));
        return DruidDataSourceFactory.createDataSource(props);

    }
}
