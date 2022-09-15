package com.example.demo.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.example.demo.Datos.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Usuario;

class UsuarioControllerTest {

	@Mock
	private UsuarioController controller;
	
	private RespuestaDto respuesta;
	private List<Usuario> usuarios;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		respuesta = mock(RespuestaDto.class);
		usuarios = mock(List.class);
		usuarios.add(USUARIO);
		usuarios.add(USUARIO2);
	}
	@Test
	void testListarTodos() {
		respuesta.setOk(true);
		respuesta.setUsuarios(usuarios);
		when(controller.listarTodos()).thenReturn(respuesta);
	}

}
