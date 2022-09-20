package com.example.demo.services;

import com.example.demo.dto.RespuestaDto;

public interface PedidosService {
	public RespuestaDto pedidos();
	public RespuestaDto pedidoDetallado(String id);
	public RespuestaDto entragaPedido(String id);
}
