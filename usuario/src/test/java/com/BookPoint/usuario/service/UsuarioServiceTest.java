package com.BookPoint.usuario.service;

import com.BookPoint.usuario.model.Usuario;
import com.BookPoint.usuario.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void testGuardarUsuario(){
        Usuario usuario = new Usuario(null, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");
        Usuario guardada = new Usuario(1L, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");

        when(usuarioRepository.save(usuario)).thenReturn(guardada);

        Usuario resultado = usuarioService.guardarUsuario(usuario);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        assertEquals("juan.perez@example.com", resultado.getEmailCliente());
        assertEquals("password123", resultado.getPassword());
        assertEquals("USER", resultado.getRol());

        verify(usuarioRepository, times(1)).save(usuario);

    }

    @Test
    void testListarUsuarios(){
        Usuario u1 = new Usuario(1L, "Juan", "Perez", "juan.perez@example.com", "password123", "USER");
        Usuario u2 = new Usuario(2L, "Maria", "Gomez", "maria.gomez@example.com", "password456", "USER");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(u1, u2));
        List<Usuario> resultado = usuarioService.listarUsuarios();

        assertEquals(2, resultado.size());
        assertEquals("Juan", resultado.get(0).getNombre());
        assertEquals("Maria", resultado.get(1).getNombre());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById(){
        Usuario usuario = new Usuario(1L, "Juan", "Perez", "juan.perez@gmail.com", "password123", "USER");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        Optional<Usuario> resultado = usuarioService.findById(1L);
        
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getIdUsuario());
        assertEquals("Juan", resultado.get().getNombre());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNoExistente(){
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Usuario> resultado = usuarioService.findById(99L);

        assertFalse(resultado.isPresent());

        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    void testActualizarUsuario(){
        Usuario existente = new Usuario(1L, "Juan", "Perez", "juan.perez@gmail.com", "password123", "USER");
        Usuario datosNuevos = new Usuario(null, "Juan", "Perez", "juan.perez.actualizado@gmail.com", "password456", "USER");
        Usuario actualizado = new Usuario(1L, "Juan", "Perez", "juan.perez.actualizado@gmail.com", "password456", "USER");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(actualizado);

        Usuario resultado = usuarioService.actualizarUsuario(1L, datosNuevos);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        assertEquals("juan.perez.actualizado@gmail.com", resultado.getEmailCliente());
        assertEquals("password456", resultado.getPassword());
        assertEquals("USER", resultado.getRol());

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(existente);
    }

    @Test
    void testActualizarUsuarioNoExistente(){
        Usuario datosNuevos = new Usuario(null, "Juan", "Perez", "juan.perez.actualizado@gmail.com", "password456", "USER");
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> usuarioService.actualizarUsuario(99L, datosNuevos));

        assertEquals("Usuario no encontrado con id: ", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99L);
        verify(usuarioRepository, never()).save(any(Usuario.class));        
    }

    @Test
    void testEliminarUsuario(){
        doNothing().when(usuarioRepository).deleteById(1L);
        usuarioService.eliminarUsuario(1L);
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindByEmailCliente() {
        Usuario existente = new Usuario(1L, "Juan", "Perez", "juan@gmail.com", "pass123", "USER");
        when(usuarioRepository.findByEmailCliente("juan@gmail.com"))
                .thenReturn(Optional.of(existente));

        Usuario resultado = usuarioService.findByEmailCliente("juan@gmail.com");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdUsuario());
        assertEquals("Juan", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());
        assertEquals("juan@gmail.com", resultado.getEmailCliente());
        assertEquals("pass123", resultado.getPassword());
        assertEquals("USER", resultado.getRol());
    }

    @Test
    void testFindByEmailClienteCuandoNoExiste() {
        when(usuarioRepository.findByEmailCliente("noexiste@gmail.com"))
                .thenReturn(Optional.empty());

        Usuario resultado = usuarioService.findByEmailCliente("noexiste@gmail.com");

        assertNull(resultado);
    }
}

