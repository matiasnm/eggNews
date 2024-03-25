package com.egg.noticias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class NoticiasApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoticiasApplication.class, args);
	}

}