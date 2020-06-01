package com.githut.lgsxiaosen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class IoUtilsApplication {

	public static void main(String[] args) {
		SpringApplication.run(IoUtilsApplication.class, args);
	}

}
