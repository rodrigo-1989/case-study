package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.EditarUsuario;
import com.example.demo.dto.NuevoUsuario;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Usuario;
import com.example.demo.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@GetMapping
	public RespuestaDto listarTodos() {
		List<Usuario> usuarios = service.listarTodos();
		if (usuarios.isEmpty())
			return new RespuestaDto(false, "No se encontro ningun usuario", null, null, null, null, null,null);
		return new RespuestaDto(true, "Usuarios:", null, null, null, service.listarTodos(), null,null);
	}

	@GetMapping("/{id}")
	public RespuestaDto listarUno(@PathVariable String id) {
		Usuario usuario = service.listarUno(id);
		if (usuario == null)
			return new RespuestaDto(false, "No se encontro el usuario", null, null, null, null, null,null);
		return new RespuestaDto(true, "Usuario encontrado", null, null, null, null, usuario,null);
	}

	@PostMapping("/crear")
	public RespuestaDto crear(@RequestBody @Valid NuevoUsuario user, BindingResult result) {
		if (result.hasErrors())
			return procesarErrores(result);

		if (service.existeCorreo(user.getEmail()))
			return new RespuestaDto(false, "El correo ya esta en uso", null, null, null, null, null,null);
		if (service.existeUsername(user.getUsername()))
			return new RespuestaDto(false, "El usuario ya esta en uso", null, null, null, null, null,null);
		Usuario usuario = new Usuario();
		usuario.setName(user.getName());
		usuario.setUsername(user.getUsername());
		usuario.setPassword(user.getPassword());
		usuario.setEmail(user.getEmail());

		return new RespuestaDto(true, String.format("Usuario %s creado con exito", user.getName()), null, null, null,
				null, service.crear(usuario),null);
	}

	@PutMapping("/{id}")
	public RespuestaDto editar(@PathVariable String id, @RequestBody @Valid EditarUsuario usuario,
			BindingResult result) {
		if (result.hasErrors())
			return procesarErrores(result);
		Usuario u = service.editar(id, usuario);
		if (u == null)
			return new RespuestaDto(false,String.format("Id: %s de usuario NO existe",id),null,null,null,null,null,null);
		return new RespuestaDto(true,String.format("Usuario %s editado con exito",usuario.getName()),null,null,null,null,u,null);
	}

	@DeleteMapping("/{id}")
	public RespuestaDto eliminar(@PathVariable String id) {
		Usuario u = service.eliminar(id);
		if (u == null)
			return new RespuestaDto(false,String.format("Id: %s de usuario NO existe",id),null,null,null,null,null,null);
		return new RespuestaDto(true,"Usuario editado",null,null,null,null,u,null);
	}

	@PutMapping("/editarRoles/{id}")
	public RespuestaDto editarRoles(@RequestBody EditarUsuario user, @PathVariable String id) {
		if( user.getRoles() == null)
			return new RespuestaDto(false,"Faltan los roles del usuario",null,null,null,null,null,null);
		Usuario usuario = new Usuario();
		usuario.setRoles(user.getRoles());
		Usuario u = service.editarRoles(usuario, id);
		if (u == null)
			return new RespuestaDto(false,"El usuario que intentas editar no existe en BD.",null,null,null,null,null,null);
		return new RespuestaDto(true,String.format("Roles del usuario %s editados ",u.getName()),null,null,null,null,u,null);
	}

	private RespuestaDto procesarErrores(BindingResult binding) {
		List<String> errores = new ArrayList<>();
		for (FieldError err : binding.getFieldErrors())
			errores.add("El campo:" +err.getField()+", "+ err.getDefaultMessage());
		return new RespuestaDto(false, "Lo siento, hay errores en la petición", null, null, errores, null, null,null);
	}
}
