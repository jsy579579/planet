package com.ysj.planet.planetconfig.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
//各类配置文件可放github或者其他远程仓库进行访问
@SpringBootApplication
@EnableConfigServer
public class PlanetconfigApplication {

    public static void main(String[] args) {
        if (args.length == 0) args = new String[] { "--spring.profiles.active=dev,native" };
        SpringApplication.run(PlanetconfigApplication.class, args);
    }

}
