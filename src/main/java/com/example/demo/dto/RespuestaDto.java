package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Pedido;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaDto {

	private boolean ok;
	private String mensaje;
	private List<Producto> productos;
	private Producto producto;
	private List<String> errores;
	private List<Usuario> usuarios;
	private Usuario usuario;
	private List<Pedido> pedidos;
	private Pedido pedido;
	
}
