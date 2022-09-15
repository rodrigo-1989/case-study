package com.example.demo.services;

import com.example.demo.dto.RespuestaDto;

public interface PedidosService {
	public RespuestaDto pedidos();
	public RespuestaDto entragaPedido(String idPedido);
}
