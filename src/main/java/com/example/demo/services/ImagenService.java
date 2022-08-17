package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.RespuestaDto;

public interface ImagenService {

	public RespuestaDto guardarIU(MultipartFile file, String idU);
	public RespuestaDto eliminarIU(String id);
	
	public RespuestaDto guardarIP(MultipartFile file, String idP);
	public RespuestaDto eliminarIP(String id);
}
