package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.EditarUsuario;
import com.example.demo.entity.Usuario;

public interface UsuarioService {

	public List<Usuario> listarTodos();
	public Usuario listarUno(String id);
	public Usuario crear(Usuario usuario);
	public Usuario editar(String id, EditarUsuario usuario);
	public Usuario eliminar(String id);
	
	public boolean existeCorreo(String correo);
	public boolean existeUsername(String username);
	
	public Usuario buscarPorUsuario(String username);
	public Usuario editarRoles(Usuario usuario,String id);
	
	public Usuario editarLogin(String id, Usuario usuario);
}
