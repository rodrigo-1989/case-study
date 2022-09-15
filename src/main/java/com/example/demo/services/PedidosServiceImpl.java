package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Pedido;
import com.example.demo.repository.PedidoRepository;

@Service
public class PedidosServiceImpl implements PedidosService{

	@Autowired
	private PedidoRepository repository;
	
	@Override
	public RespuestaDto pedidos() {
		RespuestaDto respuesta = new RespuestaDto();
		respuesta.setOk(true);
		respuesta.setPedidos(repository.findByStatusIsTrue());
		return respuesta;
	}

	@Override
	public RespuestaDto entragaPedido(String idPedido) {
		Pedido pedido = repository.findById(idPedido).orElse(null);
		if(pedido == null) 
			return new RespuestaDto(false,"El id del pedido no existe",null,null,null,null,null,null);
		pedido.setStatus(false);
		repository.save(pedido);
		return new RespuestaDto(true,"Pedido entregado",null,null,null,null,null,null);
	}


}
