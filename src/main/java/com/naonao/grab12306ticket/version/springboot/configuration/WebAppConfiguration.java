// package com.naonao.grab12306ticket.version.springboot.configuration;
//
// import com.naonao.grab12306ticket.version.springboot.interceptor.ErrorInterceptor;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// /**
//  * @program: SpringBoot
//  * @description:
//  * @author: Wen lyuzhao
//  * @create: 2019-05-26 12:22
//  **/
// @Configuration
// public class WebAppConfiguration implements WebMvcConfigurer {
//     @Override
//     public void addInterceptors(InterceptorRegistry registry) {
//         // 多个拦截器组成一个拦截器链
//         // addPathPatterns 用于添加拦截规则
//         // excludePathPatterns 用户排除拦截
//         registry.addInterceptor(new ErrorInterceptor()).addPathPatterns("/**");
//         // super.addInterceptors(registry);
//     }
// }
