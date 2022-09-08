package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@Document( "productos")
public class Producto {

	@Id
	private String id;
	private String nombre;
	private Double precio;
	private String descripcion;
	private String imagen;
	private int existentes;
	@Transient
	private String idImagen;
}
