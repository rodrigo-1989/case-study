package com.example.demo.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EditarUsuario {
	
	@NotBlank
	@Size(min = 5,message = "Nombre debe deser mayor o igual a 5 caracteres")
	private String name;
	private String password;
	private String image;
	private String IdImage;
	private List<String>roles;
}
