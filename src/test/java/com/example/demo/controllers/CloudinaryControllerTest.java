package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.RespuestaDto;

@ExtendWith(MockitoExtension.class)
class CloudinaryControllerTest {

	@Mock
	private CloudinaryController controller;
	
	@Test
	@DisplayName("Eliminar imagen")
	void testEliminarImagen() {
		RespuestaDto respuesta = new RespuestaDto();
		respuesta.setOk(false);
		when(controller.deleteImagen("12345")).thenReturn(respuesta);
		assertFalse(controller.deleteImagen("12345").isOk());
		assertNull(controller.deleteImagen("12345").getMensaje());
		
		respuesta.setOk(true);
		respuesta.setMensaje("Imagen eliminada");
		when(controller.deleteImagen("12345qwert")).thenReturn(respuesta);
		assertTrue(controller.deleteImagen("12345qwert").isOk());
		assertNotNull(controller.deleteImagen("12345qwert").getMensaje());
		
	}

}
