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
	@GetMapping("/pedidosPorUsuario/{id}")
	public RespuestaDto pedidosPorUsusario(@PathVariable String id) {
		return service.pedidosPorUsuario(id);
	}
	@GetMapping("/detalle/{id}")
	public RespuestaDto verDetallePedido(@PathVariable String id) {
		return service.pedidoDetallado(id);
	}
	
	@PutMapping("/{id}")
	public RespuestaDto entregarPedido(@PathVariable String id) {
		return service.entragarPedido(id);
	}
	
	@PutMapping("cancelar/{id}")
	public RespuestaDto acncelarPedido(@PathVariable String id) {
		return service.cancelarPedido(id);
	}
}
