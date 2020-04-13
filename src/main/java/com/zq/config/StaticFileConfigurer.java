package com.zq.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by 86132 on 2020/01/23.
 * 静态文件配置类
 */
@SpringBootConfiguration
public class StaticFileConfigurer implements WebMvcConfigurer{

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/myimg/**").addResourceLocations("file:"+"d:/img/");
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/myimg/**").addResourceLocations("file:"+"/www/wwwroot/zqain.xyz/img/");
//    }
}
