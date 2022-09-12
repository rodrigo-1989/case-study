package com.example.demo.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document( "usuarios")
public class Usuario {

	private String id;
	private String name;
	private String username;
	private String password;
	private String email;
	private String image;
	private int intentos;
	private Date createAt;
	private boolean enabled;
	private List<String>roles;
	
	@Transient
	private String IdImage;
}
