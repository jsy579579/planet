package com.ysj.planet.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;

/**
 * @ClassName PlanetErukaApplication
 * @Description TODO
 * @Author com.ysj
 * @Date 2020/4/17 17:09
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class PlanetErukaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlanetErukaApplication.class, args);
    }


    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
