package com.example.demo.services;

import java.io.IOException;
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

import com.example.demo.dto.EditarUsuario;
import com.example.demo.entity.Imagen;
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
	private CloudinaryService service;
	@Autowired
	private BCryptPasswordEncoder encoder;

	@Value("${url.image.nousuario}")
	private String noImagenUsuarioUrl;
	@Value("${url.idnousuario}")
	private String noImagenIdUsuario;

	@Override
	public List<Usuario> listarTodos() {
		return repository.findAll();
	}

	@Override
	public Usuario listarUno(String id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Usuario crear(Usuario usuario) {
		usuario.setCreateAt(new Date());
		usuario.setEnabled(true);
		usuario.setIntentos(0);
		usuario.setImage(noImagenUsuarioUrl);
		usuario.setRoles(Arrays.asList("ROLE_USER"));
		usuario.setPassword(encoder.encode(usuario.getPassword()));

		Usuario u = repository.save(usuario);
		Imagen imagen = new Imagen(u.getName() + ".jpeg", u.getImage(), noImagenIdUsuario, u.getId(), null);
		iRepository.save(imagen);
		return u;
	}

	@Override
	public Usuario editar(String id, EditarUsuario u) {
		Usuario user = listarUno(id);
		if (user == null)
			return null;
		user.setName((u.getName() != null && u.getName().length() > 4) ? u.getName() : user.getName());
		user.setPassword((u.getPassword() != null && u.getPassword().length() > 4) ? encoder.encode(u.getPassword())
				: user.getPassword());
		if (u.getImage() != null) {
			Imagen imagen = iRepository.findByUsuarioId(id);
			if (!user.getImage().contains(noImagenIdUsuario)) {
				try {
					service.delete(imagen.getImagenId());
				} catch (IOException e) {
					log.error("Algo salio mal al eliminar la imagen del usuario");
					return null;
				}
			}
			imagen.setImagenId(u.getIdImage());
			imagen.setImagenUrl(u.getImage());
			user.setImage(u.getImage());
			iRepository.save(imagen);
		}
		return repository.save(user);
	}

	@Override
	public Usuario eliminar(String id) {
		Usuario usuario = listarUno(id);
		if (usuario == null) 
			return null;
		usuario.setEnabled(!usuario.isEnabled());
		return repository.save(usuario);
	}

	@Override
	public boolean existeCorreo(String correo) {
		return repository.findByEmailIgnoreCase(correo) != null;
	}

	@Override
	public boolean existeUsername(String username) {
		return repository.findByUsernameIgnoreCase(username) != null;
	}

	// Seguridad
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Usuario usuario = repository.findByUsernameIgnoreCase(username);

			List<GrantedAuthority> authorities = usuario.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
			return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled(), true, true, true,
					authorities);
		} catch (Exception e) {
			log.error("Error en el login usuario o contraseña invalidos");
			throw new UsernameNotFoundException("Error en el Login");
		}
	}

	@Override
	public Usuario buscarPorUsuario(String username) {
		return repository.findByUsernameIgnoreCase(username);
	}

	@Override
	public Usuario editarLogin(String id, Usuario usuario) {
		Usuario user = listarUno(id);
		if (user == null)
			return null;
		user.setIntentos(usuario.getIntentos());
		user.setEnabled(usuario.isEnabled());
		return repository.save(user);
	}

	@Override
	public Usuario editarRoles(Usuario usuario, String id) {
		Usuario user = repository.findById(id).orElse(null);
		if (user != null) {
			List<String> roles = new ArrayList<>();
			List<String> rolesNuevos = new ArrayList<>();
			
			rRepository.findAll().forEach(r -> roles.add(r.getRol()));
			List<String> rolesU = usuario.getRoles();

			rolesU.forEach(r -> {
				if (roles.contains(r) && !rolesNuevos.contains(r))
					rolesNuevos.add(r);
			});
			if (!(rolesNuevos.contains(roles.get(0))))
				rolesNuevos.add(roles.get(0));
			user.setRoles(rolesNuevos);
			return repository.save(user);
		} else {
			return null;
		}
	}

}
