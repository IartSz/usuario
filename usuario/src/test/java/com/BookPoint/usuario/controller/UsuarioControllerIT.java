package com.BookPoint.usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.BookPoint.usuario.model.Usuario;
import com.BookPoint.usuario.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UsuarioControllerIT {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private ObjectMapper objectMapper = new ObjectMapper();
    
    @BeforeEach
    void cleanDb(){
        usuarioRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerUsuario() throws Exception{
        Usuario usuario = new Usuario(null, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");

        mockMvc.perform(post("/api/v1/usuarios")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(usuario)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.idUsuario").exists())
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$._links").exists());
        
        mockMvc.perform(get("/api/v1/usuarios"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$._embedded.usuarioList[0].nombre").value("Juan"))
               .andExpect(jsonPath("$._embedded.usuarioList[0].apellido").value("Perez")) 
               .andExpect(jsonPath("$._embedded.usuarioList[0].emailCliente").value("juan.perez@example.com")) 
               .andExpect(jsonPath("$._embedded.usuarioList[0].password").value("password123")) 
               .andExpect(jsonPath("$._embedded.usuarioList[0].rol").value("USER"))
               
               .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testEliminarUsuario() throws Exception{
        Usuario usuario = new Usuario(null, "Maria", "Gomez", "maria.gomez@example.com", "password123", "USER");
        Usuario guardado = usuarioRepository.save(usuario);

        mockMvc.perform(delete("/api/v1/usuarios/" + guardado.getIdUsuario()))
               .andExpect(status().isNoContent());
        
        mockMvc.perform(get("/api/v1/usuarios/" + guardado.getIdUsuario()))
               .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarUsuario() throws Exception{
        Usuario usuario = new Usuario(null, "Carlos", "Lopez", "carlos.lopez@example.com", "password123", "USER");
        Usuario guardado = usuarioRepository.save(usuario);

        Usuario actualizado = new Usuario(null, "Carlos", "Lopez", "carlos.lopez@example.com", "password423", "USER");

        mockMvc.perform(put("/api/v1/usuarios/" + guardado.getIdUsuario())
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(actualizado)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.password").value("password423"))
               .andExpect(jsonPath("$._links").exists());
}
}
