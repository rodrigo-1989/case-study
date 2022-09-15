package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private JwtTokenStore jwtTokenStore;

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(jwtTokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.GET, "/webjars/**", "/v2/**", "/swagger-resources/**", "/swagger-ui/**",
						"/productos").permitAll()
				.antMatchers(HttpMethod.POST, "/usuarios/crear").permitAll()
				.antMatchers(HttpMethod.GET, "/usuarios/{id}", "/productos/buscarParecidos/{nombre}"
						, "/productos/{id}").authenticated()
				.antMatchers("/cloudinary/**").authenticated()
				.antMatchers(HttpMethod.GET,"/usuarios").hasRole("ADMIN")
				.antMatchers( "/pedidos","/pedidos/{id}").hasAnyRole("ADMIN", "COMPRAS")
				.antMatchers(HttpMethod.POST, "/productos").hasAnyRole("ADMIN", "COMPRAS")
				.antMatchers(HttpMethod.PUT, "/productos/{id}").hasAnyRole("ADMIN", "COMPRAS")
				.antMatchers(HttpMethod.POST, "/productos/compraUsuario/{id}").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.PUT, "/productos/compraTienda").hasAnyRole("ADMIN", "COMPRAS")
				.antMatchers(HttpMethod.PUT, "/usuarios/{id}").authenticated()
				.antMatchers(HttpMethod.DELETE, "/productos/{id}").hasAnyRole("ADMIN", "COMPRAS")
				.antMatchers(HttpMethod.PUT, "/usuarios/editarRoles/{id}").hasRole("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN")
				.anyRequest().authenticated();

	}

}
