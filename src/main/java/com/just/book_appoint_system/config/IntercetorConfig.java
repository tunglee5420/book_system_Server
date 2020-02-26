package com.just.book_appoint_system.config;



import com.just.book_appoint_system.filter.OnlineIntercepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 */
@Configuration
public class IntercetorConfig implements WebMvcConfigurer {

    @Autowired
    private OnlineIntercepter onlineIntercepter;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(onlineIntercepter).addPathPatterns("/api/online/**");
//        registry.addInterceptor(new OfficeAdminIntercepter()).addPathPatterns("/api/online/officeAdmin/**");
//        registry.addInterceptor(new CollegeAdminIntercepter()).addPathPatterns("/api/online/collegeAdmin/**");
//        registry.addInterceptor(new RootIntercepter()).addPathPatterns("/api/online/root/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
