package com.hitszplaza.background;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
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

	@Bean
	public ServletWebServerFactory servletContainer() {
		TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
		tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
		return tomcat;
	}

	private Connector createHTTPConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		//同时启用http（8080）、https（8443）两个端口
		connector.setScheme("http");
		connector.setSecure(false);
		connector.setPort(8080);
		connector.setRedirectPort(8443);
		return connector;
	}

}
