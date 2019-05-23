// package com.naonao.grab12306ticket.version.springboot.configuration;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// /**
//  * @program: SpringBoot
//  * @description:
//  * @author: Wen lyuzhao
//  * @create: 2019-05-18 00:32
//  **/
// @Configuration
// public class CorsConfig implements WebMvcConfigurer {
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 //是否发送Cookie
//                 .allowCredentials(true)
//                 //放行哪些原始域
//                 .allowedOrigins("*")
//                 .allowedMethods(new String[]{"GET", "POST", "PUT", "DELETE"})
//                 .allowedHeaders("*")
//                 .exposedHeaders("*");
//     }
// }
