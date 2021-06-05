package project.api.rest.infrastructure.web.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import project.api.rest.domain.user.AppUser;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	// método que faz a autenticação pra ver se o login e senha são válidos
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			// ObjectMapper é responsavel por converter objetos em json e vice versa
			ObjectMapper mapper = new ObjectMapper();
			AppUser appUser = mapper.readValue(request.getInputStream(), AppUser.class);
			// aqui estou fazendo a autenticação, pra ver se o login e senha são corretos
			UsernamePasswordAuthenticationToken upta = new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword());
			return authenticationManager.authenticate(upta);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	//método para caso a autenticação seja um sucesso
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		//objeto userDetails que tem todas as informçãoes da class AppUser
		UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
		
		//informações que vão dentro do token
		String jwtToken = Jwts.builder()
			.setSubject(userDetails.getUsername())//setando o user name no token
			.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))//setando o tempo de expiração do token
			.claim("admin", userDetails.getAdmin())
			.claim("username", userDetails.getUsername())
			.claim("id", userDetails.getId())//aqui vai as informações adicionais do token, aqui posso enviar se o usuario é administrador ou não pelo token, pra bloquear as telas no front
			.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY)//algoritmo de criptografia e a senha secreta do token
			.compact();
		
		//envia o token pro front
		response.addHeader(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.TOKEN_PREFIX + jwtToken);
	}

}
