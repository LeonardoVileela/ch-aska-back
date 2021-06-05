package project.api.rest.infrastructure.web.security;

public class SecurityConstants {

	//chave secreta para o JWT
	public static final String SECRET_KEY = "Yuumi";
	//Tempo de expira��o do token em mili segundos
	public static final long EXPIRATION_TIME = 8640000;
	//header do token
	public static final String AUTHORIZATION_HEADER = "Authorization";
	//padr�o do token � come�ar com bearer
	public static final String TOKEN_PREFIX = "Bearer ";
	
}
