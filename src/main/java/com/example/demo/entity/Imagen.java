package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document("imagenes")
@Data
public class Imagen {

	@Id
	private String id;
	private String name;
	private String imagenUrl;
	private String imagenId;
	private String usuarioId;
	private String productoId;
	public Imagen() {}
	public Imagen(String name, String imagenUrl, String imagenId, String usuarioId, String productoId) {
		this.name = name;
		this.imagenUrl = imagenUrl;
		this.imagenId = imagenId;
		this.usuarioId = usuarioId;
		this.productoId = productoId;
	}
	
}
