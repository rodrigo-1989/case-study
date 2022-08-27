package com.example.demo.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document( "usuarios")
public class Usuario {

	private String id;
	@NotBlank(message = "El nombre es requerido")
	private String name;
	@NotBlank(message = "El usuario es requerido")
	@Min(value = 6,message = "El nombre de usuario debe de tener un minimo de 6 caracteres")
	private String username;
	@NotBlank(message = "La contraseña es requerida")
	@Min(value = 6,message = "La contraseña debe de tener un minimo de 6 caracteres")
	private String password;
	@NotBlank(message = "El correo es requerido")
	@Email(message = "El formato de correo no es valido")
	private String email;
	private String image;
	private int intentos;
	private Date createAt;
	private boolean enabled;
	private List<String>roles;
	
	@Transient
	private String IdImage;
}
