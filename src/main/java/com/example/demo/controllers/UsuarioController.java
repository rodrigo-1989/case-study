package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.EditarUsuario;
import com.example.demo.entity.Usuario;
import com.example.demo.services.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class UsuarioController {

	@Autowired
	private UsuarioService service;
	
	@GetMapping
	public ResponseEntity<?> listarTodos(){
		List<Usuario> usuarios = service.listarTodos();
		if(usuarios.size()==0)
			return respuesta(false,"No se encontro ningun usuario",null,"msg",HttpStatus.NOT_FOUND);
		return respuesta(true,null,usuarios,"usuarios",HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> listarUno(@PathVariable String id){
		Usuario usuario = service.listarUno(id);
		if(usuario == null)
			return respuesta(false,"No se encontro el usuario o algo salio mal",null,"msg",HttpStatus.NOT_FOUND);
		return respuesta(true,null,usuario,"usuario",HttpStatus.OK);
	}
	
	@PostMapping("/crear")
	public ResponseEntity<?>  crear (@RequestBody @Valid Usuario usuario,BindingResult result){
		if(result.hasErrors())
			return procesarErrores(result);
		
		if(service.existeCorreo(usuario.getEmail()))
			return respuesta(false,"El correo ya esta en uso",null,"msg",HttpStatus.CONFLICT);
		if(service.existeUsername(usuario.getUsername() ))
			return respuesta(false,"El usuario ya esta en uso",null,"msg",HttpStatus.CONFLICT);
		return respuesta(true,null,service.crear(usuario),"usuario",HttpStatus.OK);
	}
	@PutMapping("/{id}")
	public ResponseEntity<?>  editar (@PathVariable String id,@RequestBody @Valid EditarUsuario usuario,BindingResult result){
		if(result.hasErrors())
			return procesarErrores(result);
		return respuesta(true,null,service.editar(id,usuario),"usuario",HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable String id){
		return new ResponseEntity(service.eliminar(id),HttpStatus.OK);
	}
	@PutMapping("/editarRoles/{id}")
	public ResponseEntity<?> editarRoles(@RequestBody Usuario usuario,@PathVariable String id){
		Usuario u = service.editarRoles(usuario,id);
		if(u != null)
			return new ResponseEntity(u,HttpStatus.OK);
		else
			return new ResponseEntity("Id de usuario no encontrado en BD",HttpStatus.NOT_FOUND);
	}
	
	private ResponseEntity<?> respuesta(boolean status,String msg, Object objeto,String nombreObjeto, HttpStatus httpstatus){
		Map<String, Object> resp = new HashMap<>();
		resp.put("ok",status);
		if(status) 
			resp.put(nombreObjeto,objeto);
		else
			resp.put(nombreObjeto,msg);
		
		return new ResponseEntity(resp,httpstatus);
	}
	
	private ResponseEntity<?> procesarErrores(BindingResult binding) {
		List<String> errores = new ArrayList<String>();
		for (FieldError err : binding.getFieldErrors()) {
			errores.add(err.getDefaultMessage());
		}
		Map res = new HashMap<String, Object>();
		res.put("ok", false);
		res.put("errores",errores);
		return new ResponseEntity(res,HttpStatus.BAD_REQUEST);
	}
}
