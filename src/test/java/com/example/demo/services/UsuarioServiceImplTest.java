package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Datos.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.EditarUsuario;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Usuario;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

	@Mock
	UsuarioServiceImpl service;
	
	RespuestaDto respuesta;
	List<Usuario> usuarios;
	@BeforeEach
	public void setUp() {
		respuesta = new RespuestaDto();
		usuarios = new ArrayList<>();
		usuarios.add(USUARIO);
		usuarios.add(USUARIO2);
		respuesta.setOk(true);
	}
	@Test
	@DisplayName("Listar todos los usuarios")
	void testListarTodos() {
		respuesta.setUsuarios(usuarios);
		when(service.listarTodos()).thenReturn(respuesta);
		
		assertTrue(service.listarTodos().isOk());
		assertNotNull(service.listarTodos());
		assertEquals(USUARIO, service.listarTodos().getUsuarios().get(0));
		assertEquals(USUARIO2, service.listarTodos().getUsuarios().get(1));
		assertIterableEquals(USUARIOS, service.listarTodos().getUsuarios());
		assertTrue(service.listarTodos().getUsuarios().size() > 1);
		verify(service,times(6)).listarTodos();
	}
	
	@Test
	@DisplayName("Listar un usuario por id")
	void testListarUno() {
		respuesta.setUsuario(USUARIO);
		when(service.listarUno("12345qwert")).thenReturn(respuesta);
		
		assertNotNull(service.listarUno("12345qwert"));
		assertEquals(USUARIO, service.listarUno("12345qwert").getUsuario());
		
		respuesta.setUsuario(USUARIO2);
		when(service.listarUno("67890yuiop")).thenReturn(respuesta);
		assertEquals(USUARIO2, service.listarUno("67890yuiop").getUsuario());
		
		verify(service,times(3)).listarUno(any(String.class));
	}
	
	@Test
	@DisplayName("Crear un usuario")
	void testCrear() {
		respuesta.setUsuario(USUARIO);
		when(service.crear(USUARIO)).thenReturn(respuesta);
		
		assertTrue(service.crear(USUARIO).isOk());
		assertNotNull(service.crear(USUARIO).getUsuario());
		assertEquals(USUARIO, service.crear(USUARIO).getUsuario());
		verify(service,times(3)).crear(USUARIO);

	}
	
	@Test
	@DisplayName("Editar un usuario por id")
	void testEditar() {
		respuesta.setUsuario(USUARIO);
		when(service.editar("12345qwert", new EditarUsuario())).thenReturn(respuesta);
		when(service.editar("12345", new EditarUsuario())).thenReturn(null);
		
		assertNotNull(service.editar("12345qwert",new EditarUsuario()).getUsuario());
		assertEquals(USUARIO, service.editar("12345qwert",new EditarUsuario()).getUsuario());
		assertNotEquals(USUARIO2, service.editar("12345qwert",new EditarUsuario()).getUsuario());
		assertNull(service.editar("12345",new EditarUsuario()));
		verify(service,times(3)).editar("12345qwert",new EditarUsuario());
	}
	
	@Test
	@DisplayName("Eliminar un usuario")
	void testEliminar() {
		respuesta.setUsuario(USUARIO);
		when(service.eliminar( "12345qwert" )).thenReturn(respuesta);
		when(service.eliminar( "12345" )).thenReturn(null);
		
		assertNotNull(service.eliminar( "12345qwert" ).getUsuario());
		assertEquals(USUARIO, service.eliminar("12345qwert").getUsuario());
		assertNotEquals(USUARIO2, service.eliminar("12345qwert").getUsuario());
		assertNull(service.eliminar("12345"));
		
		verify(service,times(4)).eliminar(any(String.class));
	}

}
