package com.just.book_appoint_system.config;


import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * mybatis 分页插件配置
 */
@Configuration
public class MybatisConfig {
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper= new PageHelper();
        Properties p=new Properties();
        p.setProperty("offsetAsPageNum","true");
        p.setProperty("rowBoundWithCount","true");
        p.setProperty("reasonable","true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

}
