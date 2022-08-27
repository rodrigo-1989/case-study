package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Imagen;

public interface ImagenRepository extends MongoRepository<Imagen, String>{
	Imagen findByImagenUrl (String url);
	Imagen findByProductoId (String id);
	Imagen findByUsuarioId (String id);
	void deleteByImagenUrl(String url);
}
