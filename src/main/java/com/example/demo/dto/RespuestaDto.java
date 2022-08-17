package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Producto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaDto {

	private boolean ok;
	private String mensaje;
	private List<Producto> productos;
	private Producto producto;
	private List<String> errores;
	

	public RespuestaDto() {}

	public RespuestaDto(boolean ok, List<String> errores) {
		this.ok = ok;
		this.errores = errores;
	}
	public RespuestaDto(boolean ok, String mensaje) {
		this.ok = ok;
		this.mensaje = mensaje;
	}
	public RespuestaDto(boolean ok, String mensaje, List<Producto> productos) {
		this.ok = ok;
		this.mensaje = mensaje;
		this.productos = productos;
	}
	public RespuestaDto(boolean ok, String mensaje, Producto producto) {
		this.ok = ok;
		this.mensaje = mensaje;
		this.producto = producto;
	}

}
