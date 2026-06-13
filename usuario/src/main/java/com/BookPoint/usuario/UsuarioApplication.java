package com.BookPoint.usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = org.springdoc.core.configuration.SpringDocHateoasConfiguration.class)
public class UsuarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
	}

}
