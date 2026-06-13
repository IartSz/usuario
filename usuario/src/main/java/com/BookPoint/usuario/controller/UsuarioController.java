package com.BookPoint.usuario.controller;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import com.BookPoint.usuario.model.Usuario;
import com.BookPoint.usuario.service.UsuarioService;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
        @Autowired
        private UsuarioService usuarioService;

        @GetMapping
        public CollectionModel<EntityModel<Usuario>> getUsuarios() {
                List<Usuario> usuarios = usuarioService.listarUsuarios();
                List<EntityModel<Usuario>> usuariosConLinks = usuarios.stream()
                                .map(usuario -> EntityModel.of(usuario,
                                                linkTo(methodOn(UsuarioController.class)
                                                                .getUsuario(usuario.getIdUsuario())).withSelfRel()))
                                .toList();
                return CollectionModel.of(usuariosConLinks,
                                linkTo(methodOn(UsuarioController.class).getUsuarios()).withSelfRel());
        }

        @PostMapping
        public EntityModel<Usuario> postUsuario(@RequestBody Usuario usuario) {
                Usuario nuevo = usuarioService.guardarUsuario(usuario);
                return EntityModel.of(nuevo,
                                linkTo(methodOn(UsuarioController.class).getUsuario(nuevo.getIdUsuario()))
                                                .withSelfRel(),
                                linkTo(methodOn(UsuarioController.class).getUsuarios()).withRel("usuarios"));
        }

        @GetMapping("/{id}")
        public ResponseEntity<EntityModel<Usuario>> getUsuario(@PathVariable Long id) {
                return usuarioService.findById(id)
                                .map(usuario -> ResponseEntity.ok(EntityModel.of(usuario,
                                                linkTo(methodOn(UsuarioController.class).getUsuario(id)).withSelfRel(),
                                                linkTo(methodOn(UsuarioController.class).getUsuarios())
                                                                .withRel("usuarios"))))
                                .orElse(ResponseEntity.notFound().build());
        }

        @GetMapping("/correo/{emailCliente}")
        public ResponseEntity<EntityModel<Usuario>> getUsuarioPorCorreo(@PathVariable String emailCliente) {
                Usuario usuario = usuarioService.findByEmailCliente(emailCliente);
                if (usuario == null) {
                        return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(EntityModel.of(usuario,
                                linkTo(methodOn(UsuarioController.class).getUsuarioPorCorreo(emailCliente)).withSelfRel(),
                                linkTo(methodOn(UsuarioController.class).getUsuarios()).withRel("usuarios")));
        }

        @PutMapping("/{id}")
        public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(@PathVariable Long id,
                        @RequestBody Usuario usuario) {
                try {
                        Usuario actualizado = usuarioService.actualizarUsuario(id, usuario);
                        return ResponseEntity.ok(EntityModel.of(actualizado,
                                        linkTo(methodOn(UsuarioController.class).getUsuario(id)).withSelfRel(),
                                        linkTo(methodOn(UsuarioController.class).getUsuarios()).withRel("usuarios")));
                } catch (RuntimeException e) {
                        return ResponseEntity.notFound().build();
                }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
                usuarioService.eliminarUsuario(id);
                return ResponseEntity.noContent().build();
        }
}