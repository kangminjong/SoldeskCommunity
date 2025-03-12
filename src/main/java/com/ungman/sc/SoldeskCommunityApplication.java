package com.ungman.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SoldeskCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoldeskCommunityApplication.class, args);
	}

}
