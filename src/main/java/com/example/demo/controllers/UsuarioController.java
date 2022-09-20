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
		return service.listarTodos();
	}

	@GetMapping("/{id}")
	public RespuestaDto listarUno(@PathVariable String id) {
		return service.listarUno(id);
	}

	@PostMapping("/crear")
	public RespuestaDto crear(@RequestBody @Valid NuevoUsuario user, BindingResult result) {
		if (result.hasErrors())
			return procesarErrores(result);
		Usuario usuario = new Usuario();
		usuario.setName(user.getName());
		usuario.setUsername(user.getUsername());
		usuario.setPassword(user.getPassword());
		usuario.setEmail(user.getEmail());
		return service.crear(usuario);
	}

	@PutMapping("/{id}")
	public RespuestaDto editar(@PathVariable String id, @RequestBody @Valid EditarUsuario usuario,
			BindingResult result) {
		if (result.hasErrors())
			return procesarErrores(result);
		return service.editar(id, usuario);
	}

	@DeleteMapping("/{id}")
	public RespuestaDto eliminar(@PathVariable String id) {
		return service.eliminar(id);
	}

	@PutMapping("/editarRoles/{id}")
	public RespuestaDto editarRoles(@RequestBody EditarUsuario user, @PathVariable String id) {
		Usuario usuario = new Usuario();
		usuario.setRoles(user.getRoles());
		return service.editarRoles(usuario, id);
	}

	private RespuestaDto procesarErrores(BindingResult binding) {
		List<String> errores = new ArrayList<>();
		for (FieldError err : binding.getFieldErrors())
			errores.add("El campo:" + err.getField() + ", " + err.getDefaultMessage());
		return new RespuestaDto(false, "Lo siento, hay errores en la petición", null, null, errores, null, null, null,
				null);
	}
}
