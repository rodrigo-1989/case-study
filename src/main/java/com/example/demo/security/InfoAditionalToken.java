package com.example.demo.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;


@Component
public class InfoAditionalToken implements TokenEnhancer {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		Map<String, Object> info = new HashMap<>();
		Usuario usuario = repository.findByUsernameIgnoreCase(authentication.getName());
		info.put("nombre", usuario.getName());
		info.put("usuario", usuario.getUsername());
		info.put("imagen", usuario.getImage());
		info.put("rol", usuario.getRoles());
		info.put("id", usuario.getId());
		info.put("email", usuario.getEmail());
		(( DefaultOAuth2AccessToken ) accessToken).setAdditionalInformation(info);
		return accessToken;
	}

}
