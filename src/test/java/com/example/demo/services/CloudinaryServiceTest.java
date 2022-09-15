package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.dto.RespuestaDto;


class CloudinaryServiceTest {
	@Mock
	CloudinaryService service;
	
	RespuestaDto respuesta1;
	RespuestaDto respuesta2;
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		respuesta1 = new RespuestaDto();
		respuesta1.setOk(true);
		respuesta1.setMensaje("Imagen eliminada");
		respuesta2 = new RespuestaDto();
		respuesta2.setOk(false);
		respuesta2.setMensaje("El id de la imagen no existe");
	}
	
	@Test
	@DisplayName("Eliminar imagen de Cloudinary")
	void testEliminarI() {
		when(service.eliminarI("12345qwert")).thenReturn(respuesta1);
		when(service.eliminarI("12345")).thenReturn(respuesta2);
		
		assertTrue(service.eliminarI("12345qwert").isOk());
		assertFalse(service.eliminarI("12345").isOk());
		
	}

}
