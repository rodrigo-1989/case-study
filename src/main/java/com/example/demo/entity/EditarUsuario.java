package com.example.demo.entity;

import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EditarUsuario {
	
	@NotBlank
	@Size(min = 5,message = "Nombre debe deser mayor o igual a 5 caracteres")
	private String name;
//	@Pattern(regexp = "^[a-zA-Z0-9]+", message = "la contraseña debe contener por lo menos, una mayuscula, una minuscula y un numero")
	private String password;
	private String image;
	private String IdImage;
}
