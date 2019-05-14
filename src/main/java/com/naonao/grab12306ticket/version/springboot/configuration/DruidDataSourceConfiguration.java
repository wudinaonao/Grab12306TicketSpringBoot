package com.naonao.grab12306ticket.version.springboot.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.beans.ConstructorProperties;

/**
 * @program: grab12306ticket
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-14 15:52
 **/
@Configuration
public class DruidDataSourceConfiguration {

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }
}
