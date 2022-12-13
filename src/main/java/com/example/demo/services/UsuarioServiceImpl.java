package com.example.demo.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.EditarUsuario;
import com.example.demo.dto.RespuestaDto;
import com.example.demo.entity.Imagen;
import com.example.demo.entity.Rol;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService {
	private Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private RoleRepository rRepository;
	@Autowired
	private ImagenRepository iRepository;
	@Autowired
	private CloudinaryService cloudinary;
	@Autowired
	private BCryptPasswordEncoder encoder;
//	@Autowired
//	private CorreoService correo;

	@Value("${url.image.nousuario}")
	private String noImagenUsuarioUrl;
	@Value("${url.idnousuario}")
	private String noImagenIdUsuario;

	@Override
	@Transactional(readOnly = true)
	public RespuestaDto listarTodos() {
		return new RespuestaDto(true, "Usuarios:", null, null, null, repository.findAll(), null, null, null);
	}

	@Override
	@Transactional(readOnly = true)
	public RespuestaDto listarUno(String id) {
		Usuario usuario = repository.findById(id).orElse(null);
		if (usuario == null)
			return new RespuestaDto(false, "No se encontro el usuario", null, null, null, null, null, null, null);
		return new RespuestaDto(true, "Usuario encontrado", null, null, null, null, usuario, null, null);
	}

	@Override
	@Transactional
	public RespuestaDto crear(Usuario usuario) {
		if (repository.findByEmailIgnoreCase(usuario.getEmail()) != null)
			return new RespuestaDto(false, "El correo ya esta en uso", null, null, null, null, null, null, null);
		if (repository.findByUsernameIgnoreCase(usuario.getUsername()) != null)
			return new RespuestaDto(false, "El usuario ya esta en uso", null, null, null, null, null, null, null);

		usuario.setCreateAt(new Date());
		usuario.setEnabled(true);
		usuario.setIntentos(0);
		usuario.setImage(noImagenUsuarioUrl);
		usuario.setRoles(Arrays.asList("ROLE_USER"));
		usuario.setPassword(encoder.encode(usuario.getPassword()));

		Usuario u = repository.save(usuario);
		Imagen imagen = new Imagen(u.getName() + ".jpeg", u.getImage(), noImagenIdUsuario, u.getId(), null);
		iRepository.save(imagen);
		CorreoService correo = new CorreoService(u.getEmail(), "<h1>Bienvenido a CaseStudy Store!</h1>");
		correo.start();
//		correo.sendEmail(u.getEmail(), "<h1>Bienvenido a CaseStudy Store!</h1>");
		return new RespuestaDto(true, String.format("Usuario %s creado con exito", u.getName()), null, null, null, null,
				u, null, null);
	}

	@Override
	@Transactional
	public RespuestaDto editar(String id, EditarUsuario u) {
		Usuario user = repository.findById(id).orElse(null);
		if (user == null)
			return new RespuestaDto(false, String.format("Id: %s de usuario NO existe", id), null, null, null, null,
					null, null, null);
		user.setName(u.getName());
		user.setPassword((u.getPassword() != null && u.getPassword().length() > 4) ? encoder.encode(u.getPassword())
				: user.getPassword());
		if (u.getImage() != null) {
			Imagen imagen = iRepository.findByUsuarioId(id);
			if (!user.getImage().contains(noImagenIdUsuario))
				cloudinary.eliminarI(imagen.getImagenId());
			imagen.setImagenId(u.getIdImage());
			imagen.setImagenUrl(u.getImage());
			user.setImage(u.getImage());
			iRepository.save(imagen);
		}
		return new RespuestaDto(true, String.format("Usuario %s editado con exito", user.getName()), null, null, null,
				null, repository.save(user), null, null);
	}

	@Override
	@Transactional
	public RespuestaDto eliminar(String id) {
		Usuario usuario = repository.findById(id).orElse(null);

		if (usuario == null)
			return new RespuestaDto(false, String.format("Id: %s de usuario NO existe", id), null, null, null, null,
					null, null, null);
		usuario.setEnabled(!usuario.isEnabled());
		return new RespuestaDto(true, "Usuario editado", null, null, null, null, repository.save(usuario), null, null);
	}

	// Seguridad
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Usuario usuario = repository.findByUsernameIgnoreCase(username);

			List<GrantedAuthority> authorities = usuario.getRoles().stream().map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
			return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true,
					authorities);
		} catch (Exception e) {
			log.error("Error en el login, usuario o contraseña invalidos");
			throw new UsernameNotFoundException("Error en el Login");
		}
	}

	@Override
	@Transactional
	public RespuestaDto editarRoles(Usuario usuario, String id) {
		if (usuario.getRoles() == null)
			return new RespuestaDto(false, "Faltan los roles del usuario", null, null, null, null, null, null, null);
		Usuario user = repository.findById(id).orElse(null);
		if (user == null)
			return new RespuestaDto(false, "El usuario que intentas editar no existe en BD.", null, null, null, null,
					null, null, null);
		List<String> roles = rRepository.findAll().stream().map(Rol::getRol).collect(Collectors.toList());
		user.setRoles(new ArrayList<>());
		user.addRol("ROLE_USER");
		usuario.getRoles().forEach(r -> {
			if (roles.contains(r) && !(user.getRoles().contains(r)))
				user.addRol(r);
		});
		return new RespuestaDto(true, String.format("Roles del usuario %s editados ", user.getName()), null, null, null,
				null, repository.save(user), null, null);
	}

}
