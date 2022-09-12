package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.Datos;
//import com.example.demo.repository.ImagenRepository;
//import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UsuarioRepository;

class UsuarioServiceImplTest {
	@Mock
	UsuarioRepository uRepository;
//	@Mock
//	RoleRepository 	  rRepository;
//	@Mock
//	ImagenRepository  iRepository;
	
	@InjectMocks
	private UsuarioService service;
	
	@BeforeEach
	void setUp() {
		uRepository = mock(UsuarioRepository.class);
		service = new UsuarioServiceImpl();
//		rRepository = mock(RoleRepository.class);
//		iRepository = mock(ImagenRepository.class);
	}

	@Test
	void testListarTodos() {
		when(uRepository.findAll()).thenReturn(Datos.USUARIOS);
//		assertTrue(!service.listarTodos().isEmpty());
	}
	@Test
	void testListarUno() {
		when(uRepository.findById(any(String.class))).thenReturn(Datos.OPUSUARIO);
//		assertNull(service.listarUno("1q2w3e4r5t"));
		
	}

}
