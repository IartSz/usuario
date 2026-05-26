package com.BookPoint.usuario.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BookPoint.usuario.model.Usuario;
import com.BookPoint.usuario.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> getUsuarios(){
        try{
            List<Usuario> usuarios = usuarioService.listarUsuarios();
            if(usuarios.isEmpty()){
                return new ResponseEntity<>("No hay usuarios registrados", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Error al obtener usuarios", HttpStatus.CONFLICT);
        }
    }

    @PostMapping
    public ResponseEntity<?> postUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.guardarUsuario(usuario);
            if (nuevo == null) {
                return new ResponseEntity<>("No se pudo crear el usuario", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al crear el usuario", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUsuario(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.findById(id).orElse(null);
            if (usuario == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error al buscar usuario", HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/correo/{emailCliente}")
    public ResponseEntity<?> findByEmailCliente(@PathVariable String emailCliente) {
        try {
            Usuario buscado = usuarioService.findByEmailCliente(emailCliente);
            if (buscado == null) {
                return new ResponseEntity<>("Usuario con correo " + emailCliente + " no existe", HttpStatus.NOT_FOUND);
            }

        return new ResponseEntity<>(buscado, HttpStatus.OK);
    } catch(RuntimeException e){
        return new ResponseEntity<>("Error al buscar usuario por correo", HttpStatus.CONFLICT);
    }
    }
}
