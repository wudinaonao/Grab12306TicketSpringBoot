package com.naonao.grab12306ticket.version.springboot.configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @program: SpringBoot
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-24 14:46
 **/
@Configuration
public class DruidConfiguration {

    /**
     * registered a servlet
     * @return  ServletRegistrationBean
     */
    @Bean
    public ServletRegistrationBean druidStatViewServle(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        // allow list
        servletRegistrationBean.addInitParameter("allow","127.0.0.1");
        // deny list, if ip simultaneously exist in deny and allow list, then deny priority
        servletRegistrationBean.addInitParameter("deny","192.168.1.73");
        // login to view the account, password of the information.
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","123456");
        // is it possible to reset the data?
        servletRegistrationBean.addInitParameter("resetEnable","false");
        return servletRegistrationBean;
    }

    /**
     * registered a filterRegistrationBean
     * @return  FilterRegistrationBean
     */
    @Bean
    public FilterRegistrationBean druidStatFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        // add a filter rule.
        filterRegistrationBean.addUrlPatterns("/*");
        // add formatting information that you don't need to ignore.
        filterRegistrationBean.addInitParameter("exclusions","*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }
}
