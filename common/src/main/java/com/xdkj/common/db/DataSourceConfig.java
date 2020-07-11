package com.xdkj.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration

@PropertySource("classpath:config/db.properties")
public class DataSourceConfig {
	
	/*@Bean(name = "x_master")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSource master(){
		return DataSourceBuilder.create().build();
	}*/

	/*@Bean(name = "x_slave")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.slave")
	public DataSource slave(){
		return DataSourceBuilder.create().build();
	}*/

    @Bean(name = "x_master")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

}
