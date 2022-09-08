package com.example.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NuevoProducto {

	@NotEmpty(message = "El nombre del producto es necesario")
	private String nombre;
	@NotNull(message = "El precio del producto es necesario, si lo desconoce puede ser 0")
	private Double precio;
	@NotEmpty(message = "La descripcion es importante, puede ser la marca")
	private String descripcion;
	private String imagen;
	private String idImagen;
	private int existentes;
}
