package com.example.demo.security;

//import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;

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
		.antMatchers(HttpMethod.DELETE,"/cloudinary/**").authenticated()
//		.antMatchers(HttpMethod.POST,"/productos").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST,"/productos").authenticated()
		.antMatchers(HttpMethod.PUT,"/productos/{id}").hasRole("ADMIN")
		.antMatchers(HttpMethod.POST,"/productos/compraUsuario").hasRole("USER")
		.antMatchers(HttpMethod.PUT,"/productos/compraTienda").hasRole("ADMIN")
		.antMatchers(HttpMethod.PUT,"/usuarios/{id}").hasAnyRole("ADMIN","USER")
		.antMatchers(HttpMethod.DELETE,"/productos/{id}","/usuarios/{id}").hasRole("ADMIN")
		.anyRequest().authenticated();
//		.and().cors().configurationSource(corsConfigurationSource());	
	}

//	public CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration corsConfig = new CorsConfiguration();
//		corsConfig.setAllowedOrigins(Arrays.asList("*"));
//		corsConfig.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
//		corsConfig.setAllowCredentials(true);
//		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//		
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", corsConfig);
//		return source;
//	}
//	
//	@Bean
//	public FilterRegistrationBean<CorsFilter> corsFilter(){
//		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
//		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		return bean;
//	}
	
}
