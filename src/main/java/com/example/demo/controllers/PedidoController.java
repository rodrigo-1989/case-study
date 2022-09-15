package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.services.PedidosService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidosService service;
	
	@GetMapping
	public RespuestaDto listar() {
		return service.pedidos();
	}
	
	@PutMapping("/{id}")
	public RespuestaDto entregarPedido(@PathVariable String id) {
		return service.entragaPedido(id);
	}
}
