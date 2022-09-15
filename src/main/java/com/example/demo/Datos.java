package com.example.demo;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Imagen;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;

public class Datos {
	public static final List<String> ROLES = Arrays.asList("ROLE_USER","ROLE_COMPRAS","ROLE_ADMIN");
	public static final Usuario USUARIO = new Usuario("12345qwert","usuario","usuario",
			"password","email1@email.com","Http://image",0,new Date(),true,ROLES, null);
	public static final Usuario USUARIO2 = new Usuario("67890yuiop","usuario2","usuario2",
			"password2","email2@email.com","Http://image",0,new Date(),true,ROLES, null);
	public static final List<Usuario> USUARIOS = Arrays.asList(USUARIO,USUARIO2);
	public static final Optional<Usuario> OPUSUARIO = Optional.ofNullable(USUARIO);
	public static final Imagen IMAGEN = new Imagen("12345asdfg","imagen.jpg","Http://image","image","usuarioId");
	public static final Producto PRODUCTO = new Producto();
	public static final Producto PRODUCTO2 = new Producto();
	public static final Optional<Producto> OPPRODUCTO = Optional.ofNullable(PRODUCTO);
	
}
