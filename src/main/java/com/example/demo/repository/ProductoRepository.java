package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Producto;

public interface ProductoRepository extends MongoRepository<Producto, String>{

	Producto findByNombre(String nombre);
}
