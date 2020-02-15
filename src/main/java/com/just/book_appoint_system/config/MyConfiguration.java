package com.just.book_appoint_system.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration

@ControllerAdvice
public class MyConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
                builder.serializationInclusion(JsonInclude.Include.NON_NULL);
                ObjectMapper objectMapper = builder.build();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                objectMapper.registerModule(simpleModule);
                objectMapper.configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true);// 忽略 transient 修饰的属性
                converters.add(new MappingJackson2HttpMessageConverter(objectMapper));
                super.configureMessageConverters(converters);
            }

            //这里配置对前端跨域的支持
            /**
             * 跨域支持
             * @param registry
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowCredentials(true)
                        .allowedMethods("GET", "POST", "DELETE", "PUT")
                        .maxAge(3600);
            }
        };
    }

}
