package com.example.demo.controllers;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.services.ImagenService;

@RestController
@CrossOrigin("*")
@RequestMapping("/cloudinary")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CloudinaryController {

	@Autowired
	private ImagenService service;
	
	@PostMapping("/uploadImagenUsuario/{id}")
	public ResponseEntity<?> upload(@RequestParam MultipartFile file,@PathVariable String id) throws IOException{
		BufferedImage bi = ImageIO.read(file.getInputStream());
		if(bi == null) 
			return new ResponseEntity(new RespuestaDto(false,"Imagen de usuario NO valida"),HttpStatus.BAD_REQUEST);
		return new ResponseEntity(service.guardarIU(file,id), HttpStatus.OK);
	}
	
	@PostMapping("/uploadImagenProducto/{id}")
	public ResponseEntity<?> uploadImagenProducto(@RequestParam MultipartFile file,@PathVariable String id) throws IOException{
		BufferedImage bi = ImageIO.read(file.getInputStream());
		if(bi == null) 
			return new ResponseEntity(new RespuestaDto(false,"Imagen de producto NO valida"),HttpStatus.BAD_REQUEST);
		return new ResponseEntity(service.guardarIP(file,id), HttpStatus.OK);
	}

	@DeleteMapping("/deleteImagen/{id}")
	public ResponseEntity<?> deleteImagen(@PathVariable String id) throws IOException{
		return new ResponseEntity(service.eliminarI(id), HttpStatus.OK);
	}
	
}
