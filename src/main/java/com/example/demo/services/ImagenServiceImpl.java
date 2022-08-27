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
			Imagen imagen = iRepository.findByUsuarioId(usuario.getId());
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
	public RespuestaDto guardarIP(MultipartFile file, String idP) {
		try {
			Map result = service.upload(file);

			Producto producto = pRepository.findById(idP).get();
			Imagen imagen = iRepository.findByProductoId(producto.getId());
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

	@Override
	public RespuestaDto eliminarI(String id) {
		try {
			service.delete(id);
			return new RespuestaDto(true, "Eliminación de la imagen sin problema");
		} catch (Exception e) {
			log.error("Error al eliminar la imagen ", e.getLocalizedMessage());
			return new RespuestaDto(false, e.getMessage());
		}
	}

	@Override
	public RespuestaDto editarIP(Producto producto,String id) {
		Imagen imagen = iRepository.findByProductoId(id);
		if(imagen != null) {
		try {
			if (!(producto.getImagen().contains(noProductoId)))
				service.delete(imagen.getImagenId());
		} catch (IOException e) {
			log.error("Algo salio mal al eliminar la imagen del producto");
			return new RespuestaDto(false,"Algo salio mal al eliminar la imagen del producto");
		}
		imagen.setImagenId(producto.getIdImagen());
		imagen.setImagenUrl(producto.getImagen());
		iRepository.save(imagen);
		pRepository.save(producto);
		return new RespuestaDto(true,String.format("Imagen de %s actualizada",producto.getNombre()));
	}else {
		return new RespuestaDto(false,String.format("Id:%s de Producto NO existe en BD"));
	}
	}

	@Override
	public RespuestaDto editarIU(Usuario usuario, String id) {
		Imagen imagen = iRepository.findByUsuarioId(id);
		if(imagen != null) {
			try {
				if (!(usuario.getImage().contains(noUsuarioId)))
					service.delete(imagen.getImagenId());
			} catch (IOException e) {
				log.error("Algo salio mal al eliminar la imagen del producto");
				return new RespuestaDto(false,"Algo salio mal al eliminar la imagen del producto");
			}
			imagen.setImagenId(usuario.getIdImage());
			imagen.setImagenUrl(usuario.getImage());
			iRepository.save(imagen);
			uRepository.save(usuario);
			return new RespuestaDto(true,String.format("Imagen de %s actualizada",usuario.getName()));
		}else {
			return new RespuestaDto(false,String.format("Id:%s de usuario NO existe en BD"));
		}
	}
}
