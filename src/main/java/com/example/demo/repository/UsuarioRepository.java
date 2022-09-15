package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Usuario;


public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
	public Usuario findByUsernameIgnoreCase(String username);
	public Usuario findByEmailIgnoreCase(String email);

}
