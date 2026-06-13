package com.BookPoint.usuario;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class UsuarioApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainClassExist(){
		assertNotNull(UsuarioApplication.class);
	}

}
