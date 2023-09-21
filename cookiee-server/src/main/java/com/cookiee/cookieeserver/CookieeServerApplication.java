package com.cookiee.cookieeserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CookieeServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookieeServerApplication.class, args);
	}

}
