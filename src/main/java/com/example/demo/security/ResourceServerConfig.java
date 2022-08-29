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
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

	@Autowired
	private JwtTokenStore jwtTokenStore;
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(jwtTokenStore);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
		.antMatchers(HttpMethod.GET,"/productos","/productos/{id}").permitAll()
		.antMatchers(HttpMethod.POST,"/usuarios/crear").permitAll()
		.antMatchers(HttpMethod.POST,"/cloudinary/**").authenticated()
		.antMatchers(HttpMethod.DELETE,"/cloudinary/**").hasRole("ADMIN")
//		.antMatchers(HttpMethod.POST,"/productos").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST,"/productos").authenticated()
		.antMatchers(HttpMethod.PUT,"/productos/{id}").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST,"/productos/compraUsuario").hasRole("USER")
		.antMatchers(HttpMethod.PUT,"/productos/compraTienda").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/usuarios/{id}").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.DELETE,"/productos/{id}","/usuarios/{id}").hasRole("ADMIN")
		.anyRequest().authenticated();
			
	}
	
}
