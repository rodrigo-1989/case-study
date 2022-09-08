package com.example.demo.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NuevoUsuario {

	@NotBlank(message = "El nombre es requerido")
	private String name;
	@NotBlank(message = "El usuario es requerido")
	@Size(min = 5,message = "Nombre de usuario debe tener mas o igual a 5 caracteres")
	private String username;
	
	@NotBlank(message = "La contraseña es requerida")
	@Size(min = 5, message = "La contraseña debe de tener un minimo de 5 caracteres")
	private String password;
	@NotBlank(message = "El correo es requerido")
	@Email(message = "El formato de correo no es valido")
	private String email;
}
