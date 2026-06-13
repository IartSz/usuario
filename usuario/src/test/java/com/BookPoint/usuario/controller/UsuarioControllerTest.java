package com.BookPoint.usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.BookPoint.usuario.model.Usuario;
import com.BookPoint.usuario.service.UsuarioService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@ActiveProfiles("test")
public class UsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockitoBean
    private UsuarioService usuarioService;
    
    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUsuarios() throws Exception{
        Usuario u1 = new Usuario(1L, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");
        Usuario u2 = new Usuario(2L, "Maria", "Gomez", "maria.gomez@example.com", "password456", "USER");

        Mockito.when(usuarioService.listarUsuarios()).thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/api/v1/usuarios"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$._embedded.usuarioList", hasSize(2)))
               .andExpect(jsonPath("$._embedded.usuarioList[0].nombre", is("Juan")))
               .andExpect(jsonPath("$._embedded.usuarioList[1].apellido", is("Gomez")))
               .andExpect(jsonPath("$._links.self").exists());
    }

    @Test
    void testGuardarUsuario() throws Exception{
        Usuario nuevo = new Usuario(null, "Pedro", "Lopez", "p.lopez@gmail.com", "1234", "USER");
        Usuario guardado = new Usuario(2L, "Pedro", "Lopez", "p.lopez@gmail.com", "1234", "USER");

        Mockito.when(usuarioService.guardarUsuario(any(Usuario.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/usuarios")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(nuevo)))
               .andExpect(status().isOk())

               .andExpect(jsonPath("$.idUsuario").value(2L))
               .andExpect(jsonPath("$.nombre").value("Pedro"))
               .andExpect(jsonPath("$.apellido").value("Lopez"))
               .andExpect(jsonPath("$.emailCliente").value("p.lopez@gmail.com"))
               .andExpect(jsonPath("$.password").value("1234"))
               .andExpect(jsonPath("$.rol").value("USER"))

               .andExpect(jsonPath("$._links.self").exists())
               .andExpect(jsonPath("$._links.usuarios").exists());
    }

    @Test
    void testGetUsuarioById() throws Exception{
        Usuario buscado = new Usuario(2L, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");

        Mockito.when(usuarioService.findById(2L)).thenReturn(Optional.of(buscado));

        mockMvc.perform(get("/api/v1/usuarios/2"))
               .andExpect(status().isOk())

               .andExpect(jsonPath("$.idUsuario").value(2L))
               .andExpect(jsonPath("$.nombre").value("Juan"))
               .andExpect(jsonPath("$.apellido").value("Perez"))
               .andExpect(jsonPath("$.emailCliente").value("juan.perez@example.com"))
               .andExpect(jsonPath("$.password").value("password123"))
               .andExpect(jsonPath("$.rol").value("USER"))

               .andExpect(jsonPath("$._links.self").exists())
               .andExpect(jsonPath("$._links.usuarios").exists());
    }

    @Test
    void testGetUsuarioByIdNoExistente() throws Exception{
        Mockito.when(usuarioService.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/usuarios/99"))
                        .andExpect(status().isNotFound());
    }

    @Test
    void testFindByEmailClienteExistente() throws Exception {
        Usuario buscado = new Usuario(2L, "Juan", "Perez", "juan@correo.com", "password123", "USER");
        
        Mockito.when(usuarioService.findByEmailCliente("juan@correo.com")).thenReturn(buscado);

        mockMvc.perform(get("/api/v1/usuarios/correo/juan@correo.com"))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.idUsuario").value(2L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.emailCliente").value("juan@correo.com"))

                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.usuarios").exists());
    }

    @Test
    void testFindByEmailClienteNoExistente() throws Exception {
        Mockito.when(usuarioService.findByEmailCliente("noexiste@correo.com")).thenReturn(null);

        mockMvc.perform(get("/api/v1/usuarios/correo/noexiste@correo.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarUsuario() throws Exception{
        Usuario actualizado = new Usuario(1L, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");

        Mockito.when(usuarioService.actualizarUsuario(eq(1L), any(Usuario.class)))
                    .thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())

                .andExpect(jsonPath("$.idUsuario").value(1L))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Perez"))
                .andExpect(jsonPath("$.emailCliente").value("juan.perez@example.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.rol").value("USER"))

                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.usuarios").exists());
                   
    }

    @Test
    void testActualizarUsuarioNoExistente() throws Exception{
        Usuario usuario = new Usuario(null, "No", "Existe", "no.existe@correo.com", "1234", "USER");

        Mockito.when(usuarioService.actualizarUsuario(eq(99L), any(Usuario.class)))
                    .thenThrow(new RuntimeException("No existe el usuario"));
        
        mockMvc.perform(put("/api/v1/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarUsuario() throws Exception{
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/v1/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}
