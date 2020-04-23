package com.ysj.zuulplanet;

import com.ysj.zuulplanet.filters.pre.AuthenticationHeaderFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;



@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class PlanetgatewatApplication extends WebMvcConfigurationSupport {

    public static void main(String[] args) {
        if (args.length == 0) args = new String[] { "--spring.profiles.active=dev" };
        SpringApplication.run(PlanetgatewatApplication.class, args);
    }
    @Bean
    public AuthenticationHeaderFilter authenticationHeadFilter() {
        return new AuthenticationHeaderFilter();
    }


    @Bean
    public FilterRegistrationBean characterEncodingFilter() {
        FilterRegistrationBean filter=new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter =new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        filter.setFilter(characterEncodingFilter);
        return filter;
    }

    @Bean
    public CorsFilter corsFilter(){

        final UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config=new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedHeader("*");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        config.addAllowedOrigin("*");
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);

    }
}
