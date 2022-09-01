package com.example.demo.services;

import java.io.IOException;
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
	public RespuestaDto listarParecidos(String nombre) {
		return new RespuestaDto(true, "Productos encontrados", repository.findByNombreContainingIgnoreCase(nombre));
	}

	@Override
	public RespuestaDto crear(Producto producto) {
		RespuestaDto response = new RespuestaDto();
		Producto p = repository.findByNombre(producto.getNombre());
		producto.setImagen(producto.getImagen() == null ? noProductoUrl : producto.getImagen());
		producto.setExistentes((producto.getExistentes() > 0) ? producto.getExistentes() : 0);

		if (p == null) {
			Producto pn = repository.save(producto);
			iRepository.save(new Imagen(pn.getNombre() + ".jpeg", pn.getImagen(), noProductoId, null, pn.getId()));
			response.setOk(true);
			response.setMensaje("Producto creado con exito!");
			response.setProducto(pn);
		} else {
			response.setOk(false);
			response.setMensaje("El producto ya existe en BD");
		}
		return response;

	}

	@Override
	public RespuestaDto editar(String id, Producto producto) {
		RespuestaDto prod = listarUno(id);
		if (prod.isOk()) {
			Producto p = repository.findByNombre(producto.getNombre());
			if (p == null || id.equals(prod.getProducto().getId())) {
				prod.getProducto().setNombre(producto.getNombre());
				prod.getProducto().setPrecio(producto.getPrecio());
				prod.getProducto().setDescripcion(producto.getDescripcion());

				if (producto.getIdImagen() != null) {
					Imagen imagen = iRepository.findByProductoId(id);
					try {
						if (!(producto.getImagen().contains(noProductoId)))
							cloudinary.delete(imagen.getImagenId());
					} catch (IOException e) {
						log.error("Algo salio mal al eliminar la imagen del producto");
						return new RespuestaDto(false, "Algo salio mal al eliminar la imagen del producto");
					}
					imagen.setImagenId(producto.getIdImagen());
					imagen.setImagenUrl(producto.getImagen());
					prod.getProducto().setImagen(producto.getImagen());
					iRepository.save(imagen);
				}
				prod.getProducto().setExistentes((producto.getExistentes() >= 0) ? producto.getExistentes()
						: prod.getProducto().getExistentes());
				return new RespuestaDto(true, "Producto editado", repository.save(prod.getProducto()));
			} else {
				return new RespuestaDto(false, "El producto ya existe en BD");
			}
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
					eliminarImagen(producto.getProducto().getId());
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

	private void eliminarImagen(String idProducto) throws Exception {
		Imagen imagen = iRepository.findByProductoId(idProducto);
		cloudinary.delete(imagen.getImagenId());
		iRepository.deleteById(imagen.getId());
	}

	@Override
	public RespuestaDto comprar(List<CVProductos> lista) {
		List<String> errores = procesarCompra(lista);
		return (errores.size() > 0) ? new RespuestaDto(false, errores)
				: new RespuestaDto(true, "Todos los productos fueron agregados");
	}

	@Override
	public RespuestaDto comprarUno(CVProductos producto) {
		RespuestaDto resp = new RespuestaDto();
		try {
			RespuestaDto r = listarUno(producto.getId());
			if (r.isOk()) {
				if (producto.getCantidad() < 0) {
					resp.setOk(false);
					resp.setMensaje("No se pueden tener un numero de productos negativos");
				} else {
					r.getProducto().setExistentes(producto.getCantidad());
					repository.save(r.getProducto());
					resp.setOk(true);
					resp.setMensaje("Producto actualizado con exito!");
				}
			} else {
				resp.setOk(false);
				resp.setMensaje("Error, el producto NO se encontro en la BD");
			}

		} catch (Exception e) {
			resp.setOk(false);
			resp.setMensaje("Error, algo salio mal al intentar actulizar las existencias");
		}
		return resp;
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
