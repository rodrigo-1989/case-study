package com.example.demo.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CVProductos;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Pedido;
import com.example.demo.entity.Producto;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class PedidosServiceImpl implements PedidosService{

	@Autowired
	private PedidoRepository repository;
	@Autowired
	private ProductoRepository pRepository;
	@Autowired
	private UsuarioRepository uRepository;
	
	@Override
	@Transactional(readOnly = true)
	public RespuestaDto pedidos() {
		RespuestaDto respuesta = new RespuestaDto();
		respuesta.setOk(true);
		respuesta.setPedidos(repository.findByStatusIsTrue());
		return respuesta;
	}

	@Override
	@Transactional
	public RespuestaDto entragaPedido(String id) {
		Pedido pedido = repository.findById(id).orElse(null);
		if(pedido == null) 
			return new RespuestaDto(false,"El id del pedido no existe",null,null,null,null,null,null,null);
		pedido.setStatus(false);
		repository.save(pedido);
		return new RespuestaDto(true,"Pedido entregado",null,null,null,null,null,null,null);
	}

	@Override
	@Transactional(readOnly = true)
	public RespuestaDto pedidoDetallado(String id) {
		Pedido pedido = repository.findById(id).orElse(null);
		if(pedido == null) 
			return new RespuestaDto(false,"El id del pedido no existe",null,null,null,null,null,null,null);
		RespuestaDto respuesta = new RespuestaDto();
		respuesta.setOk(true);
		respuesta.setUsuario(uRepository.findById(pedido.getUsuarioId()).orElse(null));
		respuesta.setPedido(pedido);
		List<String> idLista = pedido.getListaCompra().stream().map(CVProductos::getIdProducto).collect(Collectors.toList());
		respuesta.setProductos((List<Producto>) pRepository.findAllById(idLista));
		return respuesta;
	}


}
