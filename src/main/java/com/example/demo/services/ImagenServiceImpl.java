package com.example.demo.services;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Imagen;
import com.example.demo.entity.Producto;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
@SuppressWarnings("rawtypes")
public class ImagenServiceImpl implements ImagenService {
	private Logger log = LoggerFactory.getLogger(ImagenServiceImpl.class);
	@Autowired
	private CloudinaryService service;
	@Autowired
	private ImagenRepository iRepository;
	@Autowired
	private ProductoRepository pRepository;
	@Autowired
	private UsuarioRepository uRepository;
	
	@Value("${url.image.nousuario}")
	private String noUsuarioUrl;
	@Value("${url.image.noproducto}")
	private String noProductoUrl;
	@Value("${url.idnousuario}")
	private String noUsuarioId;
	@Value("${url.idnoproducto}")
	private String noProductoId;

	@Override
	public RespuestaDto guardarIU(MultipartFile file, String idU) {
		try {

			Usuario usuario = uRepository.findById(idU).get();
			Imagen imagen = iRepository.findByImagenUrl(usuario.getImage());
			Map result = service.upload(file);
			guardarImagen(usuario.getImage(), noUsuarioId, file, result, imagen, usuario.getId(), true);
			usuario.setImage(result.get("url").toString());
			uRepository.save(usuario);
			return new RespuestaDto(true, "Imagen de usuario subida con exito!");
		} catch (Exception e) {
			log.error("Algo salio mal al guardar la imagen " + e.getMessage());
			return new RespuestaDto(false,
					String.format("Algo salio mal, posiblemente el id: %s, del usuario no esta en la BD", idU));
		}
	}

	@Override
	public RespuestaDto eliminarIU(String id) {
		String msg = "Usuario no tine imagen";
		try {
			Usuario usuario = uRepository.findById(id).get();
			if (!(usuario.getImage().contains(noUsuarioId))) {
				msg = eliminarImagen(usuario.getImage(), "usuario");
				usuario.setImage(noUsuarioUrl);
				uRepository.save(usuario);
			}
			return new RespuestaDto(true, msg);
		} catch (Exception e) {
			log.error("Algo salio mal al eliminar la imagen del usuario" + e.getMessage());
			return new RespuestaDto(false,
					String.format("Algo salio mal, posiblemente el id: %s, del usuario no esta en la BD"
							+ " ó la imagen ya fue eliminada", id));
		}
	}

	@Override
	public RespuestaDto guardarIP(MultipartFile file, String idP) {
		try {
			Map result = service.upload(file);

			Producto producto = pRepository.findById(idP).get();
			Imagen imagen = iRepository.findByImagenUrl(producto.getImagen());
			guardarImagen(producto.getImagen(), noProductoId, file, result, imagen, producto.getId(), false);
			producto.setImagen(result.get("url").toString());
			pRepository.save(producto);
			return new RespuestaDto(true, "Imagen de producto subida con exito!");
		} catch (Exception e) {
			log.error("Algo salio mal al guardar la imagen del producto" + e.getMessage());
			return new RespuestaDto(false,
					String.format("Algo salio mal, posiblemente el id: %s, del usuario no esta en la BD", idP));
		}
	}

	@Override
	public RespuestaDto eliminarIP(String id) {
		String msg = "Producto no tine imagen";
		try {
			Producto producto = pRepository.findById(id).get();
			if (!(producto.getImagen().contains("goev2g0h9li7wbcajaet"))) {
				msg = eliminarImagen(producto.getImagen(), "producto");
				producto.setImagen(noProductoUrl);
				pRepository.save(producto);
			}
			return new RespuestaDto(true, msg);
		} catch (Exception e) {
			log.error("Algo salio mal al eliminar la imagen del producto" + e.getMessage());
			return new RespuestaDto(false,
					String.format("Algo salio mal, posiblemente el id: %s, del producto no esta en la BD"
							+ " ó la imagen ya fue eliminada", id));
		}
	}

	private void guardarImagen(final String urlImage, final String idNoimage, final MultipartFile file,
			final Map result, final Imagen imagen, String id, boolean esUsuario) throws IOException {
		if (urlImage.contains(idNoimage)) {
			Imagen img = new Imagen(file.getOriginalFilename(), result.get("url").toString(),
					result.get("public_id").toString(), null, null);
			if (esUsuario)
				img.setUsuarioId(id);
			else
				img.setProductoId(id);
			iRepository.save(img);
		} else {
			service.delete(imagen.getImagenId());
			imagen.setName(file.getOriginalFilename());
			imagen.setImagenUrl(result.get("url").toString());
			imagen.setImagenId(result.get("public_id").toString());
			iRepository.save(imagen);
		}
	}

	private String eliminarImagen(final String urlImage, String msg) throws IOException {
		Imagen imagen = iRepository.findByImagenUrl(urlImage);
		service.delete(imagen.getImagenId());
		iRepository.delete(imagen);
		return String.format("Imagen de %s eliminada", msg);
	}
}
