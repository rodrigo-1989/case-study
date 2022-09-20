package com.example.demo.services;

import com.example.demo.dto.EditarUsuario;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Usuario;

public interface UsuarioService {

	public RespuestaDto listarTodos();
	public RespuestaDto listarUno(String id);
	public RespuestaDto crear(Usuario usuario);
	public RespuestaDto editar(String id, EditarUsuario usuario);
	public RespuestaDto eliminar(String id);
	public RespuestaDto editarRoles(Usuario usuario,String id);
}
