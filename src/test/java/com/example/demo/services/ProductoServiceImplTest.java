package com.example.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static com.example.demo.Datos.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Producto;
import com.example.demo.repository.ProductoRepository;

class ProductoServiceImplTest {

	@Mock
	ProductoRepository repository;
	@Mock
	ProductoService service;
	
	RespuestaDto respuesta;
	List<Producto> productos;
	
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
		respuesta = new RespuestaDto();
		PRODUCTO.setId("12345qwert");
		PRODUCTO.setNombre("Galletas Maria");
		PRODUCTO.setPrecio(5.00);
		PRODUCTO.setDescripcion("Gamesa");
		PRODUCTO.setImagen("https://ImagenProducto");
		PRODUCTO.setExistentes(10);
		PRODUCTO2.setId("67890yuiop");
		PRODUCTO2.setNombre("Gansito");
		PRODUCTO2.setPrecio(7.00);
		PRODUCTO2.setDescripcion("Marinela");
		PRODUCTO2.setImagen("https://ImagenProducto");
		PRODUCTO2.setExistentes(10);
		productos = new ArrayList<Producto>();
		productos.add(PRODUCTO);
		productos.add(PRODUCTO2);
	}

	@Test
	@DisplayName("Listar todos los productos")
	void testListarTodos() {
		when(repository.findAll()).thenReturn(productos);
		when(service.listarTodos()).thenReturn(respuesta);
		
		assertNull(service.listarTodos().getProductos());
		assertFalse(service.listarTodos().isOk());
		
		respuesta.setProductos( repository.findAll());
		respuesta.setOk(true);
		assertNotNull(repository.findAll());
		assertTrue(repository.findAll().size() > 1);
		assertEquals(PRODUCTO, repository.findAll().get(0));
		verify(repository,times(4)).findAll();
		
		assertNotNull(service.listarTodos());
		assertEquals(PRODUCTO2, service.listarTodos().getProductos().get(1));
		assertTrue(service.listarTodos().isOk());
		verify(service,times(5)).listarTodos();
		
	}
	
	@Test
	@DisplayName("Listar un producto")
	void testListarUno() {
		when(repository.findById( "12345qwert" )).thenReturn(OPPRODUCTO);
		when(service.listarUno( "12345qwert" )).thenReturn(respuesta);
		
		assertNull(service.listarUno( "12345qwert" ).getProducto());
		assertFalse(service.listarUno( "12345qwert" ).isOk());
		
		respuesta.setProducto(repository.findById( "12345qwert" ).get());
		respuesta.setOk(true);
		
		assertTrue(repository.findById( "12345").isEmpty() );
		assertNotEquals(PRODUCTO2, repository.findById( "12345qwert").get());
		verify(repository,times(3)).findById( any(String.class));
		
		assertNotNull(service.listarUno("12345qwert").getProducto());
		assertNull(service.listarUno("12345"));
		assertTrue(service.listarUno("12345qwert").isOk());
		assertEquals(PRODUCTO, service.listarUno("12345qwert").getProducto());
		verify(service,times(5)).listarUno( "12345qwert");
	}
	
	@Test
	@DisplayName("Guardar un producto")
	void testGuardar() {
		when(repository.save( any(Producto.class) )).thenReturn(PRODUCTO);
		when(service.crear( any(Producto.class))).thenReturn(respuesta);
		
		Producto producto = repository.save(new Producto());
		respuesta.setProducto(producto);
		respuesta.setOk(true);
		
		assertNotNull(producto);
		verify(repository,times(1)).save(new Producto());
		
		assertNotNull(service.crear(PRODUCTO).getProducto());
		assertTrue(service.crear(PRODUCTO).isOk());
		verify(service,times(2)).crear(any(Producto.class));
	}

	@Test
	@DisplayName("Editar un producto")
	void testEditar() {
		when(repository.save(any(Producto.class))).thenReturn(PRODUCTO);
		when(service.editar("12345qwert", PRODUCTO)).thenReturn(respuesta);
		
		Producto producto = repository.save(PRODUCTO);
		producto.setNombre("Maravillas");
		producto.setDescripcion("Gamesas");
		assertNotNull(producto);
		verify(repository,times(1)).save(any(Producto.class));

		respuesta.setProducto(producto);
		respuesta.setOk(true);
		
		assertNotNull(service.editar("12345qwert",PRODUCTO).getProducto());
		assertTrue(service.editar("12345qwert",PRODUCTO).isOk());
		assertEquals("12345qwert", service.editar("12345qwert",PRODUCTO).getProducto().getId());
		assertNotEquals("Galletas Maria",service.editar("12345qwert",PRODUCTO).getProducto().getNombre());
		assertEquals("Maravillas", service.editar("12345qwert",PRODUCTO).getProducto().getNombre());
		verify(service,times(5)).editar("12345qwert",PRODUCTO);
	}
	
	@Test
	@DisplayName("Eliminar un producto")
	void testEliminar() {
		when(service.eliminar("12345qwert")).thenReturn(respuesta);
		
		respuesta.setOk(true);
		respuesta.setMensaje("Producto eliminado");
		assertEquals("Producto eliminado", service.eliminar("12345qwert").getMensaje());
		assertTrue(service.eliminar("12345qwert").isOk());
		
		when(service.eliminar("12345")).thenReturn(respuesta);
		
		respuesta.setOk(false);
		respuesta.setMensaje("Producto NO encontrado");
		assertNotEquals("Producto eliminado", service.eliminar("12345").getMensaje());
		assertFalse(service.eliminar("12345qwert").isOk());
		verify(service,times(4)).eliminar(any(String.class));
	}

}
