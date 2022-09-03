package com.example.demo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document( "roles")
public class Rol {

	@Id
	private String id;
	private String rol;
}
