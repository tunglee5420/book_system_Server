package com.just.book_appoint_system;

import com.just.book_appoint_system.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;
import java.util.concurrent.CopyOnWriteArrayList;

@EnableAutoConfiguration
@EnableConfigurationProperties
@ServletComponentScan
@EnableTransactionManagement
@EnableScheduling
@ComponentScan("com.just.book_appoint_system")
@SpringBootApplication
@PropertySource(value = {"classpath:file.properties"})
public class BookAppointSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookAppointSystemApplication.class, args);
    }

    @Value("${file.uploadFolder}")
    private String uploadFolder;


    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setLocation(uploadFolder);
        return factory.createMultipartConfig();
    }


}
