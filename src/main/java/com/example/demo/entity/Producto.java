package com.example.demo.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@Document( "productos")
public class Producto {

	@Id
	private String id;
	
	@NotEmpty(message = "El nombre del producto es necesario")
	private String nombre;
	@NotNull(message = "El precio del producto es necesario, si lo desconoce puede ser 0")
	private Double precio;
	@NotEmpty(message = "La descripcion es importante, puede ser la marca")
	private String descripcion;
	private String imagen;
	@Transient
	private String idImagen;
	private int existentes;
}
