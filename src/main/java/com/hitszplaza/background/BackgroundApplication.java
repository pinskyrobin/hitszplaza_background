package com.hitszplaza.background;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling
@SpringBootApplication
public class BackgroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackgroundApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(){
		//在SpringBoot启动类中注册RestTemplate
		return new RestTemplate();
	}

}
