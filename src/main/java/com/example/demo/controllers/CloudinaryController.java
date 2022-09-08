package com.example.demo.controllers;

//import java.awt.image.BufferedImage;
//import java.io.IOException;

//import javax.imageio.ImageIO;

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
	
//	@PostMapping("/uploadImagenUsuario/{id}")
//	public RespuestaDto upload(@RequestParam MultipartFile file,@PathVariable String id) throws IOException{
//		BufferedImage bi = ImageIO.read(file.getInputStream());
//		if(bi == null) 
//			return new RespuestaDto(false,"Imagen de usuario NO valida",null,null,null,null,null);
//		return service.guardarIU(file,id);
//	}
//	
//	@PostMapping("/uploadImagenProducto/{id}")
//	public RespuestaDto uploadImagenProducto(@RequestParam MultipartFile file,@PathVariable String id) throws IOException{
//		BufferedImage bi = ImageIO.read(file.getInputStream());
//		if(bi == null) 
//			return new RespuestaDto(false,"Imagen de producto NO valida",null,null,null,null,null);
//		return service.guardarIP(file,id);
//	}

	@DeleteMapping("/deleteImagen/{id}")
	public RespuestaDto deleteImagen(@PathVariable String id){
		return service.eliminarI(id);
	}
	
}
