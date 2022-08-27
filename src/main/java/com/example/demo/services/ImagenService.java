package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;

public interface ImagenService {

	public RespuestaDto guardarIU(MultipartFile file, String idU);
	public RespuestaDto editarIU(Usuario usuario, String id);
	
	public RespuestaDto guardarIP(MultipartFile file, String idP);
	public RespuestaDto editarIP(Producto producto, String id);

	public RespuestaDto eliminarI(String id);
}
