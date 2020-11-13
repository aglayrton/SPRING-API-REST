package spring.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.api.rest.entitys.Usuarios;

//Estabele o nosso gerenciador de token
//Classe controlador, tem o construtor que estabele a conexao, retorna o usuario processado e dá o sucesso da autenticação

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter{

	//Configurando o gerenciado de autenticação
	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {
		
		//Obriga a autenticar a url
		super(new AntPathRequestMatcher(url));
		
		//Gerenciado de autenticacao
		setAuthenticationManager(authenticationManager);
	}
	
	//Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
		/*Pega o token para validar*/
		Usuarios user = new ObjectMapper().readValue(request.getInputStream(), Usuarios.class);
		
		/*Retorna o usuario login, senha e acessos*/
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
		
	}

}
