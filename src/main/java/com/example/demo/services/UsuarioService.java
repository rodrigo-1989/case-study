package com.example.demo.services;

import java.util.List;
import java.util.Map;

import com.example.demo.entity.Usuario;

public interface UsuarioService {

	public List<Usuario> listarTodos();
	public Usuario listarUno(String id);
	public Usuario crear(Usuario usuario);
	public Usuario editar(String id, Usuario usuario);
	public Map<String, Object> eliminar(String id);
	
	public boolean existeCorreo(String correo);
	public boolean existeUsername(String username);
	
	public Usuario buscarPorUsuario(String username);
	
	public Usuario editarLogin(String id, Usuario usuario);
}
