package project.api.rest.infrastructure.web.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().cors().and().headers().frameOptions().disable().and().httpBasic().and()
				.authorizeRequests().anyRequest().authenticated().and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager()))
				.addFilter(new JWTAuthorizationFilter(authenticationManager())).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {

		registry.addMapping("/login").allowedOrigins("*").allowedMethods("POST")
				.exposedHeaders(SecurityConstants.AUTHORIZATION_HEADER);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//autorizar cadastro de usuario
		//web.ignoring().antMatchers(HttpMethod.POST, "/cadaster");
	}
}
