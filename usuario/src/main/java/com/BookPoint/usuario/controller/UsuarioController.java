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
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> getUsuarios(){
        List<Usuario> usuarios = usuarioService.listarUsuarios();
        if(usuarios.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    
    }

    @PostMapping
    public ResponseEntity<Usuario> postUsuario(@RequestBody Usuario usuario){
        Usuario nuevo;
        try{
            nuevo = usuarioService.guardarUsuario(usuario);
            return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long id){
        Usuario usuario = usuarioService.findById(id).orElse(null);
        if(usuario == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @GetMapping("/correo/{emailCliente}")
    public ResponseEntity<?> findByEmailCliente(@PathVariable String emailCliente) {
    Usuario buscado = usuarioService.findByEmailCliente(emailCliente);
    if (buscado == null) {
        return new ResponseEntity<>("Usuario con correo " + emailCliente + " no existe", HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(buscado, HttpStatus.OK);
}
}
