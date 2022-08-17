package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CVProductos;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Imagen;
import com.example.demo.entity.Producto;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {
	private Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);
	@Autowired
	private ProductoRepository repository;
	@Autowired
	private ImagenRepository iRepository;
	@Autowired
	private CloudinaryService cloudinary;
	@Value("${url.image.noproducto}")
	private String noProductoUrl;
	@Value("${url.idnoproducto}")
	private String noProductoId;

	@Override
	public RespuestaDto listarTodos() {
		List<Producto> productos = (List<Producto>) repository.findAll();
		return (!productos.isEmpty()) ? new RespuestaDto(true, "Productos encontrados", productos)
				: new RespuestaDto(false, "Ningun producto encontrado");
	}

	@Override
	public RespuestaDto listarUno(String id) {
		Producto producto = repository.findById(id).orElse(null);
		return (producto != null) ? new RespuestaDto(true, "Producto encontrado", producto)
				: new RespuestaDto(false, "El producto que buscas no existe");
	}

	@Override
	public RespuestaDto crear(Producto producto) {
		Producto p = repository.findByNombre(producto.getNombre());
		producto.setImagen(noProductoUrl);
		producto.setExistentes((producto.getExistentes() > 0) ? producto.getExistentes() : 0);
		return (p == null) ? new RespuestaDto(true, "Producto creado con exito!", repository.save(producto))
				: new RespuestaDto(false, "El producto ya existe en BD");
	}

	@Override
	public RespuestaDto editar(String id, Producto producto) {
		RespuestaDto prod = listarUno(id);
		if (prod.isOk()) {
			prod.getProducto().setNombre(producto.getNombre());
			prod.getProducto().setPrecio(producto.getPrecio());
			prod.getProducto().setDescripcion(producto.getDescripcion());

			prod.getProducto().setExistentes(
					(producto.getExistentes() >= 0) ? producto.getExistentes() : prod.getProducto().getExistentes());
			return new RespuestaDto(true, "Producto editado", repository.save(prod.getProducto()));
		} else {
			return new RespuestaDto(false, "El producto con id:" + id + " No existe");
		}
	}

	@Override
	public RespuestaDto eliminar(String id) {
		RespuestaDto producto = listarUno(id);
		if (producto.isOk()) {
			repository.delete(producto.getProducto());

			if (!(producto.getProducto().getImagen().contains(noProductoId))) {
				try {
					eliminarImagen(producto.getProducto().getImagen());
				} catch (Exception e) {
					log.error("Error, al eliminar la imagen del producto eliminado");
					return new RespuestaDto(false, "Error al eliminar la imagen del producto");
				}
			}
			return new RespuestaDto(true, "Producto eliminado :(");
		} else {
			return new RespuestaDto(false, "El que intentas eliminar no existe");
		}
	}

	private void eliminarImagen(String urlImage) throws Exception {
		Imagen imagen = iRepository.findByImagenUrl(urlImage);
		cloudinary.delete(imagen.getImagenId());
		iRepository.delete(imagen);
	}

	@Override
	public RespuestaDto comprar(List<CVProductos> lista) {
		List<String> errores = procesarCompra(lista);
		return (errores.size() > 0) ? new RespuestaDto(false, errores)
				: new RespuestaDto(true, "Todos los productos fueron agregados");
	}

	@Override
	public RespuestaDto vender(List<CVProductos> lista) {
		List<String> errores = procesarVenta(lista);
		return (errores.size() > 0) ? new RespuestaDto(false, errores)
				: new RespuestaDto(true, "Felicidades por tu compra");
	}

	private List<String> procesarVenta(List<CVProductos> lista) {
		List<String> errores = new ArrayList<String>();
		for (CVProductos p : lista) {
			RespuestaDto r = listarUno(p.getId());
			if (r.isOk()) {
				String e = (p.getCantidad() > 0) ? "No estas autorizado para ingresar productos"
						: comprarVenderProducto(r.getProducto(), p.getCantidad());
				if (e != null)
					errores.add(e);
			} else
				errores.add("El producto con id: " + p.getId() + " NO existe en la BD");
		}
		return errores;
	}

	private List<String> procesarCompra(List<CVProductos> lista) {
		List<String> errores = new ArrayList<String>();
		for (CVProductos p : lista) {
			RespuestaDto r = listarUno(p.getId());
			if (r.isOk()) {
				String e = comprarVenderProducto(r.getProducto(), p.getCantidad());
				if (e != null)
					errores.add(e);
			} else
				errores.add("El producto con id: " + p.getId() + " NO existe en la BD");
		}
		return errores;
	}

	private String comprarVenderProducto(Producto producto, int cantidad) {
		if (cantidad > 0) {
			producto.setExistentes(cantidad + producto.getExistentes());
			repository.save(producto);
			return null;
		} else if ((cantidad * -1) > producto.getExistentes()) {
			return "No hay suficientes " + producto.getNombre() + " para hacer la compra";
		} else {
			producto.setExistentes(producto.getExistentes() + cantidad);
			repository.save(producto);
			return null;
		}
	}
}
