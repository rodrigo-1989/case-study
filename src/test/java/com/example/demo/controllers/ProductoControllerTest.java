package com.example.demo.controllers;

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

import com.example.demo.dto.NuevoProducto;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Producto;

class ProductoControllerTest {

	@Mock
	private ProductoController controller;
	
	RespuestaDto respuesta;
	List<Producto> productos;
	@BeforeEach
	void setUp(){
		MockitoAnnotations.openMocks(this);
		respuesta = new RespuestaDto();
		productos = new ArrayList<>();
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
		productos.add(PRODUCTO);
		productos.add(PRODUCTO2);
	}
	
	@Test
	@DisplayName("Listar todos los productos")
	void testListarTodos() {
		respuesta.setProductos(productos);
		respuesta.setOk(true);
		when(controller.listar()).thenReturn(respuesta);
		assertTrue(controller.listar().isOk());
		assertEquals(productos, controller.listar().getProductos());
		verify(controller,times(2)).listar();
	}

	@Test
	@DisplayName("Listar un producto")
	void testListarUno(){
		respuesta.setProducto(PRODUCTO);
		respuesta.setOk(true);
		when(controller.listarUno("12345qwert")).thenReturn(respuesta);
		assertTrue(controller.listarUno("12345qwert").isOk());
		assertEquals(PRODUCTO, controller.listarUno("12345qwert").getProducto());
		
		respuesta.setProducto(null);
		respuesta.setOk(false);
		when(controller.listarUno("12345")).thenReturn(respuesta);
		assertFalse(controller.listarUno("12345").isOk());
		assertNull(controller.listarUno("12345").getProducto());
		
		verify(controller,times(4)).listarUno(any(String.class));
	}
	
	@Test
	@DisplayName("Crear un producto")
	void testCrear(){
		respuesta.setProducto(PRODUCTO);
		respuesta.setOk(true);
		when(controller.crear(new NuevoProducto(), null)).thenReturn(respuesta);
		assertTrue(controller.crear(new NuevoProducto(), null).isOk());
		assertNotNull(controller.crear(new NuevoProducto(), null).getProducto());
		
		respuesta.setProducto(null);
		respuesta.setOk(false);
		respuesta.setMensaje("Error, no existe el id");
		when(controller.crear(any(),any())).thenReturn(respuesta);
		assertFalse(controller.crear(any(),any()).isOk());
		assertNull(controller.crear(any(),any()).getProducto());
		assertEquals("Error, no existe el id",controller.crear(any(),any()).getMensaje());
		
		verify(controller,times(5)).crear(any(),any());
	}
	
	@Test
	@DisplayName("Editar un producto")
	void testEditar(){
		respuesta.setProducto(PRODUCTO);
		respuesta.getProducto().setNombre("Galletas");;
		respuesta.getProducto().setPrecio(10.00);;
		respuesta.setOk(true);
		when(controller.editar("12345qwert",new NuevoProducto(), null)).thenReturn(respuesta);
		assertNotEquals(PRODUCTO,controller.editar("12345qwert",new NuevoProducto(), null));
		assertEquals("Galletas",controller.editar("12345qwert",new NuevoProducto(), null).getProducto().getNombre());
		assertEquals(10.00,controller.editar("12345qwert",new NuevoProducto(), null).getProducto().getPrecio());

		
		respuesta.setMensaje("Error, no existe el id");
		respuesta.setProducto(null);
		respuesta.setOk(false);
		when(controller.editar("12345",new NuevoProducto(),null)).thenReturn(respuesta);
		assertFalse(controller.editar("12345",new NuevoProducto(),null).isOk());
		assertNull(controller.editar("12345",new NuevoProducto(),null).getProducto());
		verify(controller,times(5)).editar(any(),any(),any());
	}
	
	@Test
	@DisplayName("Eliminar un producto")
	void testEliminar(){
		respuesta.setMensaje("Producto eliminado");
		respuesta.setOk(true);
		when(controller.eliminar("12345qwert")).thenReturn(respuesta);
		assertTrue(controller.eliminar("12345qwert").isOk());
		assertNotNull(controller.eliminar("12345qwert").getMensaje());
		
		respuesta.setMensaje("Error, no existe el id");
		respuesta.setOk(false);
		when(controller.eliminar("12345")).thenReturn(respuesta);
		assertFalse(controller.eliminar("12345").isOk());
		verify(controller,times(3)).eliminar(any(String.class));
	}

}
