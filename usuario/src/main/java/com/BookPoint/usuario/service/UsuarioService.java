package com.BookPoint.usuario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BookPoint.usuario.model.Usuario;
import com.BookPoint.usuario.repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id){
        return usuarioRepository.findById(id);
    }

    public Usuario findByEmailCliente(String emailCliente) {
    Usuario buscado = usuarioRepository.findByEmailCliente(emailCliente).orElse(null);
    if (buscado == null) return null;

    Usuario dto = new Usuario();
    dto.setIdUsuario(buscado.getIdUsuario());
    dto.setNombre(buscado.getNombre());
    dto.setApellido(buscado.getApellido());
    dto.setEmailCliente(buscado.getEmailCliente());
    dto.setPassword(buscado.getPassword());
    dto.setRol(buscado.getRol());

    return dto;
}
    public void eliminarUsuario(Long id){
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuario) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: "));
        
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setEmailCliente(usuario.getEmailCliente());
        existente.setPassword(usuario.getPassword());
        existente.setRol(usuario.getRol());

        return usuarioRepository.save(existente);
    }
}
