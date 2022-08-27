package com.example.demo.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.example.demo.entity.Imagen;
import com.example.demo.entity.Usuario;
import com.example.demo.repository.ImagenRepository;
import com.example.demo.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService,UserDetailsService {
	private Logger log = LoggerFactory.getLogger(UsuarioServiceImpl.class);
	@Autowired
	private UsuarioRepository repository;
	@Autowired
	private ImagenRepository iRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Value("${url.image.nousuario}")
	private String noUsuarioUrl;
	@Value("${url.idnousuario}")
	private String noUsuarioId;

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
		usuario.setImage( (usuario.getImage() == null) ?  noUsuarioUrl:usuario.getImage() );
		usuario.setRoles(Arrays.asList("ROLE_USER"));
		usuario.setPassword(encoder.encode(usuario.getPassword()));
		
		Usuario u = repository.save(usuario);
		iRepository.save(new Imagen(u.getName()+".jpeg",u.getImage(),noUsuarioUrl,null,u.getId()));
		return u;
	}

	@Override
	public Usuario editar(String id, Usuario usuario) {
		Usuario user = listarUno(id);
		if (user == null)
			return null;
		user.setName(usuario.getName());
		user.setPassword(encoder.encode(usuario.getPassword()));
		return repository.save(user);
	}

	@Override
	public  Map<String, Object> eliminar(String id) {
		Map<String, Object> respuesta = new HashMap<String, Object>();
		Usuario usuario = listarUno(id);
		if(usuario == null) {
			respuesta.put("ok", false);
			respuesta.put("msg", "El usuario que intentas eliminar no existe en la BD");
			return respuesta;
		}
		if(!usuario.isEnabled()) {
			respuesta.put("ok", false);
			respuesta.put("msg", "El usuario que intentas eliminar esta dado de baja contacta al administrador");
			return respuesta;
		}
		usuario.setEnabled(false);
		repository.save(usuario);
		respuesta.put("ok", true);
		respuesta.put("msg",  "Usuario dado de baja");
		return respuesta;

	}

	@Override
	public boolean existeCorreo(String correo) {
		return repository.findByEmail(correo) != null;
	}

	@Override
	public boolean existeUsername(String username) {
		return repository.findByUsername(username) != null;
	}

	//Seguridad
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			Usuario usuario = repository.findByUsername(username);
			
			List<GrantedAuthority> authorities = usuario.getRoles()
					.stream().map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());
			return new User(usuario.getUsername(),usuario.getPassword(),usuario.isEnabled(),true,true,true,authorities);
		} catch (Exception e) {
			log.error("Error en el login usuario o contraseña invalidos");
			throw new UsernameNotFoundException("Error en el Login");
		}
	}

	@Override
	public Usuario buscarPorUsuario(String username) {
		return repository.findByUsername(username);
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

}
