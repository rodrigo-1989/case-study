package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.entity.Rol;

public interface RoleRepository extends MongoRepository<Rol, String>{

}
