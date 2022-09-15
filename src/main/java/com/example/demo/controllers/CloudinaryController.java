package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.services.ImagenService;

@RestController
@RequestMapping("/cloudinary")
public class CloudinaryController {

	@Autowired
	private ImagenService service;

	@DeleteMapping("/deleteImagen/{id}")
	public RespuestaDto deleteImagen(@PathVariable String id){
		return service.eliminarI(id);
	}
	
}
