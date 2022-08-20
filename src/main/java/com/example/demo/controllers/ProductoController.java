package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.demo.dto.CVProductos;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Producto;
import com.example.demo.services.ProductoService;

@RestController
@RequestMapping(value = "/productos")
@CrossOrigin
public class ProductoController {

	@Autowired
	private ProductoService service;

	@GetMapping
	public RespuestaDto listar() {
		return service.listarTodos();
	}

	@GetMapping("/{id}")
	public RespuestaDto listarUno(@PathVariable String id) {
		return service.listarUno(id);
	}

	@PostMapping
	public RespuestaDto crear(@RequestBody @Valid Producto producto, BindingResult binding) {
		if (binding.hasErrors())
			return procesarErrores(binding);
		return service.crear(producto);
	}

	@PutMapping("/{id}")
	public RespuestaDto editar(@PathVariable String id, @RequestBody @Valid Producto producto, BindingResult binding) {
		if (binding.hasErrors())
			return procesarErrores(binding);
		return service.editar(id, producto);
	}

	@DeleteMapping("/{id}")
	public RespuestaDto eliminar(@PathVariable String id) {
		return service.eliminar(id);
	}

	@PutMapping("/compraTienda")
	public RespuestaDto comprar(@RequestBody List<CVProductos> lista) {
		return service.comprar(lista);
	}

	@PostMapping("/compraUsuario")
	public RespuestaDto vender(@RequestBody List<CVProductos> lista) {
		return service.vender(lista);
	}

	private RespuestaDto procesarErrores(BindingResult binding) {
		RespuestaDto respuesta = new RespuestaDto();
		respuesta.setOk(false);
		List<String> errores = new ArrayList<String>();

		for (FieldError err : binding.getFieldErrors()) {
			errores.add(err.getDefaultMessage());
		}
		respuesta.setErrores(errores);
		return respuesta;
	}
}
