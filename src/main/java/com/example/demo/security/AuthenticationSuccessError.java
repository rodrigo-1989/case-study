package com.example.demo.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

@Component
public class AuthenticationSuccessError implements AuthenticationEventPublisher {

	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessError.class);
	@Autowired
	private UsuarioRepository repository;
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		if(authentication.getDetails() instanceof WebAuthenticationDetails) 
			return;
		
		log.info("*****Usuario autenticado***** "+authentication.getName());
		Usuario usuario = repository.findByUsernameIgnoreCase(authentication.getName());
		usuario.setIntentos(0);
		repository.save(usuario);
		
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		log.error("Error, en el login "+exception.getMessage());
		
		try {
			Usuario usuario = repository.findByUsernameIgnoreCase(authentication.getName());
			usuario.setIntentos(usuario.getIntentos()+1);
			if(usuario.getIntentos() >= 3) {				
				usuario.setEnabled(false);
				log.info(String.format("El usuario %s se desabilito por ecceder los intentos" ,usuario.getName()) );
			}
			repository.save(usuario);
		} catch (Exception e) {
			log.error(String.format("El usuario %s NO existe en el sistema " , authentication.getName()));
		}
		
	}

	
}
