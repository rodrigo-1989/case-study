package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;

import lombok.Data;

@Data
public class RespuestaDto {

	private boolean ok;
	private String mensaje;
	private List<Producto> productos;
	private Producto producto;
	private List<String> errores;
	private List<Usuario> usuarios;
	private Usuario usuario;
	
	public RespuestaDto() {}

	public RespuestaDto(boolean ok, String mensaje, List<Producto> productos, Producto producto, List<String> errores,
			List<Usuario> usuarios, Usuario usuario) {
		this.ok = ok;
		this.mensaje = mensaje;
		this.productos = productos;
		this.producto = producto;
		this.errores = errores;
		this.usuarios = usuarios;
		this.usuario = usuario;
	}
	
	
}
