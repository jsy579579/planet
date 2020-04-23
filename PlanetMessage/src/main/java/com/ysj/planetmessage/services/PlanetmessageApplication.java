package com.ysj.planetmessage.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PlanetmessageApplication {

    public static void main(String[] args) {
        if (args.length == 0) args = new String[] { "--spring.profiles.active=dev" };
        SpringApplication.run(PlanetmessageApplication.class, args);
    }

}
