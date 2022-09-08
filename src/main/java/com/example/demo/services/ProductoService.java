package com.example.demo.services;

import java.util.List;

import com.example.demo.dto.CVProductos;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Producto;

public interface ProductoService {
	
	public RespuestaDto listarTodos();
	public RespuestaDto listarUno(String id);
	public RespuestaDto listarParecidos(String nombre);
	public RespuestaDto crear(Producto producto);
	public RespuestaDto editar(String id, Producto producto);
	public RespuestaDto eliminar(String id);
	public RespuestaDto actualizarExistentes(List<CVProductos> lista);
	public RespuestaDto editarUno(CVProductos producto);
	public RespuestaDto vender(List<CVProductos> lista);
	
}
