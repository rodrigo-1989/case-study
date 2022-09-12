package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CVProductos;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Imagen;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class ProductoServiceImpl implements ProductoService {
	@Autowired
	private ProductoRepository repository;
	@Autowired
	private ImagenRepository iRepository;
	@Autowired
	private UsuarioRepository uRepository;
	@Autowired
	private CloudinaryService cloudinary;
	@Autowired
	private CorreoService correo;
	@Value("${url.image.noproducto}")
	private String noImagenProductoUrl;
	@Value("${url.idnoproducto}")
	private String noImagenProducto;
	private StringBuilder msgCorreo = new StringBuilder();

	@Override
	public RespuestaDto listarTodos() {
		List<Producto> productos = repository.findAll();
		return (!productos.isEmpty())
				? new RespuestaDto(true, "Productos encontrados", productos, null, null, null, null)
				: new RespuestaDto(false, "Ningun producto encontrado", null, null, null, null, null);
	}

	@Override
	public RespuestaDto listarUno(String id) {
		Producto producto = repository.findById(id).orElse(null);
		return (producto != null) ? new RespuestaDto(true, "Producto encontrado", null, producto, null, null, null)
				: new RespuestaDto(false, "El producto que buscas no existe", null, null, null, null, null);
	}

	@Override
	public RespuestaDto listarParecidos(String nombre) {
		List<Producto> productos = repository.findByNombreContainingIgnoreCase(nombre);
		if (productos.isEmpty())
			return new RespuestaDto(false, String.format("No hay productos parecidos con el temino: %s", nombre), null,
					null, null, null, null);
		return new RespuestaDto(true, "Productos encontrados", productos, null, null, null, null);
	}

	@Override
	public RespuestaDto crear(Producto producto) {
		RespuestaDto response = new RespuestaDto();
		Producto p = repository.findByNombre(producto.getNombre());

		if (p == null) {
			producto.setImagen(producto.getImagen() == null ? noImagenProductoUrl : producto.getImagen());
			producto.setExistentes((producto.getExistentes() > 0) ? producto.getExistentes() : 0);
			Producto pn = repository.save(producto);
			String idImage = producto.getIdImagen() == null ? noImagenProducto : producto.getIdImagen();
			String urlImage = producto.getImagen() == null ? noImagenProductoUrl : producto.getImagen();
			iRepository.save(new Imagen(pn.getNombre() + ".jpeg", urlImage, idImage, null, pn.getId()));
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
			if (p == null || id.equals(prod.getProducto().getId()))
				return edit(prod, producto, id);
			return new RespuestaDto(false, "El producto ya existe en BD", null, null, null, null, null);
		} else {
			return new RespuestaDto(false, String.format("El producto con id:%s No existe", id), null, null, null, null,
					null);
		}
	}

	private RespuestaDto edit(RespuestaDto prod, Producto producto, String id) {
		prod.getProducto().setNombre(producto.getNombre());
		prod.getProducto().setPrecio(producto.getPrecio());
		prod.getProducto().setDescripcion(producto.getDescripcion());
		if (producto.getIdImagen() != null) {
			Imagen imagen = iRepository.findByProductoId(id);
			if (!prod.getProducto().getImagen().contains(noImagenProducto))
				cloudinary.eliminarI(imagen.getImagenId());

			imagen.setImagenId(producto.getIdImagen());
			imagen.setImagenUrl(producto.getImagen());
			prod.getProducto().setImagen(producto.getImagen());
			iRepository.save(imagen);

		}
		prod.getProducto().setExistentes(
				(producto.getExistentes() >= 0) ? producto.getExistentes() : prod.getProducto().getExistentes());
		return new RespuestaDto(true, "Producto editado", null, repository.save(prod.getProducto()), null, null, null);
	}

	@Override
	public RespuestaDto eliminar(String id) {
		RespuestaDto producto = listarUno(id);
		if (producto.isOk()) {
			repository.deleteById(producto.getProducto().getId());
			Imagen imagen = iRepository.findByProductoId(producto.getProducto().getId());
			if (!producto.getProducto().getImagen().contains(noImagenProducto))
				cloudinary.eliminarI(imagen.getImagenId());

			iRepository.deleteById(imagen.getId());
			return new RespuestaDto(true, "Producto eliminado :(", null, null, null, null, null);
		} else {
			return new RespuestaDto(false, String.format("El id:%s del producto No existe", id), null, null, null, null,
					null);
		}
	}

	@Override
	public RespuestaDto actualizarExistentes(List<CVProductos> lista) {
		List<String> errores = procesarCompra(lista);
		return (!errores.isEmpty())
				? new RespuestaDto(false, "Uno o mas produtos no existen en BD ó son insuficientes", null, null,
						errores, null, null)
				: new RespuestaDto(true, "Todos los productos fueron actualizados", null, null, null, null, null);
	}

	@Override
	public RespuestaDto editarUno(CVProductos producto) {
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
	public RespuestaDto vender(List<CVProductos> lista, String id) {
		msgCorreo = new StringBuilder();
		msgCorreo.append("<b>Muchas felicicades por tu compra!</b>");
		msgCorreo.append("<br><br>Detalles de tu compra\n<hr><ul>");
		List<String> errores = procesarVenta(lista);
		msgCorreo.append("</ul><hr>");
		Usuario u = uRepository.findById(id).orElse(null);
		if (u != null) {
			correo.sendEmail(u.getEmail(), msgCorreo.toString());
			return (!errores.isEmpty())
					? new RespuestaDto(false,
							"Una o mas compras no se efectuaron, por que no puedes ingresar prodcutos", null, null,
							errores, null, null)
					: new RespuestaDto(true, "Felicidades por tu compra", null, null, null, null, null);
		}
		return new RespuestaDto(false, "El Usuario que intenta hacer la compra no existe", null, null, null, null,
				null);
	}

	private List<String> procesarVenta(List<CVProductos> lista) {
		List<String> errores = new ArrayList<>();
		for (CVProductos p : lista) {
			RespuestaDto r = listarUno(p.getId());
			if (r.isOk()) {
				String e = (p.getCantidad() > 0) ? "*No estas autorizado ingresar productos*"
						: comprarVenderProducto(r.getProducto(), p.getCantidad());
				if (e != null) {
					errores.add(e);
					msgCorreo.append("<li>" + r.getProducto().getNombre() + " - " + e + "</li>");
				} else {
					msgCorreo.append("<li>" + r.getProducto().getNombre() + " - <b>compra exitosa!</b></li>");
				}

			} else
				errores.add("El producto con id: " + p.getId() + " NO existe en la BD");
		}
		return errores;
	}

	private List<String> procesarCompra(List<CVProductos> lista) {
		List<String> errores = new ArrayList<>();
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
			return "Lo sentimos por el momento no tenemos sufucientes " + producto.getNombre() + " para vender";
		} else {
			producto.setExistentes(producto.getExistentes() + cantidad);
			repository.save(producto);
			return null;
		}
	}

}
