package com.example.demo.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.demo.dto.CVProductos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document("pedidos")
public class Pedido {

	private String id;
	private String usuarioId;
	private List<CVProductos> listaCompra;
	private Date fechaCompra;
	private double total;
	private boolean status;

	public Pedido() {
		this.listaCompra = new ArrayList<>();
	}
	
	public void addCompra(CVProductos compra) {
		this.listaCompra.add(compra);
	}
	
	public void crearTotal() {
		this.total = 0.00;
		this.listaCompra.forEach(c -> this.total += c.getPrecio()*c.getCantidad());
	}
}
