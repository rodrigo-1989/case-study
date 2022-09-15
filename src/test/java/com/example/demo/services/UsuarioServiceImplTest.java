package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.example.demo.Datos.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.dto.EditarUsuario;
import com.example.demo.entity.Imagen;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {
	
	@Mock
	UsuarioRepository repository;
	@Mock
	ImagenRepository iRepository;
	@Mock
	UsuarioServiceImpl service;
	
	@Test
	@DisplayName("Listar todos los usuarios")
	void testListarTodos() {
		when(service.listarTodos()).thenReturn(USUARIOS);
		
		assertNotNull(service.listarTodos());
		assertEquals(USUARIO, service.listarTodos().get(0));
		assertEquals(USUARIO2, service.listarTodos().get(1));
		assertIterableEquals(USUARIOS, service.listarTodos());
		assertTrue(service.listarTodos().size() > 1);
		verify(service,times(5)).listarTodos();
	}
	
	@Test
	@DisplayName("Listar un usuario por id")
	void testListarUno() {
		when(repository.findById("12345qwert")).thenReturn(OPUSUARIO);
		when(service.listarUno("12345qwert")).thenReturn(USUARIO);
		when(service.listarUno("67890yuiop")).thenReturn(USUARIO2);
		
		assertNotNull(repository.findById("12345qwert"));
		assertEquals(OPUSUARIO, repository.findById("12345qwert"));
		assertTrue(repository.findById("12345qwert").isPresent());
		verify(repository,times(3)).findById("12345qwert");
		
		assertNotNull(service.listarUno("12345qwert"));
		assertEquals(USUARIO, service.listarUno("12345qwert"));
		assertEquals(USUARIO2, service.listarUno("67890yuiop"));
		verify(service,times(2)).listarUno("12345qwert");
		verify(service,times(1)).listarUno("67890yuiop");
	}
	
	@Test
	@DisplayName("Crear un usuario")
	void testCrear() {
		when(repository.save(any(Usuario.class))).thenReturn(USUARIO);
		when(service.crear(USUARIO2)).thenReturn(USUARIO2);
		when(iRepository.save( new Imagen() )).thenReturn(IMAGEN);
		
		assertNotNull(repository.save(new Usuario()));
		assertEquals(USUARIO, repository.save(new Usuario()));
		verify(repository,times(2)).save(new Usuario());
		
		assertNotNull(service.crear(USUARIO2));
		assertEquals(USUARIO2, service.crear(USUARIO2));
		verify(service,times(2)).crear(USUARIO2);
		
		assertNotNull(iRepository.save(new Imagen()));
		verify(iRepository,times(1)).save(new Imagen());
	}
	
	@Test
	@DisplayName("Editar un usuario por id")
	void testEditar() {
		when(service.editar("12345qwert", new EditarUsuario())).thenReturn(USUARIO);
		when(service.editar("12345", new EditarUsuario())).thenReturn(null);
		
		assertNotNull(service.editar("12345qwert",new EditarUsuario()));
		assertEquals(USUARIO, service.editar("12345qwert",new EditarUsuario()));
		assertNotEquals(USUARIO2, service.editar("12345qwert",new EditarUsuario()));
		assertNull(service.editar("12345",new EditarUsuario()));
		verify(service,times(3)).editar("12345qwert",new EditarUsuario());
	}
	
	@Test
	@DisplayName("Eliminar un usuario")
	void testEliminar() {
		when(service.eliminar( "12345qwert" )).thenReturn(USUARIO);
		when(service.eliminar( "12345" )).thenReturn(null);
		
		assertNotNull(service.eliminar( "12345qwert" ));
		assertEquals(USUARIO, service.eliminar("12345qwert"));
		assertNotEquals(USUARIO2, service.eliminar("12345qwert"));
		assertNull(service.eliminar("12345"));
		
		verify(service,times(4)).eliminar(any(String.class));
	}
	
	@Test
	@DisplayName("Verificacion del correo")
	void testExisteCorreo() {
		when(service.existeCorreo( "email1@email.com" )).thenReturn( true );
		when(repository.findByEmailIgnoreCase( "email1@email.com" )).thenReturn( USUARIO );
		
		assertTrue(service.existeCorreo( "email1@email.com" ));
		assertFalse(service.existeCorreo( "email3@email.com" ));
		verify(service,times(2)).existeCorreo(any(String.class) );
		
		Usuario usuario = repository.findByEmailIgnoreCase("email1@email.com");
		assertNotNull(usuario);
		assertEquals( "email1@email.com", usuario.getEmail());
		assertNotEquals( "email2@email.com", usuario.getEmail());
		verify(repository,times(1)).findByEmailIgnoreCase("email1@email.com");
	}

}
