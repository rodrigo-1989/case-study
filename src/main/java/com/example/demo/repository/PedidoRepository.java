package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Pedido;

public interface PedidoRepository extends MongoRepository<Pedido, String>{

 	List<Pedido> findByStatusIsTrue();
}
