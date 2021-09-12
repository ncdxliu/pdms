package com.dnliu.pdms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dnliu.pdms.dao")
public class PdmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdmsApplication.class, args);
	}

}
