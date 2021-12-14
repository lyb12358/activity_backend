package com.beyond.config;

import com.beyond.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Author lyb
 * @create 2019-04-10 13:32
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandler     是指你想在url请求的路径
        //addResourceLocations   是图片存放的真实路径
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0) {
            //FIXME 修复linux路径
            registry.addResourceHandler("/image/**").addResourceLocations("file:/home/activity_file/image/");
            registry.addResourceHandler("/music/**").addResourceLocations("file:/home/activity_file/music/");
            registry.addResourceHandler("/template/**").addResourceLocations("file:/home/activity_file/template/");
        } else if (System.getProperty("os.name").toLowerCase().indexOf("windows") >= 0) {
            registry.addResourceHandler("/image/**").addResourceLocations("file:D:/activity_file/image/");
            registry.addResourceHandler("/music/**").addResourceLocations("file:D:/activity_file/music/");
            registry.addResourceHandler("/template/**").addResourceLocations("file:D:/activity_file/template/");
        } else {
            registry.addResourceHandler("/image/**").addResourceLocations("file:/Users/lyb/Desktop/activity_file/image/");
            registry.addResourceHandler("/music/**").addResourceLocations("file:/Users/lyb/Desktop/activity_file/music/");
            registry.addResourceHandler("/template/**").addResourceLocations("file:/Users/lyb/Desktop/activity_file/template/");
        }
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}