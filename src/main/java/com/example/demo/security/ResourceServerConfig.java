package com.example.demo.security;

//import java.util.Arrays;
//import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
				.antMatchers(HttpMethod.GET, "/swagger-ui.html/**").permitAll()
				.antMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
				.antMatchers(HttpMethod.GET, "/webjars/**").permitAll()
				.antMatchers(HttpMethod.GET, "/v2/**").permitAll()
				.antMatchers(HttpMethod.POST, "/usuarios/crear").permitAll()
				.antMatchers(HttpMethod.GET, "/productos", "/productos/{id}", "/usuarios/{id}").permitAll()
				.antMatchers(HttpMethod.POST, "/cloudinary/**").authenticated()
				.antMatchers(HttpMethod.DELETE, "/cloudinary/**").hasRole("ADMIN")
//		.antMatchers(HttpMethod.POST,"/productos").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/productos").authenticated()
				.antMatchers(HttpMethod.PUT, "/productos/{id}").hasRole("ADMIN")
				.antMatchers(HttpMethod.POST, "/productos/compraUsuario").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.PUT, "/productos/compraTienda").hasRole("ADMIN")
				.antMatchers(HttpMethod.PUT, "/usuarios/{id}").hasAnyRole("ADMIN", "USER")
				.antMatchers(HttpMethod.DELETE, "/productos/{id}", "/usuarios/{id}").hasRole("ADMIN").anyRequest()
				.authenticated();

	}

//	@Bean
//	CorsConfigurationSource corsConfigurationSource() {
//		CorsConfiguration cc = new CorsConfiguration();
//		cc.setAllowedHeaders(Arrays.asList("Origin,Accept", "X-Requested-With", "Content-Type",
//				"Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization"));
//		cc.setExposedHeaders(Arrays.asList("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
//		cc.setAllowedOrigins(Arrays.asList("/*"));
//		cc.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "PATCH"));
//		cc.addAllowedOrigin("*");
//		cc.setMaxAge(Duration.ZERO);
//		cc.setAllowCredentials(Boolean.TRUE);
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		source.registerCorsConfiguration("/**", cc);
//		return source;
//	}

}
