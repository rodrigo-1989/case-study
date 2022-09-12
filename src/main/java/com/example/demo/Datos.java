package com.example.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Usuario;

public class Datos {
	public static final List<String> ROLES = Arrays.asList("ROLE_USER","ROLE_COMPRAS","ROLE_ADMIN");
	public static final Usuario USUARIO = new Usuario("1q2w3e4r5t","usuario","usuario",
			"password","email@email.com","Http://image",0,new Date(),true,ROLES, null);
	public static final List<Usuario> USUARIOS = Arrays.asList(USUARIO);
	public static final Optional<Usuario> OPUSUARIO = Optional.of(USUARIO); 

}
