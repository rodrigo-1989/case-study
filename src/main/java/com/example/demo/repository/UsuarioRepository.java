package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Usuario;


public interface UsuarioRepository extends MongoRepository<Usuario, String> {
	
	public Usuario findByUsername(String username);
	public Usuario findByEmail(String username);

}
